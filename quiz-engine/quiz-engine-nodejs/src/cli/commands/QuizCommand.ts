import { Argv } from 'yargs';
import chalk from 'chalk';
import { DataSource } from 'typeorm';
import { QuizEngine } from '../../service/QuizEngine';
import { Formatter, ReviewItem } from '../Formatter';
import { Prompts } from '../Prompts';
import { QuizUtils } from '../../service/QuizUtils';

interface QuizArgs {
  questions: number;
  'seconds-per': number;
}

export const quizCommand = {
  command: 'quiz',
  describe: 'Take a quiz',
  builder: (yargs: Argv): Argv<QuizArgs> =>
    yargs
      .option('questions', {
        alias: 'q',
        type: 'number',
        default: 10,
        describe: 'Number of questions',
      })
      .option('seconds-per', {
        alias: 's',
        type: 'number',
        default: 0,
        describe: 'Seconds allowed per question (0 = unlimited)',
      }),
  handler: async (argv: QuizArgs & { dataSource: DataSource }): Promise<void> => {
    const engine = new QuizEngine(argv.dataSource);

    console.log(chalk.bold('\n🎯 Starting Quiz...'));
    console.log(chalk.dim(`Loading ${argv.questions} questions...\n`));

    let state;
    try {
      state = await engine.startSession(argv.questions);
    } catch (err) {
      console.error(chalk.red(`❌ ${err instanceof Error ? err.message : String(err)}`));
      process.exit(1);
    }

    const reviewItems: ReviewItem[] = [];

    for (let i = 0; i < state.questions.length; i++) {
      const quizQ = state.questions[i];
      console.log(
        Formatter.question(i + 1, state.questions.length, quizQ.questionText, quizQ.shuffledOptions),
      );

      const answer = await Prompts.selectAnswer(quizQ.shuffledOptions);
      const isCorrect = await engine.submitAnswer(state, i, answer);

      reviewItems.push({
        questionText: quizQ.questionText,
        shuffledOptions: quizQ.shuffledOptions,
        correctShuffledIndex: quizQ.correctShuffledIndex,
        userAnswerLetter: answer,
        isCorrect,
        explanation: quizQ.explanation,
      });

      if (i < state.questions.length - 1) {
        await Prompts.pressEnterToContinue();
      }
    }

    const session = await engine.finalizeSession(state);
    const percentage = Number(session.percentageCorrect);
    const grade = QuizUtils.getGrade(percentage);
    const passed = QuizUtils.isPassing(percentage);
    const duration = session.timeTakenSeconds
      ? QuizUtils.formatDuration(session.timeTakenSeconds)
      : 'N/A';

    console.log(Formatter.reviewSection(reviewItems));
    console.log(
      Formatter.quizResult(
        session.numCorrect,
        session.numQuestions,
        percentage,
        grade,
        passed,
        duration,
      ),
    );
    console.log(chalk.dim(`Session ID: ${session.sessionId}`));
  },
};
