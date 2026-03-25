import pytest
from datetime import datetime
from quiz_engine.database import DatabaseManager
from quiz_engine.models import Question, QuizSession, QuizResponse


def test_init_schema_creates_tables(db):
    tables = db.get_table_names()
    assert 'questions' in tables
    assert 'quiz_sessions' in tables
    assert 'quiz_responses' in tables


def test_insert_and_retrieve_question(db):
    q = Question(
        question_text="What is CI/CD?",
        option_a="Continuous Integration/Delivery",
        option_b="Code Insert/Code Delete",
        option_c="Compile Install/Compile Deploy",
        option_d="None of the above",
        correct_answer="A",
    )
    qid = db.insert_question(q)
    assert qid is not None
    assert qid > 0

    questions = db.get_all_questions()
    assert len(questions) == 1
    assert questions[0].question_text == "What is CI/CD?"
    assert questions[0].correct_answer == "A"


def test_insert_duplicate_question_skipped(db):
    q = Question(
        question_text="Duplicate Q",
        option_a="A", option_b="B", option_c="C", option_d="D",
        correct_answer="A",
    )
    id1 = db.insert_question(q)
    id2 = db.insert_question(q)
    assert id1 is not None
    assert id2 is None
    assert db.count_questions() == 1


def test_get_random_questions_no_correct_answer(populated_db):
    questions = populated_db.get_random_questions(4)
    assert len(questions) == 4
    for q in questions:
        assert q.correct_answer is None
        assert q.explanation is None


def test_get_random_questions_with_difficulty(db):
    q1 = Question(question_text="Easy Q", option_a="A", option_b="B",
                  option_c="C", option_d="D", correct_answer="A", difficulty="easy")
    q2 = Question(question_text="Hard Q", option_a="A", option_b="B",
                  option_c="C", option_d="D", correct_answer="A", difficulty="hard")
    db.insert_question(q1)
    db.insert_question(q2)

    easy_qs = db.get_random_questions(10, difficulty="easy")
    assert len(easy_qs) == 1
    assert easy_qs[0].question_text == "Easy Q"


def test_get_random_questions_with_section(db):
    q1 = Question(question_text="Section 1 Q", option_a="A", option_b="B",
                  option_c="C", option_d="D", correct_answer="A", section="workflows")
    q2 = Question(question_text="Section 2 Q", option_a="A", option_b="B",
                  option_c="C", option_d="D", correct_answer="A", section="security")
    db.insert_question(q1)
    db.insert_question(q2)

    qs = db.get_random_questions(10, section="workflows")
    assert len(qs) == 1


def test_get_questions_with_answers(populated_db):
    all_qs = populated_db.get_all_questions()
    ids = [q.id for q in all_qs[:2]]
    questions = populated_db.get_questions_with_answers(ids)
    assert len(questions) == 2
    for q in questions:
        assert q.correct_answer is not None


def test_get_questions_with_answers_empty(db):
    result = db.get_questions_with_answers([])
    assert result == []


def test_get_current_cycle_empty_db(db):
    cycle = db.get_current_cycle()
    assert cycle == 1


def test_get_current_cycle(populated_db):
    cycle = populated_db.get_current_cycle()
    assert cycle == 1


def test_mark_question_used(populated_db):
    questions = populated_db.get_all_questions()
    qid = questions[0].id
    populated_db.mark_question_used(qid)
    updated = populated_db.get_all_questions()
    q = next(q for q in updated if q.id == qid)
    assert q.times_used == 1


def test_advance_cycle_if_exhausted(db):
    q = Question(question_text="Cycle Q", option_a="A", option_b="B",
                 option_c="C", option_d="D", correct_answer="A")
    qid = db.insert_question(q)

    # Not exhausted yet - shouldn't advance
    db.advance_cycle_if_exhausted()
    assert db.get_current_cycle() == 1

    # Mark all used
    db.mark_question_used(qid)
    db.advance_cycle_if_exhausted()
    assert db.get_current_cycle() == 2


def test_advance_cycle_multiple_questions(populated_db):
    all_qs = populated_db.get_all_questions()
    # Mark all but one
    for q in all_qs[:-1]:
        populated_db.mark_question_used(q.id)

    populated_db.advance_cycle_if_exhausted()
    # Should NOT advance - one question still unused
    assert populated_db.get_current_cycle() == 1

    # Mark the last one
    populated_db.mark_question_used(all_qs[-1].id)
    populated_db.advance_cycle_if_exhausted()
    assert populated_db.get_current_cycle() == 2


