import * as fs from 'fs';
import * as path from 'path';
import { DataSource } from 'typeorm';
import { parseMarkdownFile } from './MarkdownParser';
import { QuizService } from './QuizService';

export interface ImportResult {
  filePath: string;
  imported: number;
  skipped: number;
  errors: string[];
}

export class ImportService {
  private readonly quizService: QuizService;

  constructor(dataSource: DataSource) {
    this.quizService = new QuizService(dataSource);
  }

  async importFile(filePath: string): Promise<ImportResult> {
    const result: ImportResult = {
      filePath,
      imported: 0,
      skipped: 0,
      errors: [],
    };

    try {
      const parsed = parseMarkdownFile(filePath);
      const { imported, skipped } = await this.quizService.importQuestions(
        parsed,
        path.basename(filePath),
      );
      result.imported = imported;
      result.skipped = skipped;
    } catch (err) {
      result.errors.push(err instanceof Error ? err.message : String(err));
    }

    return result;
  }

  async importDirectory(dirPath: string): Promise<ImportResult[]> {
    if (!fs.existsSync(dirPath)) {
      return [{ filePath: dirPath, imported: 0, skipped: 0, errors: [`Directory not found: ${dirPath}`] }];
    }

    const files = fs
      .readdirSync(dirPath)
      .filter((f) => f.endsWith('.md') || f.endsWith('.markdown'))
      .map((f) => path.join(dirPath, f));

    const results: ImportResult[] = [];
    for (const file of files) {
      results.push(await this.importFile(file));
    }
    return results;
  }
}
