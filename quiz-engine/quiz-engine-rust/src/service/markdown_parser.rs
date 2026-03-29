use std::collections::HashMap;
use std::path::Path;

use regex::Regex;

use crate::error::{QuizError, Result};
use crate::models::NewQuestion;

/// Parse a markdown file and extract quiz questions.
/// Supports gh-200 format (with `## Answer Key` table) and legacy format.
pub fn parse_markdown_file(path: &Path) -> Result<Vec<NewQuestion>> {
    let content = std::fs::read_to_string(path).map_err(QuizError::Io)?;
    parse_markdown_content(&content, path.to_string_lossy().as_ref())
}

/// Parse markdown content string into questions.
/// Detects format automatically: gh-200 if `## Answer Key` present, otherwise legacy.
pub fn parse_markdown_content(content: &str, source_file: &str) -> Result<Vec<NewQuestion>> {
    if content.contains("## Answer Key") {
        parse_gh200_content(content, source_file)
    } else {
        parse_legacy_content(content, source_file)
    }
}

/// Parse gh-200 format: `### Question N — Section` headers with `## Answer Key` table.
fn parse_gh200_content(content: &str, source_file: &str) -> Result<Vec<NewQuestion>> {
    let answer_key = parse_answer_key(content);
    let header_re = Regex::new(r"(?m)^### Question (\d+)(?:\s*[—–-]+\s*(.+))?$").unwrap();
    let option_re = Regex::new(r"^-\s+([A-Ea-e])\)\s+(.+)$").unwrap();

    // Collect block start positions, question numbers, and sections
    let mut block_starts: Vec<(usize, u32, Option<String>)> = Vec::new();
    for cap in header_re.captures_iter(content) {
        let q_num: u32 = cap[1].parse().unwrap_or(0);
        let section = cap.get(2).map(|m| m.as_str().trim().to_string());
        let start = cap.get(0).unwrap().start();
        block_starts.push((start, q_num, section));
    }

    let ak_boundary = content.find("\n## Answer Key").unwrap_or(content.len());
    let mut questions = Vec::new();

    for (i, (start, q_num, section)) in block_starts.iter().enumerate() {
        let end = if i + 1 < block_starts.len() {
            block_starts[i + 1].0
        } else {
            ak_boundary
        };

        let block = &content[*start..end];

        let mut difficulty: Option<String> = None;
        let mut answer_type: Option<String> = None;
        let mut option_a: Option<String> = None;
        let mut option_b: Option<String> = None;
        let mut option_c: Option<String> = None;
        let mut option_d: Option<String> = None;
        let mut option_e: Option<String> = None;
        let mut in_scenario = false;
        let mut in_question = false;
        let mut scenario_lines: Vec<String> = Vec::new();
        let mut question_lines: Vec<String> = Vec::new();

        for line in block.lines() {
            let line = line.trim();

            if line.is_empty() || line == "---" {
                // End text collection once options have started
                if option_a.is_some() {
                    in_scenario = false;
                    in_question = false;
                }
                continue;
            }

            // Metadata — reset text collection state
            if let Some(val) = line.strip_prefix("**Difficulty**:") {
                in_scenario = false;
                in_question = false;
                difficulty = Some(val.trim().to_string());
                continue;
            }
            if let Some(val) = line.strip_prefix("**Answer Type**:") {
                in_scenario = false;
                in_question = false;
                answer_type = Some(val.trim().to_string());
                continue;
            }
            if line.starts_with("**Topic**:") || line.starts_with("**Tags**:") {
                in_scenario = false;
                in_question = false;
                continue;
            }
            if line == "**Scenario**:" {
                in_scenario = true;
                in_question = false;
                continue;
            }
            if line == "**Question**:" {
                in_scenario = false;
                in_question = true;
                continue;
            }

            // Option line
            if let Some(cap) = option_re.captures(line) {
                in_scenario = false;
                in_question = false;
                let letter = cap[1].to_uppercase();
                let text = cap[2].trim().to_string();
                match letter.as_str() {
                    "A" => option_a = Some(text),
                    "B" => option_b = Some(text),
                    "C" => option_c = Some(text),
                    "D" => option_d = Some(text),
                    "E" => option_e = Some(text),
                    _ => {}
                }
                continue;
            }

            // Skip the header line itself
            if line.starts_with("### Question") {
                continue;
            }

            if in_scenario {
                scenario_lines.push(line.to_string());
            } else if in_question {
                question_lines.push(line.to_string());
            }
        }

        // Skip multi-answer and none answer types
        if let Some(ref at) = answer_type {
            let at_lower = at.to_lowercase();
            if at_lower == "many" || at_lower == "none" {
                continue;
            }
        }

        // Look up answer from answer key (multi-answer questions are excluded from key)
        let (correct_answer, explanation) = match answer_key.get(q_num) {
            Some(entry) => entry.clone(),
            None => continue,
        };

        // Build question text, prepending scenario if present
        let question_part = question_lines.join(" ");
        let question_part = question_part.trim();
        let question_text = if !scenario_lines.is_empty() {
            let scenario = scenario_lines.join(" ");
            let scenario = scenario.trim();
            if question_part.is_empty() {
                scenario.to_string()
            } else {
                format!("{scenario} {question_part}")
            }
        } else {
            question_part.to_string()
        };

        if question_text.is_empty() {
            continue;
        }

        let a = match option_a { Some(v) => v, None => continue };
        let b = match option_b { Some(v) => v, None => continue };
        let c = match option_c { Some(v) => v, None => continue };
        let d = match option_d { Some(v) => v, None => continue };

        questions.push(NewQuestion {
            question_text,
            option_a: a,
            option_b: b,
            option_c: c,
            option_d: d,
            option_e,
            correct_answer,
            explanation,
            section: section.clone(),
            difficulty,
            source_file: Some(source_file.to_string()),
        });
    }

    Ok(questions)
}

