import sqlite3
from typing import Optional, List
from datetime import datetime
from .models import Question, QuizSession, QuizResponse


class DatabaseManager:
    def __init__(self, db_path: str = "quiz_engine/quiz.db"):
        self.db_path = db_path
        self.conn = None
        self._connect()

    def _connect(self):
        self.conn = sqlite3.connect(self.db_path, check_same_thread=False)
        self.conn.row_factory = sqlite3.Row

    def init_schema(self):
        cursor = self.conn.cursor()
        cursor.executescript("""
            CREATE TABLE IF NOT EXISTS questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                question_text TEXT NOT NULL,
                option_a TEXT NOT NULL,
                option_b TEXT NOT NULL,
                option_c TEXT NOT NULL,
                option_d TEXT NOT NULL,
                option_e TEXT,
                correct_answer TEXT,
                explanation TEXT,
                section TEXT,
                difficulty TEXT,
                source_file TEXT,
                usage_cycle INTEGER DEFAULT 1,
                times_used INTEGER DEFAULT 0,
                last_used_at TEXT,
                UNIQUE(question_text, option_a, option_b, option_c, option_d)
            );
            CREATE INDEX IF NOT EXISTS idx_questions_cycle ON questions(usage_cycle);
            CREATE INDEX IF NOT EXISTS idx_questions_difficulty ON questions(difficulty);
            CREATE INDEX IF NOT EXISTS idx_questions_section ON questions(section);

            CREATE TABLE IF NOT EXISTS quiz_sessions (
                session_id TEXT PRIMARY KEY,
                started_at TEXT,
                ended_at TEXT,
                num_questions INTEGER NOT NULL,
                num_correct INTEGER DEFAULT 0,
                percentage_correct REAL DEFAULT 0.0,
                time_taken_seconds INTEGER
            );
            CREATE INDEX IF NOT EXISTS idx_sessions_started ON quiz_sessions(started_at);

            CREATE TABLE IF NOT EXISTS quiz_responses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                session_id TEXT NOT NULL,
                question_id INTEGER NOT NULL,
                user_answer TEXT NOT NULL,
                is_correct INTEGER DEFAULT 0,
                time_taken_seconds INTEGER,
                FOREIGN KEY (session_id) REFERENCES quiz_sessions(session_id),
                FOREIGN KEY (question_id) REFERENCES questions(id)
            );
            CREATE INDEX IF NOT EXISTS idx_responses_session ON quiz_responses(session_id);
        """)
        self.conn.commit()

    def get_table_names(self) -> List[str]:
        cursor = self.conn.cursor()
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name")
        return [row[0] for row in cursor.fetchall()]

    def insert_question(self, q: Question) -> Optional[int]:
        cursor = self.conn.cursor()
        try:
            cursor.execute("""
                INSERT OR IGNORE INTO questions
                (question_text, option_a, option_b, option_c, option_d, option_e,
                 correct_answer, explanation, section, difficulty, source_file,
                 usage_cycle, times_used)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                q.question_text, q.option_a, q.option_b, q.option_c, q.option_d,
                q.option_e, q.correct_answer, q.explanation, q.section,
                q.difficulty, q.source_file, q.usage_cycle, q.times_used
            ))
            self.conn.commit()
            if cursor.rowcount == 0:
                return None
            return cursor.lastrowid
        except Exception:
            self.conn.rollback()
            raise

    def get_all_questions(self) -> List[Question]:
        cursor = self.conn.cursor()
        cursor.execute("SELECT * FROM questions ORDER BY id")
        rows = cursor.fetchall()
        return [self._row_to_question(row) for row in rows]

    def get_random_questions(self, n: int, difficulty=None, section=None) -> List[Question]:
        current_cycle = self.get_current_cycle()
        params = [current_cycle]
        filters = ["usage_cycle = ?"]
        if difficulty:
            filters.append("difficulty = ?")
            params.append(difficulty)
        if section:
            filters.append("section = ?")
            params.append(section)
        where = " AND ".join(filters)
        params.append(n)
        cursor = self.conn.cursor()
        cursor.execute(f"""
            SELECT id, question_text, option_a, option_b, option_c, option_d, option_e,
                   section, difficulty, source_file, usage_cycle, times_used
            FROM questions
            WHERE {where}
            ORDER BY RANDOM()
            LIMIT ?
        """, params)
        rows = cursor.fetchall()
        questions = []
        for row in rows:
            q = Question(
                id=row["id"],
                question_text=row["question_text"],
                option_a=row["option_a"],
                option_b=row["option_b"],
                option_c=row["option_c"],
                option_d=row["option_d"],
                option_e=row["option_e"],
                correct_answer=None,  # explicitly hidden
                explanation=None,
                section=row["section"],
                difficulty=row["difficulty"],
                source_file=row["source_file"],
                usage_cycle=row["usage_cycle"],
                times_used=row["times_used"],
            )
            questions.append(q)
        return questions

    def get_questions_with_answers(self, question_ids: List[int]) -> List[Question]:
        if not question_ids:
            return []
        placeholders = ",".join("?" * len(question_ids))
        cursor = self.conn.cursor()
        cursor.execute(f"SELECT * FROM questions WHERE id IN ({placeholders})", question_ids)
        rows = cursor.fetchall()
        row_map = {row["id"]: row for row in rows}
        return [self._row_to_question(row_map[qid]) for qid in question_ids if qid in row_map]

    def get_current_cycle(self) -> int:
        cursor = self.conn.cursor()
        cursor.execute("SELECT MIN(usage_cycle) FROM questions")
        row = cursor.fetchone()
        if row and row[0] is not None:
            return row[0]
        return 1

    def mark_question_used(self, question_id: int):
        cursor = self.conn.cursor()
        cursor.execute("""
            UPDATE questions
            SET times_used = times_used + 1, last_used_at = ?
            WHERE id = ?
        """, (datetime.utcnow().isoformat(), question_id))
        self.conn.commit()

    def advance_cycle_if_exhausted(self):
        current_cycle = self.get_current_cycle()
        cursor = self.conn.cursor()
        # Check if all questions at current cycle have been used at least once
        cursor.execute("""
            SELECT COUNT(*) FROM questions
            WHERE usage_cycle = ? AND times_used = 0
        """, (current_cycle,))
        unused_count = cursor.fetchone()[0]
        if unused_count == 0:
            # All questions used - advance their cycle
            cursor.execute("""
                UPDATE questions
                SET usage_cycle = usage_cycle + 1, times_used = 0
                WHERE usage_cycle = ?
            """, (current_cycle,))
            self.conn.commit()

    def count_questions(self) -> int:
        cursor = self.conn.cursor()
        cursor.execute("SELECT COUNT(*) FROM questions")
        return cursor.fetchone()[0]

    def delete_all_questions(self):
        cursor = self.conn.cursor()
        cursor.execute("DELETE FROM questions")
        self.conn.commit()

    def insert_session(self, session: QuizSession):
        cursor = self.conn.cursor()
        cursor.execute("""
            INSERT INTO quiz_sessions
            (session_id, started_at, ended_at, num_questions, num_correct,
             percentage_correct, time_taken_seconds)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """, (
            session.session_id,
            session.started_at.isoformat() if session.started_at else None,
            session.ended_at.isoformat() if session.ended_at else None,
            session.num_questions,
            session.num_correct,
            session.percentage_correct,
            session.time_taken_seconds,
        ))
        self.conn.commit()

    def update_session(self, session: QuizSession):
        cursor = self.conn.cursor()
        cursor.execute("""
            UPDATE quiz_sessions
            SET started_at=?, ended_at=?, num_questions=?, num_correct=?,
                percentage_correct=?, time_taken_seconds=?
            WHERE session_id=?
        """, (
            session.started_at.isoformat() if session.started_at else None,
            session.ended_at.isoformat() if session.ended_at else None,
            session.num_questions,
            session.num_correct,
            session.percentage_correct,
            session.time_taken_seconds,
            session.session_id,
        ))
        self.conn.commit()

    def get_session(self, session_id: str) -> Optional[QuizSession]:
        cursor = self.conn.cursor()
        cursor.execute("SELECT * FROM quiz_sessions WHERE session_id=?", (session_id,))
        row = cursor.fetchone()
        if row:
            return self._row_to_session(row)
        return None

    def get_all_sessions(self) -> List[QuizSession]:
        cursor = self.conn.cursor()
        cursor.execute("SELECT * FROM quiz_sessions ORDER BY started_at DESC")
        return [self._row_to_session(row) for row in cursor.fetchall()]

    def insert_response(self, response: QuizResponse):
        cursor = self.conn.cursor()
        cursor.execute("""
            INSERT INTO quiz_responses
            (session_id, question_id, user_answer, is_correct, time_taken_seconds)
            VALUES (?, ?, ?, ?, ?)
        """, (
            response.session_id, response.question_id, response.user_answer,
            1 if response.is_correct else 0, response.time_taken_seconds,
        ))
        self.conn.commit()

    def get_responses_for_session(self, session_id: str) -> List[QuizResponse]:
        cursor = self.conn.cursor()
        cursor.execute("""
            SELECT * FROM quiz_responses WHERE session_id=? ORDER BY id
        """, (session_id,))
        return [self._row_to_response(row) for row in cursor.fetchall()]

    def delete_session(self, session_id: str):
        cursor = self.conn.cursor()
        cursor.execute("DELETE FROM quiz_responses WHERE session_id=?", (session_id,))
        cursor.execute("DELETE FROM quiz_sessions WHERE session_id=?", (session_id,))
        self.conn.commit()

    def delete_all_sessions(self):
        cursor = self.conn.cursor()
        cursor.execute("DELETE FROM quiz_responses")
        cursor.execute("DELETE FROM quiz_sessions")
        self.conn.commit()

    def delete_sessions_before(self, date: datetime):
        cursor = self.conn.cursor()
        date_str = date.isoformat()
        cursor.execute("""
            DELETE FROM quiz_responses WHERE session_id IN (
                SELECT session_id FROM quiz_sessions WHERE started_at < ?
            )
        """, (date_str,))
        cursor.execute("DELETE FROM quiz_sessions WHERE started_at < ?", (date_str,))
        self.conn.commit()

    def close(self):
        if self.conn:
            self.conn.close()

    def _row_to_question(self, row) -> Question:
        return Question(
            id=row["id"],
            question_text=row["question_text"],
            option_a=row["option_a"],
            option_b=row["option_b"],
            option_c=row["option_c"],
            option_d=row["option_d"],
            option_e=row["option_e"],
            correct_answer=row["correct_answer"],
            explanation=row["explanation"],
            section=row["section"],
            difficulty=row["difficulty"],
            source_file=row["source_file"],
            usage_cycle=row["usage_cycle"],
            times_used=row["times_used"],
        )

    def _row_to_session(self, row) -> QuizSession:
        return QuizSession(
            session_id=row["session_id"],
            started_at=datetime.fromisoformat(row["started_at"]) if row["started_at"] else None,
            ended_at=datetime.fromisoformat(row["ended_at"]) if row["ended_at"] else None,
            num_questions=row["num_questions"],
            num_correct=row["num_correct"],
            percentage_correct=row["percentage_correct"],
            time_taken_seconds=row["time_taken_seconds"],
        )

    def _row_to_response(self, row) -> QuizResponse:
        return QuizResponse(
            id=row["id"],
            session_id=row["session_id"],
            question_id=row["question_id"],
            user_answer=row["user_answer"],
            is_correct=bool(row["is_correct"]),
            time_taken_seconds=row["time_taken_seconds"],
        )