def test_count_questions(populated_db):
    assert populated_db.count_questions() == 4


def test_delete_all_questions(populated_db):
    populated_db.delete_all_questions()
    assert populated_db.count_questions() == 0


def test_session_crud(db):
    session = QuizSession(
        session_id="test-session-1",
        started_at=datetime(2024, 1, 1, 10, 0, 0),
        ended_at=datetime(2024, 1, 1, 10, 30, 0),
        num_questions=20,
        num_correct=15,
        percentage_correct=75.0,
        time_taken_seconds=1800,
    )
    db.insert_session(session)

    retrieved = db.get_session("test-session-1")
    assert retrieved is not None
    assert retrieved.session_id == "test-session-1"
    assert retrieved.num_correct == 15
    assert retrieved.percentage_correct == 75.0


def test_update_session(db):
    session = QuizSession(
        session_id="test-session-2",
        num_questions=10,
        num_correct=5,
        percentage_correct=50.0,
    )
    db.insert_session(session)

    session.num_correct = 8
    session.percentage_correct = 80.0
    db.update_session(session)

    retrieved = db.get_session("test-session-2")
    assert retrieved.num_correct == 8
    assert retrieved.percentage_correct == 80.0


def test_get_all_sessions(db):
    for i in range(3):
        s = QuizSession(session_id=f"session-{i}", num_questions=10)
        db.insert_session(s)

    sessions = db.get_all_sessions()
    assert len(sessions) == 3


def test_response_crud(db):
    session = QuizSession(session_id="test-resp-session", num_questions=5)
    db.insert_session(session)

    q = Question(question_text="R Q", option_a="A", option_b="B",
                 option_c="C", option_d="D", correct_answer="A")
    qid = db.insert_question(q)

    response = QuizResponse(
        session_id="test-resp-session",
        question_id=qid,
        user_answer="A",
        is_correct=True,
        time_taken_seconds=30,
    )
    db.insert_response(response)

    responses = db.get_responses_for_session("test-resp-session")
    assert len(responses) == 1
    assert responses[0].user_answer == "A"
    assert responses[0].is_correct is True


def test_delete_session(db):
    s = QuizSession(session_id="del-session", num_questions=5)
    db.insert_session(s)

    q = Question(question_text="Del Q", option_a="A", option_b="B",
                 option_c="C", option_d="D", correct_answer="A")
    qid = db.insert_question(q)

    r = QuizResponse(session_id="del-session", question_id=qid,
                     user_answer="A", is_correct=True)
    db.insert_response(r)

    db.delete_session("del-session")
    assert db.get_session("del-session") is None
    assert db.get_responses_for_session("del-session") == []


def test_delete_all_sessions(db):
    for i in range(3):
        s = QuizSession(session_id=f"sess-{i}", num_questions=5)
        db.insert_session(s)

    db.delete_all_sessions()
    assert db.get_all_sessions() == []


def test_delete_sessions_before(db):
    s1 = QuizSession(
        session_id="old-session",
        started_at=datetime(2020, 1, 1),
        num_questions=5,
    )
    s2 = QuizSession(
        session_id="new-session",
        started_at=datetime(2024, 1, 1),
        num_questions=5,
    )
    db.insert_session(s1)
    db.insert_session(s2)

    db.delete_sessions_before(datetime(2023, 1, 1))
    sessions = db.get_all_sessions()
    assert len(sessions) == 1
    assert sessions[0].session_id == "new-session"


def test_get_session_nonexistent(db):
    result = db.get_session("nonexistent")
    assert result is None


def test_insert_question_with_all_fields(db):
    q = Question(
        question_text="Full Q",
        option_a="A", option_b="B", option_c="C", option_d="D",
        option_e="E",
        correct_answer="E",
        explanation="Because E",
        section="testing",
        difficulty="hard",
        source_file="test.md",
    )
    qid = db.insert_question(q)
    assert qid is not None

    questions = db.get_all_questions()
    assert questions[0].option_e == "E"
    assert questions[0].correct_answer == "E"
    assert questions[0].explanation == "Because E"
