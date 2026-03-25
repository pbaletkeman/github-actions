"""Entry point for the quiz engine."""  # pragma: no cover
import argparse
import sys
import os

# Ensure quiz_engine is importable
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from quiz_engine.database import DatabaseManager
from quiz_engine.quiz import QuizEngine
from quiz_engine import cli


def main():  # pragma: no cover
    parser = argparse.ArgumentParser(description="GitHub Actions Quiz Engine")
    parser.add_argument('--db', default='quiz_engine/quiz.db', help='Database path')
    parser.add_argument('--questions', type=int, default=20, help='Number of questions')
    parser.add_argument('--difficulty', help='Filter by difficulty')
    parser.add_argument('--section', help='Filter by section')
    args = parser.parse_args()

    db = DatabaseManager(args.db)
    db.init_schema()

    count = db.count_questions()
    if count == 0:
        cli.console.print("[red]No questions in database. Run scripts/import_questions.py first.[/red]")
        db.close()
        sys.exit(1)

    engine = QuizEngine(db, args.questions, args.difficulty, args.section)
    engine.load_questions()

    if not engine.questions:
        cli.console.print("[red]No questions available for quiz.[/red]")
        db.close()
        sys.exit(1)

    total = len(engine.questions)
    for idx, question in enumerate(engine.questions):
        cli.display_question(question, idx + 1, total)
        valid = ['A', 'B', 'C', 'D']
        if question.option_e:
            valid.append('E')
        answer = cli.get_user_answer(valid)
        if answer == 'Q':
            cli.console.print("[yellow]Quiz aborted.[/yellow]")
            break
        engine.submit_answer(idx, answer, time_taken=0)

    session = engine.finalize()
    cli.display_session_summary(session)
    db.close()


if __name__ == "__main__":
    main()
