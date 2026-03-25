package database

import (
	"database/sql"
	"time"

	"github.com/pbaletkeman/quiz-engine-golang/internal/models"
)

func InsertQuestion(db *sql.DB, q models.Question) (int64, error) {
	res, err := db.Exec(`INSERT OR IGNORE INTO questions
		(question_text, option_a, option_b, option_c, option_d, option_e,
		 correct_answer, explanation, section, difficulty, source_file)
		VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
		q.QuestionText, q.OptionA, q.OptionB, q.OptionC, q.OptionD, q.OptionE,
		q.CorrectAnswer, q.Explanation, q.Section, q.Difficulty, q.SourceFile)
	if err != nil {
		return 0, err
	}
	return res.LastInsertId()
}

func GetRandomQuestions(db *sql.DB, n int, difficulty, section string) ([]models.Question, error) {
	cycle, err := GetCurrentCycle(db)
	if err != nil {
		return nil, err
	}

	query := `SELECT id, question_text, option_a, option_b, option_c, option_d, COALESCE(option_e,''),
		section, difficulty, source_file, created_at, usage_cycle, times_used, last_used_at
		FROM questions WHERE usage_cycle = ?`
	args := []interface{}{cycle}

	if difficulty != "" {
		query += " AND difficulty = ?"
		args = append(args, difficulty)
	}
	if section != "" {
		query += " AND section = ?"
		args = append(args, section)
	}
	query += " ORDER BY RANDOM() LIMIT ?"
	args = append(args, n)

	rows, err := db.Query(query, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var questions []models.Question
	for rows.Next() {
		var q models.Question
		var lastUsed sql.NullTime
		var createdAt sql.NullTime
		var section sql.NullString
		var difficulty sql.NullString
		var sourceFile sql.NullString
		err := rows.Scan(&q.ID, &q.QuestionText, &q.OptionA, &q.OptionB, &q.OptionC, &q.OptionD, &q.OptionE,
			&section, &difficulty, &sourceFile, &createdAt, &q.UsageCycle, &q.TimesUsed, &lastUsed)
		if err != nil {
			return nil, err
		}
		if section.Valid {
			q.Section = section.String
		}
		if difficulty.Valid {
			q.Difficulty = difficulty.String
		}
		if sourceFile.Valid {
			q.SourceFile = sourceFile.String
		}
		if createdAt.Valid {
			q.CreatedAt = createdAt.Time
		}
		if lastUsed.Valid {
			q.LastUsedAt = &lastUsed.Time
		}
		// CorrectAnswer and Explanation intentionally left empty
		questions = append(questions, q)
	}
	return questions, rows.Err()
}

func GetQuestionWithAnswer(db *sql.DB, id int64) (*models.Question, error) {
	row := db.QueryRow(`SELECT id, question_text, option_a, option_b, option_c, option_d, COALESCE(option_e,''),
		correct_answer, COALESCE(explanation,''), section, difficulty, source_file, created_at,
		usage_cycle, times_used, last_used_at
		FROM questions WHERE id = ?`, id)

	var q models.Question
	var lastUsed sql.NullTime
	var createdAt sql.NullTime
	var section sql.NullString
	var difficulty sql.NullString
	var sourceFile sql.NullString
	err := row.Scan(&q.ID, &q.QuestionText, &q.OptionA, &q.OptionB, &q.OptionC, &q.OptionD, &q.OptionE,
		&q.CorrectAnswer, &q.Explanation, &section, &difficulty, &sourceFile,
		&createdAt, &q.UsageCycle, &q.TimesUsed, &lastUsed)
	if err != nil {
		return nil, err
	}
	if section.Valid {
		q.Section = section.String
	}
	if difficulty.Valid {
		q.Difficulty = difficulty.String
	}
	if sourceFile.Valid {
		q.SourceFile = sourceFile.String
	}
	if createdAt.Valid {
		q.CreatedAt = createdAt.Time
	}
	if lastUsed.Valid {
		q.LastUsedAt = &lastUsed.Time
	}
	return &q, nil
}

func GetAllQuestions(db *sql.DB) ([]models.Question, error) {
	rows, err := db.Query(`SELECT id, question_text, option_a, option_b, option_c, option_d, COALESCE(option_e,''),
		correct_answer, COALESCE(explanation,''), section, difficulty, source_file, created_at,
		usage_cycle, times_used, last_used_at FROM questions ORDER BY id`)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var questions []models.Question
	for rows.Next() {
		var q models.Question
		var lastUsed sql.NullTime
		var createdAt sql.NullTime
		var section sql.NullString
		var difficulty sql.NullString
		var sourceFile sql.NullString
		err := rows.Scan(&q.ID, &q.QuestionText, &q.OptionA, &q.OptionB, &q.OptionC, &q.OptionD, &q.OptionE,
			&q.CorrectAnswer, &q.Explanation, &section, &difficulty, &sourceFile,
			&createdAt, &q.UsageCycle, &q.TimesUsed, &lastUsed)
		if err != nil {
			return nil, err
		}
		if section.Valid {
			q.Section = section.String
		}
		if difficulty.Valid {
			q.Difficulty = difficulty.String
		}
		if sourceFile.Valid {
			q.SourceFile = sourceFile.String
		}
		if createdAt.Valid {
			q.CreatedAt = createdAt.Time
		}
		if lastUsed.Valid {
			q.LastUsedAt = &lastUsed.Time
		}
		questions = append(questions, q)
	}
	return questions, rows.Err()
}

func CountQuestions(db *sql.DB) (int, error) {
	var count int
	err := db.QueryRow("SELECT COUNT(*) FROM questions").Scan(&count)
	return count, err
}

func GetCurrentCycle(db *sql.DB) (int64, error) {
	var cycle sql.NullInt64
	err := db.QueryRow("SELECT MIN(usage_cycle) FROM questions").Scan(&cycle)
	if err != nil {
		return 1, err
	}
	if !cycle.Valid {
		return 1, nil
	}
	return cycle.Int64, nil
}

func MarkQuestionUsed(db *sql.DB, id int64) error {
	_, err := db.Exec(`UPDATE questions SET times_used = times_used + 1, last_used_at = ? WHERE id = ?`,
		time.Now(), id)
	return err
}

func AdvanceCycleIfExhausted(db *sql.DB) error {
	cycle, err := GetCurrentCycle(db)
	if err != nil {
		return err
	}

	var unusedCount int
	err = db.QueryRow(`SELECT COUNT(*) FROM questions WHERE usage_cycle = ? AND last_used_at IS NULL`, cycle).Scan(&unusedCount)
	if err != nil {
		return err
	}

	if unusedCount == 0 {
		_, err = db.Exec(`UPDATE questions SET usage_cycle = usage_cycle + 1`)
		return err
	}
	return nil
}

func DeleteAllQuestions(db *sql.DB) error {
	_, err := db.Exec("DELETE FROM questions")
	return err
}
