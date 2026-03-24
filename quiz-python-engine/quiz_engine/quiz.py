from typing import List, Optional
import uuid
from datetime import datetime
from .database import DatabaseManager
from .models import Question, QuizSession, QuizResponse
from .utils import calculate_score


class QuizEngine:
    def __init__(self, db: DatabaseManager, num_questions: int = 100,
                 difficulty: str = None, section: str = None):
        self.db = db
        self.num_questions = num_questions
        self.difficulty = difficulty
        self.section = section
        self.session_id = str(uuid.uuid4())
        self.questions: List[Question] = []
        self._correct_answers: dict = {}  # question_id -> correct_answer (private)
        self.responses: List[QuizResponse] = []
        self.num_correct = 0
        self.started_at = datetime.utcnow()

    def load_questions(self):
        # Load display questions (no correct_answer)
        self.questions = self.db.get_random_questions(
            self.num_questions, self.difficulty, self.section
        )
        # Separately fetch correct answers for internal checking
        question_ids = [q.id for q in self.questions if q.id is not None]
        if question_ids:
            questions_with_answers = self.db.get_questions_with_answers(question_ids)
            self._correct_answers = {q.id: q.correct_answer for q in questions_with_answers}

    def submit_answer(self, question_idx: int, user_answer: str, time_taken: int = 0):
        question = self.questions[question_idx]
        correct = self._correct_answers.get(question.id)
        is_correct = (user_answer.upper() == correct) if correct else False

        if is_correct:
            self.num_correct += 1

        response = QuizResponse(
            session_id=self.session_id,
            question_id=question.id,
            user_answer=user_answer.upper(),
            is_correct=is_correct,
            time_taken_seconds=time_taken,
        )
        self.responses.append(response)
        self.db.insert_response(response)
        return is_correct

    def finalize(self) -> QuizSession:
        ended_at = datetime.utcnow()
        time_taken = int((ended_at - self.started_at).total_seconds())
        percentage = calculate_score(self.num_correct, len(self.questions))

        session = QuizSession(
            session_id=self.session_id,
            started_at=self.started_at,
            ended_at=ended_at,
            num_questions=len(self.questions),
            num_correct=self.num_correct,
            percentage_correct=percentage,
            time_taken_seconds=time_taken,
        )
        self.db.insert_session(session)

        # Mark questions as used
        for q in self.questions:
            if q.id is not None:
                self.db.mark_question_used(q.id)

        # Auto-advance cycle if exhausted
        self.db.advance_cycle_if_exhausted()

        return session

    def get_results(self) -> QuizSession:
        session = self.db.get_session(self.session_id)
        return session

    def get_session_review(self) -> dict:
        question_ids = [q.id for q in self.questions if q.id is not None]
        questions_with_answers = self.db.get_questions_with_answers(question_ids)
        q_map = {q.id: q for q in questions_with_answers}

        incorrect = []
        correct = []

        for response in self.responses:
            q = q_map.get(response.question_id)
            if q:
                item = {
                    'question': q,
                    'response': response,
                }
                if response.is_correct:
                    correct.append(item)
                else:
                    incorrect.append(item)

        return {'incorrect': incorrect, 'correct': correct}
