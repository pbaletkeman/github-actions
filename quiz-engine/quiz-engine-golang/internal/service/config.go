package service

// Config holds application configuration.
type Config struct {
	DBPath       string
	NumQuestions int
	SecondsPerQ  int
	TotalSeconds int
}

// DefaultConfig returns the default application configuration.
func DefaultConfig() Config {
	return Config{
		DBPath:       "./quiz.db",
		NumQuestions: 20,
		SecondsPerQ:  60,
		TotalSeconds: 3600,
	}
}
