import 'reflect-metadata';
import * as dotenv from 'dotenv';
import yargs from 'yargs';
import { hideBin } from 'yargs/helpers';
import { initializeDatabase, AppDataSource } from './database/database';
import { quizCommand } from './cli/commands/QuizCommand';
import { importCommand } from './cli/commands/ImportCommand';
import { historyCommand } from './cli/commands/HistoryCommand';
import { clearCommand } from './cli/commands/ClearCommand';

dotenv.config();

async function main(): Promise<void> {
  try {
    await initializeDatabase();
  } catch (err) {
    console.error('Failed to initialize database:', err);
    process.exit(1);
  }

  // Inject the data source into all command handlers via middleware
  const withDataSource = (handler: Function) => async (argv: object) => {
    await handler({ ...argv, dataSource: AppDataSource });
  };

  await yargs(hideBin(process.argv))
    .scriptName('quiz-engine')
    .usage('$0 <command> [options]')
    .command({
      ...quizCommand,
      handler: withDataSource(quizCommand.handler),
    })
    .command({
      ...importCommand,
      handler: withDataSource(importCommand.handler),
    })
    .command({
      ...historyCommand,
      handler: withDataSource(historyCommand.handler),
    })
    .command({
      ...clearCommand,
      handler: withDataSource(clearCommand.handler),
    })
    .demandCommand(1, 'You must provide a command')
    .help()
    .version()
    .strict()
    .parseAsync();
}

main().catch((err) => {
  console.error('Fatal error:', err);
  process.exit(1);
});
