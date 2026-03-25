package service

import (
	"database/sql"

	"github.com/pbaletkeman/quiz-engine-golang/internal/database"
	"github.com/pbaletkeman/quiz-engine-golang/internal/engine"
)

// QuizService manages DB initialization and quiz engine creation.
type QuizService struct {
	DB     *sql.DB
	Config Config
}

// NewQuizService creates a new QuizService with an initialized DB.
func NewQuizService(cfg Config) (*QuizService, error) {
	db, err := database.NewDB(cfg.DBPath)
	if err != nil {
		return nil, err
	}
	return &QuizService{DB: db, Config: cfg}, nil
}

// NewEngine creates a new QuizEngine using this service's DB and config.
func (qs *QuizService) NewEngine() *engine.QuizEngine {
	cfg := engine.QuizConfig{
		NumQuestions: qs.Config.NumQuestions,
		DBPath:       qs.Config.DBPath,
	}
	return engine.NewQuizEngine(qs.DB, cfg)
}

// Close closes the database connection.
func (qs *QuizService) Close() error {
	return qs.DB.Close()
}
