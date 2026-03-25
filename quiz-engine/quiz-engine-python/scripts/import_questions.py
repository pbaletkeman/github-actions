import argparse
import os
import sys
import glob as glob_module

sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from quiz_engine.database import DatabaseManager
from quiz_engine.utils import parse_markdown_file


def import_file(db, file_path):
    questions = parse_markdown_file(file_path)
    imported = skipped = errors = 0
    for q in questions:
        q.source_file = os.path.basename(file_path)
        try:
            result = db.insert_question(q)
            if result:
                imported += 1
            else:
                skipped += 1
        except Exception as e:
            print(f"Error importing question: {e}")
            errors += 1
    return imported, skipped, errors


def main():
    parser = argparse.ArgumentParser(description="Import questions from Markdown files")
    parser.add_argument('--file', help='Single markdown file to import')
    parser.add_argument('--dir', help='Directory containing markdown files')
    parser.add_argument('--db', default='quiz_engine/quiz.db', help='Database path')
    args = parser.parse_args()

    db = DatabaseManager(args.db)
    db.init_schema()

    files = []
    if args.file:
        files.append(args.file)
    elif args.dir:
        files = sorted(glob_module.glob(os.path.join(args.dir, '*.md')))
    else:
        print("Error: --file or --dir required")
        sys.exit(1)

    total_imported = total_skipped = total_errors = 0
    for file_path in files:
        print(f"Importing {file_path}...")
        imp, skip, err = import_file(db, file_path)
        print(f"  Imported: {imp}, Skipped: {skip}, Errors: {err}")
        total_imported += imp
        total_skipped += skip
        total_errors += err

    print(f"\nTotal: Imported={total_imported}, Skipped={total_skipped}, Errors={total_errors}")
    db.close()


if __name__ == "__main__":
    main()
