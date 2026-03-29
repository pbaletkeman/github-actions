/// Format a fixed-width table with box-drawing characters.
pub fn print_table(headers: &[&str], rows: &[Vec<String>]) {
    if rows.is_empty() {
        println!("(no records)");
        return;
    }

    let col_count = headers.len();
    let mut widths: Vec<usize> = headers.iter().map(|h| h.len()).collect();

    for row in rows {
        for (i, cell) in row.iter().enumerate() {
            if i < col_count {
                widths[i] = widths[i].max(cell.len());
            }
        }
    }

    let separator: String = widths
        .iter()
        .map(|w| "─".repeat(w + 2))
        .collect::<Vec<_>>()
        .join("┼");

    let top: String = widths
        .iter()
        .map(|w| "─".repeat(w + 2))
        .collect::<Vec<_>>()
        .join("┬");

    let bottom: String = widths
        .iter()
        .map(|w| "─".repeat(w + 2))
        .collect::<Vec<_>>()
        .join("┴");

    println!("┌{top}┐");

    let header_row: String = headers
        .iter()
        .zip(widths.iter())
        .map(|(h, w)| format!(" {h:<w$} "))
        .collect::<Vec<_>>()
        .join("│");
    println!("│{header_row}│");

    println!("├{separator}┤");

    for row in rows {
        let cells: String = (0..col_count)
            .map(|i| {
                let cell = row.get(i).map(|s| s.as_str()).unwrap_or("");
                format!(" {cell:<width$} ", width = widths[i])
            })
            .collect::<Vec<_>>()
            .join("│");
        println!("│{cells}│");
    }

    println!("└{bottom}┘");
}

/// Print a labeled result box.
pub fn print_result_box(title: &str, lines: &[String]) {
    let max_width = lines
        .iter()
        .map(|l| l.len())
        .max()
        .unwrap_or(0)
        .max(title.len());

    let border = "═".repeat(max_width + 2);
    println!("╔{border}╗");
    println!("║ {title:<max_width$} ║");
    println!("╠{border}╣");
    for line in lines {
        println!("║ {line:<max_width$} ║");
    }
    println!("╚{border}╝");
}

/// Print a simple divider line.
pub fn print_divider(width: usize) {
    println!("{}", "─".repeat(width));
}

/// Print the end-of-quiz answer review.
/// Each entry: (question_number, question_text, user_letter, correct_letter, correct_answer_text, is_correct, explanation)
pub fn print_review(items: &[(usize, &str, &str, &str, &str, bool, Option<&str>)]) {
    if items.is_empty() {
        return;
    }

    println!();
    let border = "═".repeat(60);
    println!("╔{border}╗");
    println!("║ {:<58} ║", "Answer Review");
    println!("╚{border}╝");

    for &(num, question, user_letter, correct_letter, correct_text, is_correct, explanation) in items {
        println!();
        let marker = if is_correct { "✓" } else { "✗" };
        println!("  {marker} Q{num}: {question}");
        if is_correct {
            println!("    Your answer: {user_letter})  ✓");
        } else {
            println!("    Your answer:    {user_letter})");
            println!("    Correct answer: {correct_letter}) {correct_text}");
        }
        if let Some(expl) = explanation {
            println!("    Explanation: {expl}");
        }
    }
    println!();
}
