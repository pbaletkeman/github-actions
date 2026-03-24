import pytest
from quiz_engine.database import DatabaseManager
from quiz_engine.models import Question


@pytest.fixture
def db(tmp_path):
    db_path = str(tmp_path / "test.db")
    manager = DatabaseManager(db_path)
    manager.init_schema()
    yield manager
    manager.close()


@pytest.fixture
def sample_questions():
    return [
        Question(question_text="Q1 text", option_a="Opt A", option_b="Opt B",
                 option_c="Opt C", option_d="Opt D", correct_answer="A"),
        Question(question_text="Q2 text", option_a="Opt A", option_b="Opt B",
                 option_c="Opt C", option_d="Opt D", correct_answer="B"),
        Question(question_text="Q3 text", option_a="Opt A", option_b="Opt B",
                 option_c="Opt C", option_d="Opt D", correct_answer="C"),
        Question(question_text="Q4 text", option_a="Opt A", option_b="Opt B",
                 option_c="Opt C", option_d="Opt D", correct_answer="D"),
    ]


@pytest.fixture
def populated_db(db, sample_questions):
    for q in sample_questions:
        db.insert_question(q)
    return db
