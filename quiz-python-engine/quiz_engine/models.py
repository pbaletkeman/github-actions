from pydantic import BaseModel, field_validator
from typing import Optional
from datetime import datetime

class Question(BaseModel):
    id: Optional[int] = None
    question_text: str
    option_a: str
    option_b: str
    option_c: str
    option_d: str
    option_e: Optional[str] = None
    correct_answer: Optional[str] = None  # None when loaded for quiz display
    explanation: Optional[str] = None
    section: Optional[str] = None
    difficulty: Optional[str] = None
    source_file: Optional[str] = None
    usage_cycle: int = 1
    times_used: int = 0

    @field_validator('question_text')
    @classmethod
    def question_text_not_empty(cls, v):
        if not v or not v.strip():
            raise ValueError('question_text cannot be empty')
        return v

    @field_validator('correct_answer')
    @classmethod
    def valid_answer_letter(cls, v):
        if v is not None and v.upper() not in ('A', 'B', 'C', 'D', 'E'):
            raise ValueError('correct_answer must be A-E')
        return v.upper() if v else v

class QuizSession(BaseModel):
    session_id: str
    started_at: Optional[datetime] = None
    ended_at: Optional[datetime] = None
    num_questions: int
    num_correct: int = 0
    percentage_correct: float = 0.0
    time_taken_seconds: Optional[int] = None

class QuizResponse(BaseModel):
    id: Optional[int] = None
    session_id: str
    question_id: int
    user_answer: str
    is_correct: bool = False
    time_taken_seconds: Optional[int] = None
