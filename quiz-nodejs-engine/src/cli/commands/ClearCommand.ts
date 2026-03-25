import { Argv } from 'yargs';
import chalk from 'chalk';
import { DataSource } from 'typeorm';
import { QuizService } from '../../service/QuizService';
import { Prompts } from '../Prompts';

interface ClearArgs {
  questions: boolean;
  history: boolean;
  all: boolean;
  confirm: boolean;
}

export const clearCommand = {
  command: 'clear',
  describe: 'Clear stored data',
  builder: (yargs: Argv): Argv<ClearArgs> =>
    yargs
      .option('questions', {
        type: 'boolean',
        default: false,
        describe: 'Clear all questions from the database',
      })
      .option('history', {
        type: 'boolean',
        default: false,
        describe: 'Clear all quiz session history',
      })
      .option('all', {
        type: 'boolean',
        default: false,
        describe: 'Clear all data (questions + history)',
      })
      .option('confirm', {
        type: 'boolean',
        default: false,
        describe: 'Skip confirmation prompt',
      })
      .check((argv) => {
        if (!argv.questions && !argv.history && !argv.all) {
          throw new Error('Specify --questions, --history, or --all');
        }
        return true;
      }),
  handler: async (argv: ClearArgs & { dataSource: DataSource }): Promise<void> => {
    const service = new QuizService(argv.dataSource);

    let confirmed = argv.confirm;
    if (!confirmed) {
      let target = '';
      if (argv.all) target = 'ALL data (questions + sessions + responses)';
      else if (argv.questions && argv.history) target = 'questions and session history';
      else if (argv.questions) target = 'all questions';
      else if (argv.history) target = 'all session history';

      confirmed = await Prompts.confirm(`Are you sure you want to delete ${target}?`);
    }

    if (!confirmed) {
      console.log(chalk.yellow('Aborted.'));
      return;
    }

    if (argv.all) {
      await service.clearAll();
      console.log(chalk.green('✅ All data cleared.'));
      return;
    }

    if (argv.history) {
      await service.deleteAllResponses();
      await service.deleteAllSessions();
      console.log(chalk.green('✅ Session history cleared.'));
    }

    if (argv.questions) {
      await service.deleteAllQuestions();
      console.log(chalk.green('✅ Questions cleared.'));
    }
  },
};
