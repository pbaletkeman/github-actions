use rand::seq::SliceRandom;
use rand::thread_rng;

/// Result from shuffling a question's answers.
#[derive(Debug, Clone)]
pub struct ShuffleResult {
    pub shuffled_options: Vec<String>,
    pub correct_shuffled_index: usize,
}

/// Shuffle the provided options and track where the correct answer lands.
///
/// `correct_answer` is a letter `"A"`, `"B"`, `"C"`, `"D"`, or `"E"` indicating
/// which of the original options is correct.
pub fn shuffle_answers(options: &[String], correct_answer: &str) -> ShuffleResult {
    let correct_index = letter_to_index(correct_answer);
    let mut indexed: Vec<(usize, &String)> = options.iter().enumerate().collect();
    indexed.shuffle(&mut thread_rng());

    let correct_shuffled_index = indexed
        .iter()
        .position(|(orig_idx, _)| *orig_idx == correct_index)
        .unwrap_or(0);

    let shuffled_options: Vec<String> = indexed
        .into_iter()
        .map(|(_, s)| s.clone())
        .collect();

    ShuffleResult {
        shuffled_options,
        correct_shuffled_index,
    }
}

/// Convert an answer letter to a 0-based index.
pub fn letter_to_index(letter: &str) -> usize {
    match letter.to_uppercase().as_str() {
        "A" => 0,
        "B" => 1,
        "C" => 2,
        "D" => 3,
        "E" => 4,
        _ => 0,
    }
}

/// Convert a 0-based index to an answer letter.
pub fn index_to_letter(index: usize) -> &'static str {
    match index {
        0 => "A",
        1 => "B",
        2 => "C",
        3 => "D",
        4 => "E",
        _ => "A",
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use std::collections::HashSet;

    #[test]
    fn test_shuffle_preserves_all_options() {
        let options = vec![
            "Alpha".to_string(),
            "Beta".to_string(),
            "Gamma".to_string(),
            "Delta".to_string(),
        ];
        let result = shuffle_answers(&options, "A");
        let original: HashSet<&String> = options.iter().collect();
        let shuffled: HashSet<&String> = result.shuffled_options.iter().collect();
        assert_eq!(original, shuffled);
    }

    #[test]
    fn test_shuffle_maps_correct_answer_to_new_position() {
        let options = vec![
            "Alpha".to_string(),
            "Beta".to_string(),
            "Gamma".to_string(),
            "Delta".to_string(),
        ];
        let result = shuffle_answers(&options, "A");
        assert_eq!(result.shuffled_options[result.correct_shuffled_index], "Alpha");
    }

    #[test]
    fn test_shuffle_returns_four_options() {
        let options = vec![
            "A".to_string(),
            "B".to_string(),
            "C".to_string(),
            "D".to_string(),
        ];
        let result = shuffle_answers(&options, "C");
        assert_eq!(result.shuffled_options.len(), 4);
    }

    #[test]
    fn test_shuffle_five_options() {
        let options: Vec<String> = vec!["A", "B", "C", "D", "E"]
            .into_iter()
            .map(|s| s.to_string())
            .collect();
        let result = shuffle_answers(&options, "E");
        assert_eq!(result.shuffled_options.len(), 5);
        assert_eq!(result.shuffled_options[result.correct_shuffled_index], "E");
    }

    #[test]
    fn test_letter_to_index() {
        assert_eq!(letter_to_index("A"), 0);
        assert_eq!(letter_to_index("B"), 1);
        assert_eq!(letter_to_index("C"), 2);
        assert_eq!(letter_to_index("D"), 3);
        assert_eq!(letter_to_index("E"), 4);
        assert_eq!(letter_to_index("a"), 0); // lowercase
        assert_eq!(letter_to_index("Z"), 0); // unknown → 0
    }

    #[test]
    fn test_index_to_letter() {
        assert_eq!(index_to_letter(0), "A");
        assert_eq!(index_to_letter(1), "B");
        assert_eq!(index_to_letter(2), "C");
        assert_eq!(index_to_letter(3), "D");
        assert_eq!(index_to_letter(4), "E");
        assert_eq!(index_to_letter(99), "A"); // out of range → "A"
    }
}
