import argparse
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from quiz_engine.database import DatabaseManager


def main():
    parser = argparse.ArgumentParser(description="Clear the questions database")
    parser.add_argument('--db', default='quiz_engine/quiz.db', help='Database path')
    parser.add_argument('--confirm', action='store_true', required=True,
                        help='Must confirm deletion')
    args = parser.parse_args()

    db = DatabaseManager(args.db)
    db.init_schema()
    count = db.count_questions()
    db.delete_all_questions()
    print(f"Deleted {count} questions from database.")
    db.close()


if __name__ == "__main__":
    main()
