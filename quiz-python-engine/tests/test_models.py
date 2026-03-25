import pytest
from pydantic import ValidationError
from quiz_engine.models import Question, QuizSession, QuizResponse


def test_question_requires_nonempty_text():
    with pytest.raises(ValidationError):
        Question(question_text="", option_a="A", option_b="B",
                 option_c="C", option_d="D")


def test_question_requires_whitespace_only_text():
    with pytest.raises(ValidationError):
        Question(question_text="   ", option_a="A", option_b="B",
                 option_c="C", option_d="D")


def test_question_valid_answer_a_through_e():
    for letter in ['A', 'B', 'C', 'D', 'E']:
        q = Question(question_text="Q", option_a="A", option_b="B",
                     option_c="C", option_d="D", correct_answer=letter)
        assert q.correct_answer == letter


def test_question_lowercase_answer_uppercased():
    q = Question(question_text="Q", option_a="A", option_b="B",
                 option_c="C", option_d="D", correct_answer="a")
    assert q.correct_answer == "A"


def test_question_invalid_answer():
    with pytest.raises(ValidationError):
        Question(question_text="Q", option_a="A", option_b="B",
                 option_c="C", option_d="D", correct_answer="Z")


def test_question_none_answer_allowed():
    q = Question(question_text="Q", option_a="A", option_b="B",
                 option_c="C", option_d="D", correct_answer=None)
    assert q.correct_answer is None


def test_question_defaults():
    q = Question(question_text="Q", option_a="A", option_b="B",
                 option_c="C", option_d="D")
    assert q.usage_cycle == 1
    assert q.times_used == 0
    assert q.id is None
    assert q.option_e is None


def test_quiz_session_defaults():
    s = QuizSession(session_id="abc", num_questions=10)
    assert s.num_correct == 0
    assert s.percentage_correct == 0.0
    assert s.started_at is None
    assert s.ended_at is None
    assert s.time_taken_seconds is None


def test_quiz_session_full():
    from datetime import datetime
    s = QuizSession(
        session_id="sess123",
        started_at=datetime(2024, 1, 1),
        ended_at=datetime(2024, 1, 1, 1),
        num_questions=50,
        num_correct=40,
        percentage_correct=80.0,
        time_taken_seconds=3600,
    )
    assert s.num_correct == 40
    assert s.percentage_correct == 80.0


def test_quiz_response_model():
    r = QuizResponse(
        session_id="sess",
        question_id=1,
        user_answer="B",
        is_correct=True,
        time_taken_seconds=45,
    )
    assert r.is_correct is True
    assert r.user_answer == "B"
    assert r.id is None


def test_quiz_response_defaults():
    r = QuizResponse(session_id="s", question_id=1, user_answer="A")
    assert r.is_correct is False
    assert r.time_taken_seconds is None
