import pytest
import os
from quiz_engine.utils import shuffle_answers, calculate_score, format_time, parse_markdown_file


def test_shuffle_answers_preserves_all_options():
    options = ["Option A text", "Option B text", "Option C text", "Option D text"]
    result = shuffle_answers(options, "A")
    assert len(result.options) == 4
    assert set(result.options) == set(options)


def test_shuffle_answers_correct_position_tracked():
    options = ["Option A text", "Option B text", "Option C text", "Option D text"]
    result = shuffle_answers(options, "A")
    # The correct answer text is "Option A text"
    assert result.options[result.correct_shuffled_position] == "Option A text"


def test_shuffle_answers_with_5_options():
    options = ["Opt A", "Opt B", "Opt C", "Opt D", "Opt E"]
    result = shuffle_answers(options, "E")
    assert len(result.options) == 5
    assert result.options[result.correct_shuffled_position] == "Opt E"


def test_shuffle_answers_with_none_option():
    options = ["Opt A", "Opt B", "Opt C", "Opt D", None]
    result = shuffle_answers(options, "B")
    assert len(result.options) == 4
    assert result.options[result.correct_shuffled_position] == "Opt B"


def test_shuffle_answers_mapping():
    options = ["Opt A", "Opt B", "Opt C", "Opt D"]
    result = shuffle_answers(options, "C")
    # The answer mapping should map new labels to original labels
    assert len(result.answer_mapping) == 4
    # The new label at correct position maps back to 'C'
    new_correct_label = result.labels[result.correct_shuffled_position]
    assert result.answer_mapping[new_correct_label] == 'C'


def test_shuffle_answers_labels():
    options = ["A", "B", "C", "D"]
    result = shuffle_answers(options, "A")
    assert set(result.labels) == {'A', 'B', 'C', 'D'}


def test_calculate_score_perfect():
    assert calculate_score(10, 10) == 100.0


def test_calculate_score_zero():
    assert calculate_score(0, 10) == 0.0


def test_calculate_score_zero_total():
    assert calculate_score(0, 0) == 0.0


def test_calculate_score_partial():
    assert calculate_score(7, 10) == 70.0


def test_calculate_score_rounding():
    assert calculate_score(1, 3) == 33.3


def test_format_time_seconds():
    assert format_time(45) == "00:45"


def test_format_time_minutes():
    assert format_time(90) == "01:30"


def test_format_time_zero():
    assert format_time(0) == "00:00"


def test_format_time_large():
    assert format_time(3661) == "61:01"


def test_parse_markdown_file_basic(tmp_path):
    md_content = """# Quiz

## Question 1
What is GitHub Actions?

- A) A CI/CD platform
- B) A version control system
- C) A code editor
- D) A deployment server

**Answer: A**

GitHub Actions is a CI/CD platform.

## Question 2
Which file defines a workflow?

- A) package.json
- B) .github/workflows/main.yml
- C) Dockerfile
- D) Makefile

**Answer: B**
"""
    md_file = tmp_path / "test_quiz.md"
    md_file.write_text(md_content)

    questions = parse_markdown_file(str(md_file))
    assert len(questions) == 2
    assert "GitHub Actions" in questions[0].question_text
    assert questions[0].correct_answer == "A"
    assert questions[1].correct_answer == "B"


def test_parse_markdown_file_extracts_options(tmp_path):
    md_content = """## Question 1
What triggers a workflow?

- A) push event
- B) pull event
- C) merge event
- D) close event

**Answer: A**
"""
    md_file = tmp_path / "options_test.md"
    md_file.write_text(md_content)

    questions = parse_markdown_file(str(md_file))
    assert len(questions) == 1
    assert questions[0].option_a == "push event"
    assert questions[0].option_b == "pull event"
    assert questions[0].option_c == "merge event"
    assert questions[0].option_d == "close event"


def test_parse_markdown_file_with_explanation(tmp_path):
    md_content = """## Question 1
What is a runner?

- A) A virtual machine that runs jobs
- B) A code reviewer
- C) A build artifact
- D) A secret variable

**Answer: A**
A runner is a server that runs your workflow jobs.
"""
    md_file = tmp_path / "explain_test.md"
    md_file.write_text(md_content)

    questions = parse_markdown_file(str(md_file))
    assert len(questions) == 1
    assert questions[0].explanation is not None


def test_parse_markdown_file_no_questions(tmp_path):
    md_content = "# Just a README\n\nNo questions here."
    md_file = tmp_path / "empty.md"
    md_file.write_text(md_content)

    questions = parse_markdown_file(str(md_file))
    assert questions == []


def test_parse_markdown_file_with_5_options(tmp_path):
    md_content = """## Question 1
Which of the following are valid?

- A) Option one
- B) Option two
- C) Option three
- D) Option four
- E) All of the above

**Answer: E**
"""
    md_file = tmp_path / "five_options.md"
    md_file.write_text(md_content)

    questions = parse_markdown_file(str(md_file))
    assert len(questions) == 1
    assert questions[0].option_e == "All of the above"
    assert questions[0].correct_answer == "E"
