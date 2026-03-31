package com.quizengine.cli;

import com.quizengine.repository.QuestionRepository;
import com.quizengine.repository.ResponseRepository;
import com.quizengine.repository.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Scanner;

@Component
@Command(name = "clear", description = "Clear questions or history", mixinStandardHelpOptions = true)
public class ClearCommand implements Runnable {

    @Option(names = {"--questions"}, description = "Clear all questions")
    private boolean clearQuestions;

    @Option(names = {"--history"}, description = "Clear quiz history")
    private boolean clearHistory;

    @Option(names = {"--all"}, description = "Clear all questions and history")
    private boolean clearAll;

    @Option(names = {"--confirm"}, description = "Skip confirmation prompt")
    private boolean confirm;

    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;
    private final ResponseRepository responseRepository;

    public ClearCommand(QuestionRepository questionRepository,
                        SessionRepository sessionRepository,
                        ResponseRepository responseRepository) {
        this.questionRepository = questionRepository;
        this.sessionRepository = sessionRepository;
        this.responseRepository = responseRepository;
    }

    @Override
    @Transactional
    public void run() {
        if (!clearQuestions && !clearHistory && !clearAll) {
            ConsoleFormatter.printError("Please specify --questions, --history, or --all");
            return;
        }

        if (!confirm) {
            System.out.print("Are you sure? This cannot be undone. (yes/no): ");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("yes")) {
                ConsoleFormatter.printInfo("Cancelled.");
                return;
            }
        }

        boolean doHistory = clearHistory || clearAll;
        boolean doQuestions = clearQuestions || clearAll;

        if (doHistory) {
            responseRepository.deleteAll();
            sessionRepository.deleteAll();
            ConsoleFormatter.printSuccess("Quiz history cleared.");
        }

        if (doQuestions) {
            questionRepository.deleteAll();
            ConsoleFormatter.printSuccess("All questions cleared.");
        }
    }
}
