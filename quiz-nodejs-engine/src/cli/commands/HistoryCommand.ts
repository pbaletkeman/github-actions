import { Argv } from 'yargs';
import chalk from 'chalk';
import * as fs from 'fs';
import { DataSource } from 'typeorm';
import { HistoryService } from '../../service/HistoryService';
import { Formatter } from '../Formatter';

interface HistoryArgs {
  sessionId?: string;
  review: boolean;
  export?: string;
  limit: number;
}

export const historyCommand = {
  command: 'history',
  describe: 'View quiz history',
  builder: (yargs: Argv): Argv<HistoryArgs> =>
    yargs
      .option('session-id', {
        alias: 'i',
        type: 'string',
        describe: 'Session ID to inspect',
      })
      .option('review', {
        alias: 'r',
        type: 'boolean',
        default: false,
        describe: 'Show full answer review for a session',
      })
      .option('export', {
        alias: 'e',
        type: 'string',
        describe: 'Export format: json or csv',
        choices: ['json', 'csv'],
      })
      .option('limit', {
        alias: 'l',
        type: 'number',
        default: 10,
        describe: 'Number of recent sessions to show',
      }),
  handler: async (argv: HistoryArgs & { dataSource: DataSource }): Promise<void> => {
    const service = new HistoryService(argv.dataSource);

    if (argv.export) {
      const format = argv.export as 'json' | 'csv';
      const data = await service.exportSessions(format);
      const filename = `quiz-history.${format}`;
      fs.writeFileSync(filename, data);
      console.log(chalk.green(`✅ Exported to ${filename}`));
      return;
    }

    if (argv.sessionId && argv.review) {
      const review = await service.getSessionReview(argv.sessionId);
      if (!review) {
        console.error(chalk.red(`Session not found: ${argv.sessionId}`));
        return;
      }
      console.log(chalk.bold(`\n📋 Review: Session ${argv.sessionId}`));
      console.log(Formatter.table([review.summary as unknown as Record<string, unknown>]));
      console.log(chalk.bold('\nAnswer Key:'));
      for (const r of review.responses) {
        const icon = r.isCorrect ? chalk.green('✅') : chalk.red('❌');
        console.log(
          `  ${icon} Q${r.questionNumber}: You answered ${r.userAnswer}, Correct: ${r.correctAnswer}`,
        );
        if (!r.isCorrect && r.explanation) {
          console.log(chalk.dim(`     💡 ${r.explanation}`));
        }
      }
      return;
    }

    // Show session list
    const sessions = await service.getRecentSessions(argv.limit);
    if (sessions.length === 0) {
      console.log(chalk.yellow('\nNo quiz sessions found. Take a quiz first!'));
      return;
    }

    console.log(chalk.bold(`\n📊 Recent Quiz Sessions (last ${argv.limit}):`));
    console.log(Formatter.table(sessions as unknown as Record<string, unknown>[]));
  },
};
