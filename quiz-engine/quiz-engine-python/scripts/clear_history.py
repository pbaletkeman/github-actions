import argparse
import os
import sys
from datetime import datetime, timedelta

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from quiz_engine.database import DatabaseManager


def main():
    parser = argparse.ArgumentParser(description="Clear quiz history")
    parser.add_argument('--db', default='quiz_engine/quiz.db', help='Database path')
    parser.add_argument('--session-id', help='Delete specific session')
    parser.add_argument('--all', action='store_true', help='Delete all sessions')
    parser.add_argument('--before', type=int, help='Delete sessions older than N days')
    parser.add_argument('--confirm', action='store_true', required=True,
                        help='Must confirm deletion')
    args = parser.parse_args()

    db = DatabaseManager(args.db)
    db.init_schema()

    if args.session_id:
        db.delete_session(args.session_id)
        print(f"Deleted session {args.session_id}")
    elif args.all:
        db.delete_all_sessions()
        print("Deleted all sessions.")
    elif args.before:
        cutoff = datetime.utcnow() - timedelta(days=args.before)
        db.delete_sessions_before(cutoff)
        print(f"Deleted sessions before {cutoff.date()}")
    else:
        print("Specify --session-id, --all, or --before")
        sys.exit(1)

    db.close()


if __name__ == "__main__":
    main()
