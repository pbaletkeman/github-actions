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


GH200_MD = """# GitHub Actions GH-200

## Questions

---

### Question 1 \u2014 VS Code Extension

**Difficulty**: Easy
**Answer Type**: one
**Topic**: Extension capabilities

**Question**:
What is the primary purpose of the VS Code extension?

- A) Execute workflow runs locally
- B) Provide YAML schema validation and IntelliSense
- C) Deploy files directly to GitHub
- D) Manage secrets from within the IDE

---

### Question 2 \u2014 Context

**Difficulty**: Medium
**Answer Type**: one
**Topic**: Scenario use

**Scenario**:
A developer wants to catch syntax errors before committing.

**Question**:
Which capability directly addresses this need without a workflow run?

- A) Run workflows locally using act
- B) Real-time YAML syntax validation with inline error highlighting
- C) Submit the file to a remote linter API
- D) Dry-run all run steps using a local shell

---

### Question 3 \u2014 Context

**Difficulty**: Hard
**Answer Type**: many
**Topic**: Multiple correct answers

**Question**:
Which of the following apply?

- A) Option one
- B) Option two
- C) Option three
- D) Option four

---

## Answer Key

| Q# | Answer(s) | Explanation | Source | Difficulty |
|----|-----------|-------------|--------|------------|
| 1 | B | The extension provides YAML schema validation and IntelliSense. | file.md | Easy |
| 2 | B | Real-time validation highlights errors without running the workflow. | file.md | Medium |
| 3 | A, B | Multiple correct answers. | file.md | Hard |
"""


def test_parse_gh200_format_reads_answer_key(tmp_path):
    md_file = tmp_path / "gh200.md"
    md_file.write_text(GH200_MD, encoding='utf-8')
    questions = parse_markdown_file(str(md_file))
    # Q3 is 'many' type — should be skipped
    assert len(questions) == 2
    assert questions[0].correct_answer == "B"
    assert questions[1].correct_answer == "B"


def test_parse_gh200_extracts_section_and_difficulty(tmp_path):
    md_file = tmp_path / "gh200.md"
    md_file.write_text(GH200_MD, encoding='utf-8')
    questions = parse_markdown_file(str(md_file))
    assert questions[0].section == "VS Code Extension"
    assert questions[0].difficulty == "Easy"
    assert questions[1].difficulty == "Medium"


def test_parse_gh200_extracts_explanation_from_key(tmp_path):
    md_file = tmp_path / "gh200.md"
    md_file.write_text(GH200_MD, encoding='utf-8')
    questions = parse_markdown_file(str(md_file))
    assert questions[0].explanation is not None
    assert "IntelliSense" in questions[0].explanation


def test_parse_gh200_scenario_question_text(tmp_path):
    md_file = tmp_path / "gh200.md"
    md_file.write_text(GH200_MD, encoding='utf-8')
    questions = parse_markdown_file(str(md_file))
    # Q2 has both a Scenario and a Question block — both should appear in text
    assert "catch syntax errors" in questions[1].question_text
    assert "directly addresses" in questions[1].question_text


def test_parse_gh200_skips_many_type(tmp_path):
    md_file = tmp_path / "gh200.md"
    md_file.write_text(GH200_MD, encoding='utf-8')
    questions = parse_markdown_file(str(md_file))
    # Q3 has Answer Type: many and a multi-answer key entry — must be excluded
    assert all(q.question_text != "Which of the following apply?" for q in questions)


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
