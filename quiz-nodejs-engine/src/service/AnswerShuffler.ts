import { Question } from '../models/Question';

export interface ShuffleResult {
  /** The shuffled display options (e.g., ["Beta", "Alpha", "Gamma", "Delta"]) */
  shuffledOptions: string[];
  /** Index in shuffledOptions that corresponds to the correct answer */
  correctShuffledIndex: number;
  /** Map from shuffled display letter (A-D/E) to the original option letter */
  displayToOriginal: Record<string, string>;
}

const LETTERS = ['A', 'B', 'C', 'D', 'E'] as const;

/**
 * Shuffle the answer options for a question using the Fisher-Yates algorithm.
 * Returns the shuffled options, the index of the correct answer, and a display-to-original map.
 */
export function shuffleAnswers(question: Question): ShuffleResult {
  const options = buildOptions(question);
  const correctText = getCorrectText(question);

  // Fisher-Yates shuffle
  const shuffled = [...options];
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
  }

  const correctShuffledIndex = shuffled.indexOf(correctText);

  const displayToOriginal: Record<string, string> = {};
  shuffled.forEach((text, idx) => {
    const displayLetter = LETTERS[idx];
    const originalIdx = options.indexOf(text);
    displayToOriginal[displayLetter] = LETTERS[originalIdx];
  });

  return { shuffledOptions: shuffled, correctShuffledIndex, displayToOriginal };
}

function buildOptions(question: Question): string[] {
  const opts: string[] = [question.optionA, question.optionB, question.optionC, question.optionD];
  if (question.optionE) {
    opts.push(question.optionE);
  }
  return opts;
}

function getCorrectText(question: Question): string {
  switch (question.correctAnswer.toUpperCase()) {
    case 'A':
      return question.optionA;
    case 'B':
      return question.optionB;
    case 'C':
      return question.optionC;
    case 'D':
      return question.optionD;
    case 'E':
      return question.optionE ?? question.optionD;
    default:
      return question.optionA;
  }
}
