package com.quizengine.cli;

import com.quizengine.entity.Question;
import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.service.QuizService;
import com.quizengine.util.AnswerShuffler;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
@Command(name = "quiz", description = "Take a quiz", mixinStandardHelpOptions = true)
public class QuizCommand implements Runnable {

    @Option(names = {"-n", "--questions"}, description = "Number of questions", defaultValue = "10")
    private int numQuestions;

    private final QuizService quizService;

    public QuizCommand(QuizService quizService) {
        this.quizService = quizService;
    }

    @Override
    public void run() {
        ConsoleFormatter.printHeader("GH-200 CERTIFICATION QUIZ");

        try {
            QuizSession session = quizService.startQuiz(numQuestions);
            List<Question> questions = quizService.getSessionQuestions(session.getSessionId());
            String sessionId = session.getSessionId();

            Scanner scanner = new Scanner(System.in);
            int correct = 0;
            List<ConsoleFormatter.ReviewItem> reviewItems = new ArrayList<>();

            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                Map<String, String> shuffled = AnswerShuffler.shuffleAnswers(question);
                String shuffledCorrect = AnswerShuffler.findCorrectLetter(shuffled, question);

                ConsoleFormatter.printQuestion(i + 1, questions.size(), question.getQuestionText());
                ConsoleFormatter.printOptions(shuffled);

                System.out.print("\nYour answer (A/B/C/D): ");
                String userAnswer = scanner.nextLine().trim().toUpperCase();

                boolean isCorrect = userAnswer.equals(shuffledCorrect);
                if (isCorrect) correct++;

                quizService.submitAnswer(
                    sessionId, i, isCorrect ?
                        question.getCorrectAnswer() : userAnswer, 0);

                reviewItems.add(new ConsoleFormatter.ReviewItem(
                    i + 1,
                    question.getQuestionText(),
                    userAnswer,
                    shuffledCorrect,
                    shuffled.getOrDefault(shuffledCorrect, question.getCorrectAnswer()),
                    isCorrect,
                    question.getExplanation()
                ));
            }

            QuizSession finalSession = quizService.finalizeQuiz(sessionId);
            ConsoleFormatter.printScore(finalSession.getPercentageCorrect(),
                finalSession.getNumCorrect(), finalSession.getNumQuestions());

            ConsoleFormatter.printReview(reviewItems);

        } catch (QuizEngineException e) {
            ConsoleFormatter.printError(e.getMessage());
        }
    }
}