/// Parse the `## Answer Key` table.
/// Returns a map of question number → (answer_letter, explanation).
/// Multi-answer rows (containing comma) are excluded.
fn parse_answer_key(content: &str) -> HashMap<u32, (String, Option<String>)> {
    let mut map = HashMap::new();

    let ak_start = match content.find("## Answer Key") {
        Some(pos) => pos,
        None => return map,
    };

    let row_re = Regex::new(r"^\|\s*(\d+)\s*\|\s*([^|]+?)\s*\|\s*([^|]+?)\s*\|").unwrap();

    for line in content[ak_start..].lines() {
        let line = line.trim();
        if !line.starts_with('|') || line.contains("---") {
            continue;
        }
        let lower = line.to_lowercase();
        if lower.contains("q#") || lower.contains("answer(s)") {
            continue;
        }

        if let Some(cap) = row_re.captures(line) {
            let q_num: u32 = match cap[1].parse() {
                Ok(n) => n,
                Err(_) => continue,
            };
            let answer = cap[2].trim().to_string();
            let explanation = cap[3].trim().to_string();

            // Skip multi-answer rows
            if answer.contains(',') {
                continue;
            }

            // Must be a single letter A-E
            let upper = answer.to_uppercase();
            if upper.len() == 1 && matches!(upper.as_str(), "A" | "B" | "C" | "D" | "E") {
                let expl = if explanation.is_empty() { None } else { Some(explanation) };
                map.insert(q_num, (upper, expl));
            }
        }
    }

    map
}

