package cli

import (
	"fmt"
	"strings"

	"github.com/fatih/color"
)

var (
	successColor = color.New(color.FgGreen, color.Bold)
	errorColor   = color.New(color.FgRed, color.Bold)
	infoColor    = color.New(color.FgCyan)
	warningColor = color.New(color.FgYellow)
	headerColor  = color.New(color.FgCyan, color.Bold)
)

// PrintSuccess prints a green success message.
func PrintSuccess(msg string) {
	successColor.Println(msg)
}

// PrintError prints a red error message.
func PrintError(msg string) {
	errorColor.Println(msg)
}

// PrintInfo prints a cyan info message.
func PrintInfo(msg string) {
	infoColor.Println(msg)
}

// PrintWarning prints a yellow warning message.
func PrintWarning(msg string) {
	warningColor.Println(msg)
}

// PrintHeader prints a bold cyan header with a separator line.
func PrintHeader(title string) {
	headerColor.Println(title)
	fmt.Println(strings.Repeat("=", len(title)))
}
