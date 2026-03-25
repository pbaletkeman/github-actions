import argparse
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from quiz_engine.database import DatabaseManager
from quiz_engine.history import HistoryManager


def main():
    parser = argparse.ArgumentParser(description="View quiz history")
    parser.add_argument('--db', default='quiz_engine/quiz.db', help='Database path')
    parser.add_argument('--session-id', help='Show details for specific session')
    parser.add_argument('--summary', action='store_true', help='Show summary lines')
    parser.add_argument('--review', action='store_true', help='Show full review')
    parser.add_argument('--export', choices=['csv', 'json'], help='Export format')
    parser.add_argument('--output', help='Output file path for export')
    parser.add_argument('--include-answers', action='store_true', help='Include answers in export')
    parser.add_argument('--start-date', help='Filter sessions after this date (YYYY-MM-DD)')
    parser.add_argument('--end-date', help='Filter sessions before this date (YYYY-MM-DD)')
    args = parser.parse_args()

    db = DatabaseManager(args.db)
    db.init_schema()
    history = HistoryManager(db)

    if args.session_id:
        details = history.get_session_details(args.session_id)
        if not details:
            print(f"Session {args.session_id} not found")
            sys.exit(1)
        print(history.format_session_summary(details['session']))
        if args.review:
            for r in details['responses']:
                print(f"  Q{r.question_id}: {r.user_answer} | {'✓' if r.is_correct else '✗'}")
    else:
        sessions = history.get_all_sessions()
        if args.export == 'csv':
            out_path = args.output or 'history.csv'
            history.export_to_csv(sessions, out_path, args.include_answers)
            print(f"Exported to {out_path}")
        elif args.export == 'json':
            out_path = args.output or 'history.json'
            history.export_to_json(sessions, out_path, args.include_answers)
            print(f"Exported to {out_path}")
        else:
            for s in sessions:
                print(history.format_session_summary(s))

    db.close()


if __name__ == "__main__":
    main()
