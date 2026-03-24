import { Argv } from 'yargs';
import chalk from 'chalk';
import { DataSource } from 'typeorm';
import { ImportService } from '../../service/ImportService';
import { Formatter } from '../Formatter';

interface ImportArgs {
  file?: string;
  dir?: string;
}

export const importCommand = {
  command: 'import',
  describe: 'Import questions from markdown files',
  builder: (yargs: Argv): Argv<ImportArgs> =>
    yargs
      .option('file', {
        alias: 'f',
        type: 'string',
        describe: 'Path to a single markdown file',
      })
      .option('dir', {
        alias: 'd',
        type: 'string',
        describe: 'Path to a directory of markdown files',
      })
      .check((argv) => {
        if (!argv.file && !argv.dir) {
          throw new Error('Provide --file or --dir');
        }
        return true;
      }),
  handler: async (argv: ImportArgs & { dataSource: DataSource }): Promise<void> => {
    const service = new ImportService(argv.dataSource);

    if (argv.file) {
      console.log(chalk.bold(`\n📥 Importing from: ${argv.file}`));
      const result = await service.importFile(argv.file);
      if (result.errors.length > 0) {
        result.errors.forEach((e) => console.error(chalk.red(`  ❌ ${e}`)));
      } else {
        console.log(chalk.green(`  ✅ Imported: ${result.imported}, Skipped (duplicates): ${result.skipped}`));
      }
    }

    if (argv.dir) {
      console.log(chalk.bold(`\n📥 Importing from directory: ${argv.dir}`));
      const results = await service.importDirectory(argv.dir);
      let totalImported = 0;
      let totalSkipped = 0;
      for (const r of results) {
        if (r.errors.length > 0) {
          console.error(chalk.red(`  ❌ ${r.filePath}: ${r.errors.join('; ')}`));
        } else {
          console.log(chalk.green(`  ✅ ${r.filePath}: ${r.imported} imported, ${r.skipped} skipped`));
          totalImported += r.imported;
          totalSkipped += r.skipped;
        }
      }
      console.log(chalk.bold(`\nTotal: ${totalImported} imported, ${totalSkipped} skipped`));
    }
  },
};
