"""Entry point for the quiz engine."""  # pragma: no cover
import argparse
import sys
import os

# Ensure quiz_engine is importable
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from quiz_engine.database import DatabaseManager
from quiz_engine.quiz import QuizEngine
from quiz_engine import cli


def run_quiz(db, args):  # pragma: no cover
    """Run the quiz with specified arguments."""
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
    review_items = []
    for idx, question in enumerate(engine.questions):
        cli.display_question(question, idx + 1, total)
        valid = ['A', 'B', 'C', 'D']
        if question.option_e:
            valid.append('E')
        answer = cli.get_user_answer(valid)
        if answer == 'Q':
            cli.console.print("[yellow]Quiz aborted.[/yellow]")
            break
        is_correct = engine.submit_answer(idx, answer, time_taken=0)
        correct_answer = engine._correct_answers.get(question.id, '?')
        review_items.append((idx + 1, question, answer, correct_answer, is_correct))

    if review_items:
        cli.display_review(review_items)

    session = engine.finalize()
    cli.display_session_summary(session)
    db.close()


def run_clear(db, args):  # pragma: no cover
    """Handle the clear command."""
    if args.all:
        if not args.confirm:
            cli.console.print("[yellow]WARNING: This will delete all questions and sessions from the database.[/yellow]")
            response = cli.console.input("[bold yellow]Are you sure? Type 'yes' to confirm: [/bold yellow]").strip()
            if response.lower() != 'yes':
                cli.console.print("[cyan]Operation cancelled.[/cyan]")
                db.close()
                sys.exit(0)

        # Clear all data
        db.delete_all_sessions()
        db.delete_all_questions()
        cli.console.print("[green]✓ All questions and sessions have been cleared.[/green]")
    else:
        cli.console.print("[yellow]Please specify what to clear (e.g., --all)[/yellow]")

    db.close()


def main():  # pragma: no cover
    # Check if the first argument is a recognized command
    # If not, assume it's the old format and prepend 'quiz'
    if len(sys.argv) > 1 and sys.argv[1] not in ['quiz', 'clear', '-h', '--help']:
        # Check if it looks like a quiz argument (flag or number)
        if sys.argv[1].startswith('-') or sys.argv[1].isdigit():
            sys.argv.insert(1, 'quiz')

    parser = argparse.ArgumentParser(description="GitHub Actions Quiz Engine")
    subparsers = parser.add_subparsers(dest='command', help='Available commands')

    # Quiz command (default)
    quiz_parser = subparsers.add_parser('quiz', help='Run the quiz')
    quiz_parser.add_argument('--db', default='quiz_engine/quiz.db', help='Database path')
    quiz_parser.add_argument('--questions', type=int, default=20, help='Number of questions')
    quiz_parser.add_argument('--difficulty', help='Filter by difficulty')
    quiz_parser.add_argument('--section', help='Filter by section')
    quiz_parser.set_defaults(func=run_quiz)

    # Clear command
    clear_parser = subparsers.add_parser('clear', help='Clear data from the database')
    clear_parser.add_argument('--all', action='store_true', help='Clear all questions and sessions')
    clear_parser.add_argument('--confirm', action='store_true', help='Skip confirmation prompt')
    clear_parser.add_argument('--db', default='quiz_engine/quiz.db', help='Database path')
    clear_parser.set_defaults(func=run_clear)

    args = parser.parse_args()

    db = DatabaseManager(args.db)
    db.init_schema()
    if hasattr(args, 'func'):
        args.func(db, args)
    else:
        parser.print_help()
        sys.exit(1)


if __name__ == "__main__":
    main()
