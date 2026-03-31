"""CLI display and input functions using Rich. Excluded from coverage."""
from rich.console import Console
from rich.table import Table
from rich.panel import Panel
from typing import List, Optional
from .models import Question

console = Console()


def display_question(question: Question, question_num: int, total: int):  # pragma: no cover
    options = [
        ('A', question.option_a),
        ('B', question.option_b),
        ('C', question.option_c),
        ('D', question.option_d),
    ]
    if question.option_e:
        options.append(('E', question.option_e))

    console.print(f"\n[bold cyan]Question {question_num}/{total}[/bold cyan]")
    console.print(Panel(question.question_text, expand=False))
    for label, text in options:
        console.print(f"  [bold]{label})[/bold] {text}")


def get_user_answer(valid_options: List[str] = None) -> str:  # pragma: no cover
    if valid_options is None:
        valid_options = ['A', 'B', 'C', 'D', 'E']
    while True:
        answer = console.input("\n[bold yellow]Your answer (A/B/C/D/E or Q to quit): [/bold yellow]").strip().upper()
        if answer == 'Q':
            return 'Q'
        if answer in valid_options:
            return answer
        console.print("[red]Invalid option. Please try again.[/red]")


def display_result(is_correct: bool, correct_answer: str, explanation: Optional[str] = None):  # pragma: no cover
    if is_correct:
        console.print("[bold green]✓ Correct![/bold green]")
    else:
        console.print(f"[bold red]✗ Incorrect. The correct answer is {correct_answer}.[/bold red]")
    if explanation:
        console.print(f"[dim]{explanation}[/dim]")


def display_review(items) -> None:  # pragma: no cover
    console.print("\n[bold cyan]=== Answer Review ===[/bold cyan]")
    for question_num, question, user_answer, correct_answer, is_correct in items:
        if is_correct:
            status = "[bold green]✓ CORRECT[/bold green]"
        else:
            status = f"[bold red]✗ WRONG — Correct answer: {correct_answer}[/bold red]"
        short_text = question.question_text[:80] + ('...' if len(question.question_text) > 80 else '')
        console.print(f"\n[bold]Q{question_num}:[/bold] {short_text}")
        console.print(f"  Your answer: [bold]{user_answer}[/bold]  {status}")
        if not is_correct and question.explanation:
            console.print(f"  [dim]{question.explanation}[/dim]")


def display_session_summary(session):  # pragma: no cover
    console.print("\n")
    table = Table(title="Quiz Results")
    table.add_column("Metric", style="cyan")
    table.add_column("Value", style="white")
    table.add_row("Questions", str(session.num_questions))
    table.add_row("Correct", str(session.num_correct))
    table.add_row("Score", f"{session.percentage_correct:.1f}%")
    if session.time_taken_seconds:
        from .utils import format_time
        table.add_row("Time", format_time(session.time_taken_seconds))
    console.print(table)


def display_history_table(sessions):  # pragma: no cover
    if not sessions:
        console.print("[yellow]No quiz history found.[/yellow]")
        return
    table = Table(title="Quiz History")
    table.add_column("Session ID", style="cyan")
    table.add_column("Date", style="white")
    table.add_column("Questions", style="white")
    table.add_column("Correct", style="white")
    table.add_column("Score %", style="white")
    for s in sessions:
        date_str = s.started_at.strftime("%Y-%m-%d %H:%M") if s.started_at else "N/A"
        table.add_row(
            s.session_id,
            date_str,
            str(s.num_questions),
            str(s.num_correct),
            f"{s.percentage_correct:.1f}%",
        )
    console.print(table)
