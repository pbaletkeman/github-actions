import Table from 'cli-table3';
import chalk from 'chalk';

export interface ReviewItem {
  questionText: string;
  shuffledOptions: string[];
  correctShuffledIndex: number;
  userAnswerLetter: string;
  isCorrect: boolean;
  explanation?: string;
}

export class Formatter {
  /**
   * Render a table from an array of objects. All objects should have the same keys.
   */
  static table(data: Record<string, unknown>[]): string {
    if (data.length === 0) return chalk.yellow('No data to display.');

    const headers = Object.keys(data[0]);
    const table = new Table({
      head: headers.map((h) => chalk.cyan(h)),
      style: { head: [], border: [] },
    });

    for (const row of data) {
      table.push(headers.map((h) => String(row[h] ?? '')));
    }

    return table.toString();
  }

  /**
   * Render a bordered box with a title and content.
   */
  static box(title: string, content: string): string {
    const lines = content.split('\n');
    const maxWidth = Math.max(title.length + 4, ...lines.map((l) => l.length + 4), 40);
    const border = '─'.repeat(maxWidth - 2);

    const header = `┌${border}┐`;
    const footer = `└${border}┘`;
    const titleLine = `│ ${chalk.bold(title)}${' '.repeat(maxWidth - title.length - 3)}│`;
    const separator = `├${border}┤`;
    const contentLines = lines.map(
      (l) => `│ ${l}${' '.repeat(Math.max(0, maxWidth - l.length - 3))}│`,
    );

    return [header, titleLine, separator, ...contentLines, footer].join('\n');
  }

  static reviewSection(items: ReviewItem[]): string {
    const letters = ['A', 'B', 'C', 'D', 'E'];
    const lines: string[] = [];
    lines.push(chalk.bold('\n📝 Answer Review'));
    lines.push('─'.repeat(60));

    for (let i = 0; i < items.length; i++) {
      const item = items[i];
      const marker = item.isCorrect ? chalk.green('✓') : chalk.red('✗');
      const firstLine = item.questionText.split('\n')[0];
      lines.push(`${marker} Q${i + 1}: ${firstLine}`);

      if (!item.isCorrect) {
        const correctLetter = letters[item.correctShuffledIndex];
        const correctText = item.shuffledOptions[item.correctShuffledIndex];
        lines.push(
          chalk.dim(`   Your answer: ${item.userAnswerLetter} | Correct: ${correctLetter}) ${correctText}`),
        );
        if (item.explanation) {
          lines.push(chalk.yellow(`   💡 ${item.explanation}`));
        }
      }
    }
    lines.push('');
    return lines.join('\n');
  }

  /**
   * Format a quiz result summary.
   */
  static quizResult(
    numCorrect: number,
    numQuestions: number,
    percentage: number,
    grade: string,
    passed: boolean,
    duration: string,
  ): string {
    const lines: string[] = [];
    lines.push(chalk.bold('\n📊 Quiz Results'));
    lines.push('─'.repeat(40));
    lines.push(`Score:    ${chalk.bold(`${numCorrect} / ${numQuestions}`)}`);
    lines.push(`Percent:  ${chalk.bold(`${percentage}%`)}`);
    lines.push(`Grade:    ${chalk.bold(grade)}`);
    lines.push(`Status:   ${passed ? chalk.green('✅ PASSED') : chalk.red('❌ FAILED')}`);
    lines.push(`Duration: ${duration}`);
    lines.push('─'.repeat(40));
    return lines.join('\n');
  }

  /**
   * Format a single question for display.
   */
  static question(
    questionNumber: number,
    totalQuestions: number,
    questionText: string,
    options: string[],
  ): string {
    const letters = ['A', 'B', 'C', 'D', 'E'];
    const lines: string[] = [];
    lines.push(chalk.bold(`\nQuestion ${questionNumber} of ${totalQuestions}`));
    lines.push('─'.repeat(60));
    lines.push(chalk.white(questionText));
    lines.push('');
    options.forEach((opt, idx) => {
      lines.push(`  ${chalk.cyan(letters[idx])}) ${opt}`);
    });
    lines.push('');
    return lines.join('\n');
  }

  /**
   * Format answer feedback.
   */
  static answerFeedback(
    isCorrect: boolean,
    correctLetter: string,
    correctText: string,
    explanation?: string,
  ): string {
    const lines: string[] = [];
    if (isCorrect) {
      lines.push(chalk.green('✅ Correct!'));
    } else {
      lines.push(chalk.red(`❌ Incorrect. Correct answer: ${correctLetter}) ${correctText}`));
    }
    if (explanation) {
      lines.push(chalk.dim(`💡 ${explanation}`));
    }
    lines.push('');
    return lines.join('\n');
  }
}
