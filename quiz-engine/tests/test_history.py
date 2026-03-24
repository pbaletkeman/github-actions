import pytest
import os
import json
import csv
from datetime import datetime
from quiz_engine.history import HistoryManager
from quiz_engine.models import QuizSession, QuizResponse, Question


@pytest.fixture
def sessions():
    return [
        QuizSession(
            session_id="hist-1",
            started_at=datetime(2024, 1, 1, 10, 0),
            ended_at=datetime(2024, 1, 1, 10, 30),
            num_questions=20,
            num_correct=15,
            percentage_correct=75.0,
            time_taken_seconds=1800,
        ),
        QuizSession(
            session_id="hist-2",
            started_at=datetime(2024, 1, 2, 14, 0),
            ended_at=datetime(2024, 1, 2, 14, 30),
            num_questions=10,
            num_correct=9,
            percentage_correct=90.0,
            time_taken_seconds=900,
        ),
    ]


@pytest.fixture
def populated_history_db(db, sessions):
    for s in sessions:
        db.insert_session(s)
    return db


def test_get_all_sessions(populated_history_db, sessions):
    history = HistoryManager(populated_history_db)
    result = history.get_all_sessions()
    assert len(result) == 2


def test_get_session_details(populated_history_db):
    q = Question(question_text="History Q", option_a="A", option_b="B",
                 option_c="C", option_d="D", correct_answer="A")
    qid = populated_history_db.insert_question(q)

    r = QuizResponse(session_id="hist-1", question_id=qid,
                     user_answer="A", is_correct=True)
    populated_history_db.insert_response(r)

    history = HistoryManager(populated_history_db)
    details = history.get_session_details("hist-1")

    assert details is not None
    assert details['session'].session_id == "hist-1"
    assert len(details['responses']) == 1
    assert len(details['questions']) == 1


def test_get_session_details_not_found(populated_history_db):
    history = HistoryManager(populated_history_db)
    result = history.get_session_details("nonexistent")
    assert result is None


def test_format_session_summary(populated_history_db, sessions):
    history = HistoryManager(populated_history_db)
    summary = history.format_session_summary(sessions[0])
    assert "hist-1" in summary
    assert "15" in summary
    assert "75.0" in summary


def test_export_to_csv(populated_history_db, sessions, tmp_path):
    history = HistoryManager(populated_history_db)
    csv_path = str(tmp_path / "history.csv")
    history.export_to_csv(sessions, csv_path)

    assert os.path.exists(csv_path)
    with open(csv_path) as f:
        reader = csv.DictReader(f)
        rows = list(reader)
    assert len(rows) == 2
    assert rows[0]['session_id'] == 'hist-1'


def test_export_to_json(populated_history_db, sessions, tmp_path):
    history = HistoryManager(populated_history_db)
    json_path = str(tmp_path / "history.json")
    history.export_to_json(sessions, json_path)

    assert os.path.exists(json_path)
    with open(json_path) as f:
        data = json.load(f)
    assert len(data) == 2
    assert data[0]['session_id'] == 'hist-1'


def test_export_to_json_with_answers(populated_history_db, sessions, tmp_path):
    q = Question(question_text="Export Q", option_a="A", option_b="B",
                 option_c="C", option_d="D", correct_answer="A")
    qid = populated_history_db.insert_question(q)

    r = QuizResponse(session_id="hist-1", question_id=qid,
                     user_answer="A", is_correct=True)
    populated_history_db.insert_response(r)

    history = HistoryManager(populated_history_db)
    json_path = str(tmp_path / "history_answers.json")
    history.export_to_json(sessions, json_path, include_answers=True)

    with open(json_path) as f:
        data = json.load(f)
    assert 'responses' in data[0]


def test_format_session_summary_no_dates(db):
    s = QuizSession(session_id="no-date", num_questions=5, num_correct=3, percentage_correct=60.0)
    db.insert_session(s)
    history = HistoryManager(db)
    summary = history.format_session_summary(s)
    assert "N/A" in summary