/// Parse legacy format: `## Q1` / `### Q1` headers, `> question` blockquotes, `**Answer: X**`.
fn parse_legacy_content(content: &str, source_file: &str) -> Result<Vec<NewQuestion>> {
    let answer_re = Regex::new(r"(?i)\*\*Answer:\s*([A-Ea-e])\*\*").unwrap();
    let option_re = Regex::new(r"^-\s+([A-Ea-e])\)\s+(.+)$").unwrap();
    let question_re = Regex::new(r"^>\s+(.+)$").unwrap();
    let explanation_re = Regex::new(r"(?i)>\s+(?:Explanation|Note|Hint):\s*(.+)").unwrap();

    let mut questions = Vec::new();

    // Split by question headers (## Q<n> or ### Q<n>)
    let header_re = Regex::new(r"(?m)^#{1,3}\s+Q\d+").unwrap();
    let blocks: Vec<&str> = header_re.split(content).collect();

    // First block is whatever comes before the first header (skip it)
    let question_blocks: &[&str] = if blocks.len() > 1 { &blocks[1..] } else { &blocks };

    for block in question_blocks {
        let mut question_text: Option<String> = None;
        let mut option_a: Option<String> = None;
        let mut option_b: Option<String> = None;
        let mut option_c: Option<String> = None;
        let mut option_d: Option<String> = None;
        let mut option_e: Option<String> = None;
        let mut correct_answer: Option<String> = None;
        let mut explanation: Option<String> = None;

        for line in block.lines() {
            let line = line.trim();
            if line.is_empty() {
                continue;
            }

            // Match answer line first
            if let Some(cap) = answer_re.captures(line) {
                correct_answer = Some(cap[1].to_uppercase());
                continue;
            }

            // Match explanation
            if let Some(cap) = explanation_re.captures(line) {
                explanation = Some(cap[1].trim().to_string());
                continue;
            }

            // Match options
            if let Some(cap) = option_re.captures(line) {
                let letter = cap[1].to_uppercase();
                let text = cap[2].trim().to_string();
                match letter.as_str() {
                    "A" => option_a = Some(text),
                    "B" => option_b = Some(text),
                    "C" => option_c = Some(text),
                    "D" => option_d = Some(text),
                    "E" => option_e = Some(text),
                    _ => {}
                }
                continue;
            }

            // Match question text (blockquote)
            if let Some(cap) = question_re.captures(line) {
                let text = cap[1].trim().to_string();
                if question_text.is_none() {
                    question_text = Some(text);
                }
            }
        }

        // Validate we have a complete question
        let correct = match correct_answer {
            Some(ref a) => a.clone(),
            None => continue, // skip blocks without an answer
        };

        let q_text = match question_text {
            Some(t) => t,
            None => continue,
        };

        let a = match option_a {
            Some(a) => a,
            None => continue,
        };
        let b = match option_b {
            Some(b) => b,
            None => continue,
        };
        let c = match option_c {
            Some(c) => c,
            None => continue,
        };
        let d = match option_d {
            Some(d) => d,
            None => continue,
        };

        // Validate correct_answer letter
        if !["A", "B", "C", "D", "E"].contains(&correct.as_str()) {
            return Err(QuizError::ParseError {
                file: source_file.to_string(),
                message: format!("Invalid answer letter: '{correct}'"),
            });
        }

        questions.push(NewQuestion {
            question_text: q_text,
            option_a: a,
            option_b: b,
            option_c: c,
            option_d: d,
            option_e,
            correct_answer: correct,
            explanation,
            source_file: Some(source_file.to_string()),
            ..Default::default()
        });
    }

    // If content had answer lines but no valid questions were parsed, it's an error
    if questions.is_empty() && answer_re.is_match(content) {
        // There were answer lines but no complete question blocks — indicates format error
        // Check for invalid answer letter
        for cap in answer_re.captures_iter(content) {
            let letter = cap[1].to_uppercase();
            if !["A", "B", "C", "D", "E"].contains(&letter.as_str()) {
                return Err(QuizError::ParseError {
                    file: source_file.to_string(),
                    message: format!("Invalid answer letter: '{letter}'"),
                });
            }
        }
    }

    // Validate: if content had answer markers with no complete questions, signal error
    let has_answer_line = answer_re.is_match(content);
    let has_question_header = header_re.is_match(content);
    if has_answer_line && !has_question_header && questions.is_empty() {
        return Err(QuizError::ParseError {
            file: source_file.to_string(),
            message: "No complete question blocks found (missing ## Q<n> headers)".to_string(),
        });
    }

    Ok(questions)
}

/// Validate that an answer letter is one of A-E.
pub fn validate_answer_letter(answer: &str) -> bool {
    matches!(answer.to_uppercase().as_str(), "A" | "B" | "C" | "D" | "E")
}

#[cfg(test)]
mod tests {
    use super::*;
    use std::io::Write;
    use tempfile::NamedTempFile;

