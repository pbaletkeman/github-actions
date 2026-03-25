use std::io::{self, Write};

/// Prompt the user with a question and return their trimmed input.
pub fn prompt(message: &str) -> String {
    print!("{message}");
    io::stdout().flush().ok();
    let mut input = String::new();
    io::stdin().read_line(&mut input).ok();
    input.trim().to_string()
}

/// Ask the user to choose an option (1-indexed) and return the 0-based index.
pub fn prompt_choice(question: &str, options: &[String]) -> usize {
    loop {
        println!("{question}");
        for (i, opt) in options.iter().enumerate() {
            let letter = match i {
                0 => "A",
                1 => "B",
                2 => "C",
                3 => "D",
                4 => "E",
                _ => "?",
            };
            println!("  {letter}) {opt}");
        }
        let input = prompt("Your answer [A/B/C/D]: ").to_uppercase();
        let idx = match input.as_str() {
            "A" => 0,
            "B" => 1,
            "C" => 2,
            "D" => 3,
            "E" => 4,
            _ => {
                println!("Invalid input. Please enter A, B, C, D, or E.");
                continue;
            }
        };
        if idx < options.len() {
            return idx;
        }
        println!("Invalid option. Please choose from the available letters.");
    }
}

/// Ask a yes/no confirmation question.
pub fn confirm(message: &str) -> bool {
    let input = prompt(&format!("{message} [y/N]: ")).to_lowercase();
    matches!(input.as_str(), "y" | "yes")
}
