import pytest
import os
from quiz_engine.database import DatabaseManager
from quiz_engine.utils import parse_markdown_file


SAMPLE_MARKDOWN = """# GitHub Actions Quiz

## Question 1
What is GitHub Actions used for?

- A) Automating CI/CD workflows
- B) Managing database schemas
- C) Creating user interfaces
- D) Building mobile apps

**Answer: A**

GitHub Actions automates software development workflows.

## Question 2
Where are workflow files stored?

- A) In the root directory
- B) In .github/workflows/
- C) In the src/ directory
- D) In the config/ directory

**Answer: B**

## Question 3
What is the file format for workflow files?

- A) JSON
- B) XML
- C) YAML
- D) TOML

**Answer: C**
"""


def test_import_valid_markdown(tmp_path):
    md_file = tmp_path / "test_questions.md"
    md_file.write_text(SAMPLE_MARKDOWN)

    questions = parse_markdown_file(str(md_file))
    assert len(questions) == 3


def test_import_to_database(tmp_path):
    md_file = tmp_path / "import_test.md"
    md_file.write_text(SAMPLE_MARKDOWN)

    db_path = str(tmp_path / "import_test.db")
    db = DatabaseManager(db_path)
    db.init_schema()

    questions = parse_markdown_file(str(md_file))
    for q in questions:
        q.source_file = "import_test.md"
        db.insert_question(q)

    assert db.count_questions() == 3
    db.close()


def test_duplicate_detection(tmp_path):
    md_file = tmp_path / "dup_test.md"
    md_file.write_text(SAMPLE_MARKDOWN)

    db_path = str(tmp_path / "dup_test.db")
    db = DatabaseManager(db_path)
    db.init_schema()

    questions = parse_markdown_file(str(md_file))
    imported = 0
    skipped = 0
    for q in questions:
        result = db.insert_question(q)
        if result:
            imported += 1
        else:
            skipped += 1

    assert imported == 3
    assert skipped == 0

    # Import again - all should be skipped
    imported2 = 0
    skipped2 = 0
    for q in questions:
        result = db.insert_question(q)
        if result:
            imported2 += 1
        else:
            skipped2 += 1

    assert imported2 == 0
    assert skipped2 == 3
    db.close()


def test_invalid_file_handled(tmp_path):
    md_file = tmp_path / "invalid.md"
    md_file.write_text("This is not a quiz file. No questions here.")

    questions = parse_markdown_file(str(md_file))
    assert questions == []


def test_import_preserves_options(tmp_path):
    md_file = tmp_path / "opts_test.md"
    md_file.write_text(SAMPLE_MARKDOWN)

    questions = parse_markdown_file(str(md_file))
    assert len(questions) > 0

    q = questions[0]
    assert q.option_a != ""
    assert q.option_b != ""
    assert q.option_c != ""
    assert q.option_d != ""


def test_import_extracts_correct_answers(tmp_path):
    md_file = tmp_path / "answers_test.md"
    md_file.write_text(SAMPLE_MARKDOWN)

    questions = parse_markdown_file(str(md_file))
    answers = [q.correct_answer for q in questions]
    assert answers == ['A', 'B', 'C']


def test_import_script_function(tmp_path):
    """Test the import_file function from scripts."""
    import sys
    import os
    quiz_engine_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    if quiz_engine_dir not in sys.path:
        sys.path.insert(0, quiz_engine_dir)
    from scripts.import_questions import import_file

    md_file = tmp_path / "script_test.md"
    md_file.write_text(SAMPLE_MARKDOWN)

    db_path = str(tmp_path / "script_test.db")
    db = DatabaseManager(db_path)
    db.init_schema()

    imported, skipped, errors = import_file(db, str(md_file))
    assert imported == 3
    assert skipped == 0
    assert errors == 0
    db.close()