    fn make_question_block(q: &str, a: &str, b: &str, c: &str, d: &str, ans: &str) -> String {
        format!(
            "## Q1\n> {q}\n- A) {a}\n- B) {b}\n- C) {c}\n- D) {d}\n**Answer: {ans}**\n"
        )
    }

    #[test]
    fn test_parse_valid_markdown_file() {
        let mut file = NamedTempFile::new().unwrap();
        let content = make_question_block(
            "What is CI?",
            "Continuous Integration",
            "Code Import",
            "Compile",
            "Configure",
            "A",
        );
        write!(file, "{content}").unwrap();
        let questions = parse_markdown_file(file.path()).unwrap();
        assert_eq!(questions.len(), 1);
        assert_eq!(questions[0].correct_answer, "A");
        assert_eq!(questions[0].question_text, "What is CI?");
    }

    #[test]
    fn test_parse_multiple_questions() {
        let mut file = NamedTempFile::new().unwrap();
        writeln!(file, "## Q1").unwrap();
        writeln!(file, "> Question one?").unwrap();
        writeln!(file, "- A) A1\n- B) B1\n- C) C1\n- D) D1").unwrap();
        writeln!(file, "**Answer: B**").unwrap();
        writeln!(file, "## Q2").unwrap();
        writeln!(file, "> Question two?").unwrap();
        writeln!(file, "- A) A2\n- B) B2\n- C) C2\n- D) D2").unwrap();
        writeln!(file, "**Answer: C**").unwrap();
        let questions = parse_markdown_file(file.path()).unwrap();
        assert_eq!(questions.len(), 2);
        assert_eq!(questions[0].correct_answer, "B");
        assert_eq!(questions[1].correct_answer, "C");
    }

    #[test]
    fn test_parse_with_explanation() {
        let mut file = NamedTempFile::new().unwrap();
        writeln!(file, "## Q1").unwrap();
        writeln!(file, "> What is CD?").unwrap();
        writeln!(file, "- A) Continuous Delivery\n- B) Code\n- C) Copy\n- D) Deploy").unwrap();
        writeln!(file, "**Answer: A**").unwrap();
        writeln!(file, "> Explanation: CD stands for Continuous Delivery").unwrap();
        let questions = parse_markdown_file(file.path()).unwrap();
        assert_eq!(questions.len(), 1);
        assert!(questions[0].explanation.is_some());
    }

    #[test]
    fn test_parse_with_five_options() {
        let mut file = NamedTempFile::new().unwrap();
        writeln!(file, "## Q1").unwrap();
        writeln!(file, "> Multi-option?").unwrap();
        writeln!(file, "- A) Opt1\n- B) Opt2\n- C) Opt3\n- D) Opt4\n- E) Opt5").unwrap();
        writeln!(file, "**Answer: E**").unwrap();
        let questions = parse_markdown_file(file.path()).unwrap();
        assert_eq!(questions.len(), 1);
        assert_eq!(questions[0].correct_answer, "E");
        assert!(questions[0].option_e.is_some());
    }

    #[test]
    fn test_parse_empty_file_returns_empty_vec() {
        let file = NamedTempFile::new().unwrap();
        let questions = parse_markdown_file(file.path()).unwrap();
        assert!(questions.is_empty());
    }

    #[test]
    fn test_parse_skips_incomplete_blocks() {
        // Block with answer but missing options should be skipped
        let content = "## Q1\n> Some question\n**Answer: A**\n";
        let result = parse_markdown_content(content, "test.md").unwrap();
        assert!(result.is_empty());
    }

    #[test]
    fn test_validate_answer_letter() {
        assert!(validate_answer_letter("A"));
        assert!(validate_answer_letter("b"));
        assert!(validate_answer_letter("E"));
        assert!(!validate_answer_letter("F"));
        assert!(!validate_answer_letter("Z"));
        assert!(!validate_answer_letter("1"));
    }

    #[test]
    fn test_parse_content_directly() {
        let content =
            "## Q1\n> Direct parse?\n- A) Yes\n- B) No\n- C) Maybe\n- D) Never\n**Answer: A**\n";
        let questions = parse_markdown_content(content, "inline").unwrap();
        assert_eq!(questions.len(), 1);
        assert_eq!(questions[0].source_file, Some("inline".to_string()));
    }
}
