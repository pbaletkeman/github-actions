use std::path::Path;

use regex::Regex;

use crate::error::{QuizError, Result};
use crate::models::NewQuestion;

/// Parse a markdown file and extract quiz questions.
///
/// Expected format per question block:
/// ```markdown
/// ## Q1
/// > What is Continuous Integration?
/// - A) Continuous Integration
/// - B) Code Import
/// - C) Compile
/// - D) Configure
/// **Answer: A**
/// > Explanation text (optional)
/// ```
pub fn parse_markdown_file(path: &Path) -> Result<Vec<NewQuestion>> {
    let content = std::fs::read_to_string(path).map_err(QuizError::Io)?;
    parse_markdown_content(&content, path.to_string_lossy().as_ref())
}

/// Parse markdown content string into questions.
pub fn parse_markdown_content(content: &str, source_file: &str) -> Result<Vec<NewQuestion>> {
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
