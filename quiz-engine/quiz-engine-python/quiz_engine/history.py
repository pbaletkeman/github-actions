import csv
import json
from typing import List, Optional
from datetime import datetime
from .database import DatabaseManager
from .models import QuizSession, QuizResponse, Question


class HistoryManager:
    def __init__(self, db: DatabaseManager):
        self.db = db

    def get_all_sessions(self) -> List[QuizSession]:
        return self.db.get_all_sessions()

    def get_session_details(self, session_id: str):
        session = self.db.get_session(session_id)
        if not session:
            return None
        responses = self.db.get_responses_for_session(session_id)
        question_ids = [r.question_id for r in responses]
        questions = self.db.get_questions_with_answers(question_ids)
        return {
            'session': session,
            'responses': responses,
            'questions': questions,
        }

    def format_session_summary(self, session: QuizSession) -> str:
        started = session.started_at.strftime("%Y-%m-%d %H:%M") if session.started_at else "N/A"
        time_str = f"{session.time_taken_seconds}s" if session.time_taken_seconds else "N/A"
        return (
            f"Session {session.session_id} | {started} | "
            f"{session.num_correct}/{session.num_questions} correct | "
            f"{session.percentage_correct:.1f}% | {time_str}"
        )

    def export_to_csv(self, sessions: List[QuizSession], file_path: str, include_answers: bool = False):
        fieldnames = ['session_id', 'started_at', 'ended_at', 'num_questions',
                      'num_correct', 'percentage_correct', 'time_taken_seconds']
        with open(file_path, 'w', newline='', encoding='utf-8') as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            for session in sessions:
                writer.writerow({
                    'session_id': session.session_id,
                    'started_at': session.started_at.isoformat() if session.started_at else '',
                    'ended_at': session.ended_at.isoformat() if session.ended_at else '',
                    'num_questions': session.num_questions,
                    'num_correct': session.num_correct,
                    'percentage_correct': session.percentage_correct,
                    'time_taken_seconds': session.time_taken_seconds or '',
                })

    def export_to_json(self, sessions: List[QuizSession], file_path: str, include_answers: bool = False):
        data = []
        for session in sessions:
            entry = {
                'session_id': session.session_id,
                'started_at': session.started_at.isoformat() if session.started_at else None,
                'ended_at': session.ended_at.isoformat() if session.ended_at else None,
                'num_questions': session.num_questions,
                'num_correct': session.num_correct,
                'percentage_correct': session.percentage_correct,
                'time_taken_seconds': session.time_taken_seconds,
            }
            if include_answers:
                details = self.get_session_details(session.session_id)
                if details:
                    entry['responses'] = [
                        {
                            'question_id': r.question_id,
                            'user_answer': r.user_answer,
                            'is_correct': r.is_correct,
                        }
                        for r in details['responses']
                    ]
            data.append(entry)
        with open(file_path, 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2)
