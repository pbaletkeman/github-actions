import * as fs from 'fs';
import * as path from 'path';
import { Question } from '../models/Question';
import { ParseError } from '../exceptions/QuizExceptions';

export interface ParsedQuestion {
  questionText: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
  optionE?: string;
  correctAnswer: string;
  explanation?: string;
  section?: string;
  difficulty?: string;
}

/**
 * Parse a markdown file containing quiz questions in the following format:
 *
 * ```markdown
 * ## Q1
 * > Question text here
 * - A) Option A text
 * - B) Option B text
 * - C) Option C text
 * - D) Option D text
 * **Answer: A**
 * > Optional explanation text
 * ```
 *
 * Also supports the GH-200 format:
 * ```markdown
 * ### Question 1 — Section Name
 * **Difficulty**: Easy
 * **Answer Type**: one
 * **Topic**: Topic name
 *
 * **Question**:
 * Question text here
 *
 * - A) Option A text
 * - B) Option B text
 * - C) Option C text
 * - D) Option D text
 *
 * **Answer: B**
 * ```
 */
export function parseMarkdownFile(filePath: string): ParsedQuestion[] {
  if (!fs.existsSync(filePath)) {
    throw new ParseError(`File not found: ${filePath}`);
  }

  const content = fs.readFileSync(filePath, 'utf-8');
  return parseMarkdownContent(content, path.basename(filePath));
}

export function parseMarkdownContent(content: string, sourceFile = ''): ParsedQuestion[] {
  const questions: ParsedQuestion[] = [];
  const blocks = splitIntoBlocks(content);

  for (const block of blocks) {
    try {
      const parsed = parseBlock(block, sourceFile);
      if (parsed) {
        questions.push(parsed);
      }
    } catch (err) {
      if (err instanceof ParseError) {
        throw err;
      }
      // Skip malformed blocks in lenient mode
    }
  }

  return questions;
}

function splitIntoBlocks(content: string): string[] {
  // Split on ### Question N or ## Q markers
  const parts = content.split(/(?=^#{2,3}\s+(?:Q\d+|Question\s+\d+))/m);
  return parts.filter((p) => p.trim().length > 0);
}

function parseBlock(block: string, sourceFile: string): ParsedQuestion | null {
  const lines = block.split('\n').map((l) => l.trim());

  // Extract header info
  const headerLine = lines[0];
  let section: string | undefined;
  let difficulty: string | undefined;

  const sectionMatch = headerLine.match(/^#{2,3}\s+(?:Q\d+|Question\s+\d+)(?:\s+[—-]\s+(.+))?/);
  if (!sectionMatch) return null;
  if (sectionMatch[1]) {
    section = sectionMatch[1].trim();
  }

  // Extract metadata
  for (const line of lines) {
    const diffMatch = line.match(/^\*\*Difficulty\*\*:\s*(.+)/);
    if (diffMatch) difficulty = diffMatch[1].trim();
  }

  // Extract question text: lines after "> " or after "**Question**:"
  let questionText = '';
  const questionMode = block.includes('**Question**:') || block.includes('**Scenario**:');

  if (questionMode) {
    questionText = extractStructuredQuestion(block);
  } else {
    // Simple format: > question text
    const quoteMatch = block.match(/^>\s*(.+?)$/m);
    if (quoteMatch) {
      questionText = quoteMatch[1].trim();
    }
  }

  if (!questionText) return null;

  // Extract options
  const optionMap: Record<string, string> = {};
  const optionRegex = /^-\s+([A-E])\)\s+(.+)$/gm;
  let match: RegExpExecArray | null;
  while ((match = optionRegex.exec(block)) !== null) {
    optionMap[match[1]] = match[2].trim();
  }

  if (!optionMap['A'] || !optionMap['B'] || !optionMap['C'] || !optionMap['D']) {
    throw new ParseError(
      `Question missing required options A-D: "${questionText.substring(0, 50)}..."`,
    );
  }

  // Extract answer
  const answerMatch = block.match(/\*\*Answer:\s*([A-E])\*\*/i);
  if (!answerMatch) {
    throw new ParseError(
      `Question missing answer line: "${questionText.substring(0, 50)}..."`,
    );
  }
  const correctAnswer = answerMatch[1].toUpperCase();

  // Extract explanation (text after answer line)
  let explanation: string | undefined;
  const answerLineIdx = block.indexOf(`**Answer: ${correctAnswer}**`);
  if (answerLineIdx !== -1) {
    const afterAnswer = block.slice(answerLineIdx + `**Answer: ${correctAnswer}**`.length).trim();
    if (afterAnswer.length > 0) {
      explanation = afterAnswer.replace(/^>\s*/gm, '').trim();
    }
  }

  return {
    questionText,
    optionA: optionMap['A'],
    optionB: optionMap['B'],
    optionC: optionMap['C'],
    optionD: optionMap['D'],
    optionE: optionMap['E'],
    correctAnswer,
    explanation,
    section,
    difficulty,
  };
}

function extractStructuredQuestion(block: string): string {
  // Handle: **Scenario**: ... **Question**: ...
  // Or: **Question**: ...
  const scenarioMatch = block.match(/\*\*Scenario\*\*:\s*([\s\S]+?)(?=\*\*Question\*\*:|$)/);
  const questionMatch = block.match(/\*\*Question\*\*:\s*([\s\S]+?)(?=\n-\s[A-E]\)|$)/);

  let text = '';
  if (scenarioMatch) {
    text += scenarioMatch[1].trim() + '\n\n';
  }
  if (questionMatch) {
    text += questionMatch[1].trim();
  }
  return text.trim();
}
