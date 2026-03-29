import * as fs from 'fs';
import * as path from 'path';
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

interface AnswerEntry {
  answer: string;
  explanation?: string;
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
  const answerKey = parseAnswerKey(content);
  const hasAnswerKey = answerKey.size > 0;

  // Strip the answer key section so it isn't parsed as a question block
  const questionsContent = hasAnswerKey
    ? content.replace(/^## Answer Key[\s\S]*$/m, '')
    : content;

  const questions: ParsedQuestion[] = [];
  const blocks = splitIntoBlocks(questionsContent);

  for (const block of blocks) {
    try {
      const parsed = parseBlock(block, sourceFile, answerKey, hasAnswerKey);
      if (parsed) {
        questions.push(parsed);
      }
    } catch (err) {
      if (!hasAnswerKey && err instanceof ParseError) {
        throw err;
      }
    }
  }

  return questions;
}

function parseAnswerKey(content: string): Map<number, AnswerEntry> {
  const map = new Map<number, AnswerEntry>();
  const keyMatch = content.match(/^## Answer Key([\s\S]*)$/m);
  if (!keyMatch) return map;

  const keySection = keyMatch[1];
  const rowRegex = /^\|\s*(\d+)\s*\|\s*([^|]+)\s*\|\s*([^|]*)\s*\|/gm;
  let match: RegExpExecArray | null;
  while ((match = rowRegex.exec(keySection)) !== null) {
    const qNum = parseInt(match[1], 10);
    const rawAnswer = match[2].trim();
    const explanation = match[3].trim() || undefined;
    // Only single-letter answers (skip "A, C" type answers for many-type questions)
    if (/^[A-E]$/.test(rawAnswer)) {
      map.set(qNum, { answer: rawAnswer, explanation });
    }
  }
  return map;
}

function splitIntoBlocks(content: string): string[] {
  const parts = content.split(/(?=^#{2,3}\s+(?:Q\d+|Question\s+\d+))/m);
  return parts.filter((p) => p.trim().length > 0);
}

function parseBlock(
  block: string,
  sourceFile: string,
  answerKey: Map<number, AnswerEntry>,
  hasAnswerKey: boolean,
): ParsedQuestion | null {
  const lines = block.split('\n').map((l) => l.trim());
  const headerLine = lines[0];
  const headerMatch = headerLine.match(
    /^#{2,3}\s+(?:Q(\d+)|Question\s+(\d+))(?:\s+[-\u2014\u2013]+\s+(.+))?/,
  );
  if (!headerMatch) return null;

  const questionNumStr = headerMatch[1] || headerMatch[2];
  const questionNum = questionNumStr ? parseInt(questionNumStr, 10) : 0;
  const section = headerMatch[3] ? headerMatch[3].trim() : undefined;

  let difficulty: string | undefined;
  for (const line of lines) {
    const diffMatch = line.match(/^\*\*Difficulty\*\*:\s*(.+)/);
    if (diffMatch) difficulty = diffMatch[1].trim();
  }

  // Skip many/none answer types — only process 'one' type
  const answerTypeMatch = block.match(/^\*\*Answer\s+Type\*\*:\s*(.+?)$/m);
  if (answerTypeMatch && answerTypeMatch[1].trim().toLowerCase() !== 'one') {
    return null;
  }

  // Extract question text
  const questionMode = block.includes('**Question**:') || block.includes('**Scenario**:');
  let questionText = '';
  if (questionMode) {
    questionText = extractStructuredQuestion(block);
  } else {
    const quoteMatch = block.match(/^>\s*(.+?)$/m);
    if (quoteMatch) {
      questionText = quoteMatch[1].trim();
    }
  }

  if (!questionText) return null;

  // Extract options
  const optionMap: Record<string, string> = {};
  const optionRegex = /^-\s+([A-E])\)\s+(.+)$/gm;
  let optMatch: RegExpExecArray | null;
  while ((optMatch = optionRegex.exec(block)) !== null) {
    optionMap[optMatch[1]] = optMatch[2].trim();
  }

  if (!optionMap['A'] || !optionMap['B'] || !optionMap['C'] || !optionMap['D']) {
    throw new ParseError(
      `Question missing required options A-D: "${questionText.substring(0, 50)}..."`,
    );
  }

  // Get answer: answer key table takes priority, then inline **Answer: X**
  let correctAnswer: string | undefined;
  let explanation: string | undefined;

  const keyEntry = answerKey.get(questionNum);
  if (keyEntry) {
    correctAnswer = keyEntry.answer;
    explanation = keyEntry.explanation;
  } else {
    const answerMatch = block.match(/\*\*Answer:\s*([A-E])\*\*/i);
    if (!answerMatch) {
      if (hasAnswerKey) return null;
      throw new ParseError(
        `Question missing answer line: "${questionText.substring(0, 50)}..."`,
      );
    }
    correctAnswer = answerMatch[1].toUpperCase();
    const answerLineIdx = block.indexOf(`**Answer: ${correctAnswer}**`);
    if (answerLineIdx !== -1) {
      const afterAnswer = block
        .slice(answerLineIdx + `**Answer: ${correctAnswer}**`.length)
        .trim();
      if (afterAnswer.length > 0) {
        explanation = afterAnswer.replace(/^>\s*/gm, '').trim();
      }
    }
  }

  if (!correctAnswer) return null;

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
