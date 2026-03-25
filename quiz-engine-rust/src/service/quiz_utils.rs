/// Calculate percentage score.
pub fn calculate_percentage(correct: usize, total: usize) -> f64 {
    if total == 0 {
        return 0.0;
    }
    (correct as f64 / total as f64) * 100.0
}

/// Return a grade letter for a given percentage.
pub fn grade_from_percentage(pct: f64) -> &'static str {
    match pct as u32 {
        90..=100 => "A",
        80..=89 => "B",
        70..=79 => "C",
        60..=69 => "D",
        _ => "F",
    }
}

/// Format seconds as "Xh Ym Zs" (hours shown only when >= 3600s).
pub fn format_duration(seconds: i64) -> String {
    let hours = seconds / 3600;
    let mins = (seconds % 3600) / 60;
    let secs = seconds % 60;
    if hours > 0 {
        format!("{hours}h {mins}m {secs}s")
    } else if mins > 0 {
        format!("{mins}m {secs}s")
    } else {
        format!("{secs}s")
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_calculate_percentage_full() {
        assert!((calculate_percentage(10, 10) - 100.0).abs() < f64::EPSILON);
    }

    #[test]
    fn test_calculate_percentage_half() {
        assert!((calculate_percentage(5, 10) - 50.0).abs() < f64::EPSILON);
    }

    #[test]
    fn test_calculate_percentage_zero_total() {
        assert!((calculate_percentage(0, 0)).abs() < f64::EPSILON);
    }

    #[test]
    fn test_grade_a() {
        assert_eq!(grade_from_percentage(95.0), "A");
        assert_eq!(grade_from_percentage(90.0), "A");
    }

    #[test]
    fn test_grade_b() {
        assert_eq!(grade_from_percentage(85.0), "B");
    }

    #[test]
    fn test_grade_f() {
        assert_eq!(grade_from_percentage(40.0), "F");
    }

    #[test]
    fn test_format_duration_seconds_only() {
        assert_eq!(format_duration(45), "45s");
    }

    #[test]
    fn test_format_duration_minutes_and_seconds() {
        assert_eq!(format_duration(125), "2m 5s");
    }

    #[test]
    fn test_format_duration_exact_minutes() {
        assert_eq!(format_duration(120), "2m 0s");
    }

    #[test]
    fn test_format_duration_hours() {
        assert_eq!(format_duration(3661), "1h 1m 1s");
    }

    #[test]
    fn test_format_duration_exactly_one_hour() {
        assert_eq!(format_duration(3600), "1h 0m 0s");
    }
}
