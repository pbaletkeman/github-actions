package cli

import (
	"bufio"
	"fmt"
	"os"
	"strings"
	"time"
)

// GetUserAnswer reads user input and validates it against valid options.
// Returns the answer in uppercase.
func GetUserAnswer(validOptions []string) string {
	scanner := bufio.NewScanner(os.Stdin)
	validSet := make(map[string]bool)
	for _, opt := range validOptions {
		validSet[strings.ToUpper(opt)] = true
	}

	for {
		fmt.Print("Your answer: ")
		if scanner.Scan() {
			input := strings.TrimSpace(strings.ToUpper(scanner.Text()))
			if validSet[input] {
				return input
			}
			fmt.Printf("Invalid option. Please enter one of %v\n", validOptions)
		}
	}
}

// AskYesNo reads y/n input from the user.
func AskYesNo(question string) bool {
	scanner := bufio.NewScanner(os.Stdin)
	for {
		fmt.Printf("%s (y/n): ", question)
		if scanner.Scan() {
			input := strings.TrimSpace(strings.ToLower(scanner.Text()))
			if input == "y" || input == "yes" {
				return true
			}
			if input == "n" || input == "no" {
				return false
			}
			fmt.Println("Please enter y or n")
		}
	}
}

// GetTimedAnswer reads user input with a timeout. Returns the answer and time taken in seconds.
func GetTimedAnswer(validOptions []string, timeoutSec int) (string, int) {
	start := time.Now()
	type result struct {
		answer string
	}

	ch := make(chan result, 1)
	go func() {
		answer := GetUserAnswer(validOptions)
		ch <- result{answer: answer}
	}()

	select {
	case res := <-ch:
		elapsed := int(time.Since(start).Seconds())
		return res.answer, elapsed
	case <-time.After(time.Duration(timeoutSec) * time.Second):
		elapsed := int(time.Since(start).Seconds())
		fmt.Println("\nTime's up!")
		return "", elapsed
	}
}
