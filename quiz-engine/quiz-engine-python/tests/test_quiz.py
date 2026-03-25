import pytest
from quiz_engine.quiz import QuizEngine
from quiz_engine.models import Question


@pytest.fixture
def engine_with_questions(populated_db):
    engine = QuizEngine(populated_db, num_questions=4)
    engine.load_questions()
    return engine


def test_load_questions_returns_correct_count(populated_db):
    engine = QuizEngine(populated_db, num_questions=3)
    engine.load_questions()
    assert len(engine.questions) == 3


def test_load_questions_no_correct_answer(engine_with_questions):
    for q in engine_with_questions.questions:
        assert q.correct_answer is None


def test_correct_answers_stored_internally(engine_with_questions):
    assert len(engine_with_questions._correct_answers) == 4
    for qid, answer in engine_with_questions._correct_answers.items():
        assert answer in ('A', 'B', 'C', 'D', 'E')


def test_submit_correct_answer(engine_with_questions):
    engine = engine_with_questions
    # Find first question and submit its correct answer
    q = engine.questions[0]
    correct_ans = engine._correct_answers[q.id]
    result = engine.submit_answer(0, correct_ans, time_taken=10)
    assert result is True
    assert engine.num_correct == 1


def test_submit_wrong_answer(engine_with_questions):
    engine = engine_with_questions
    q = engine.questions[0]
    correct_ans = engine._correct_answers[q.id]
    wrong_ans = 'B' if correct_ans != 'B' else 'A'
    result = engine.submit_answer(0, wrong_ans, time_taken=5)
    assert result is False
    assert engine.num_correct == 0


def test_submit_answer_case_insensitive(engine_with_questions):
    engine = engine_with_questions
    q = engine.questions[0]
    correct_ans = engine._correct_answers[q.id].lower()
    result = engine.submit_answer(0, correct_ans, time_taken=5)
    assert result is True


def test_finalize_calculates_percentage(engine_with_questions):
    engine = engine_with_questions
    # Submit correct answers for first 2 questions
    for i in range(2):
        q = engine.questions[i]
        correct_ans = engine._correct_answers[q.id]
        engine.submit_answer(i, correct_ans, time_taken=10)

    # Submit wrong answers for remaining
    for i in range(2, 4):
        q = engine.questions[i]
        correct_ans = engine._correct_answers[q.id]
        wrong = 'B' if correct_ans != 'B' else 'A'
        engine.submit_answer(i, wrong, time_taken=10)

    session = engine.finalize()
    assert session.num_correct == 2
    assert session.num_questions == 4
    assert session.percentage_correct == 50.0


def test_finalize_persists_session(engine_with_questions, populated_db):
    engine = engine_with_questions
    for i in range(4):
        q = engine.questions[i]
        correct_ans = engine._correct_answers[q.id]
        engine.submit_answer(i, correct_ans)

    session = engine.finalize()
    retrieved = populated_db.get_session(engine.session_id)
    assert retrieved is not None
    assert retrieved.num_correct == 4


def test_finalize_marks_questions_used(db):
    # Use 5 questions but only load 4, so cycle doesn't advance and times_used stays > 0
    for i in range(5):
        q = Question(question_text=f"Mark Q{i}", option_a="A", option_b="B",
                     option_c="C", option_d="D", correct_answer="A")
        db.insert_question(q)

    engine = QuizEngine(db, num_questions=4)
    engine.load_questions()
    engine.finalize()

    questions = db.get_all_questions()
    used_questions = [q for q in questions if q.times_used > 0]
    assert len(used_questions) == 4


def test_finalize_advances_cycle(engine_with_questions, populated_db):
    engine = engine_with_questions
    engine.finalize()
    # All 4 questions used - cycle should advance
    cycle = populated_db.get_current_cycle()
    assert cycle == 2


def test_get_results(engine_with_questions, populated_db):
    engine = engine_with_questions
    for i in range(4):
        engine.submit_answer(i, 'A', time_taken=5)
    engine.finalize()

    results = engine.get_results()
    assert results is not None
    assert results.session_id == engine.session_id


def test_get_session_review(engine_with_questions):
    engine = engine_with_questions
    # Submit mix of correct and wrong answers
    for i in range(4):
        q = engine.questions[i]
        correct_ans = engine._correct_answers[q.id]
        if i < 2:
            engine.submit_answer(i, correct_ans, time_taken=10)
        else:
            wrong = 'B' if correct_ans != 'B' else 'A'
            engine.submit_answer(i, wrong, time_taken=10)

    engine.finalize()
    review = engine.get_session_review()

    assert 'correct' in review
    assert 'incorrect' in review
    assert len(review['correct']) == 2
    assert len(review['incorrect']) == 2


def test_load_questions_empty_db(db):
    engine = QuizEngine(db, num_questions=5)
    engine.load_questions()
    assert engine.questions == []
    assert engine._correct_answers == {}
