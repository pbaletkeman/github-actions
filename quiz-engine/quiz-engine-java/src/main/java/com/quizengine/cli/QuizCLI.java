package com.quizengine.cli;

import com.quizengine.dao.QuestionDAO;
import com.quizengine.dao.SessionDAO;
import com.quizengine.dao.ResponseDAO;
import com.quizengine.model.Question;
import com.quizengine.model.QuizSession;
import com.quizengine.service.HistoryService;
import com.quizengine.service.QuizEngine;
import com.quizengine.service.QuizService;
import com.quizengine.util.MarkdownParser;
import com.quizengine.util.ShuffleResult;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizCLI {
    private final Scanner scanner;
    private final Connection connection;

    public QuizCLI(Connection connection) {
        this.connection = connection;
        this.scanner = new Scanner(System.in);
    }

    public void run(String[] args) {
        String command = args.length > 0 ? args[0].toLowerCase() : "quiz";
        try {
            switch (command) {
                case "quiz":
                    runQuiz();
                    break;
                case "import":
                    runImport(args);
                    break;
                case "history":
                    runHistory(args);
                    break;
                case "clear":
                    runClear(args);
                    break;
                case "help":
                default:
                    printHelp();
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void runQuiz() throws Exception {
        ConsoleFormatter.printBanner();
        int numQuestions = promptNumQuestions();
        QuizService quizService = new QuizService(connection);
        QuizEngine engine = quizService.createQuizEngine(numQuestions);
        engine.loadQuestions();

        List<Question> questions = engine.getQuestions();
        if (questions.isEmpty()) {
            System.out.println("No questions available. Import questions first with: import <file.md>");
            return;
        }

        List<Question> reviewQuestions = new ArrayList<>();
        List<ShuffleResult> reviewShuffles = new ArrayList<>();
        List<String> reviewAnswers = new ArrayList<>();
        List<Boolean> reviewCorrects = new ArrayList<>();
        long questionStart;
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            ShuffleResult shuffle = engine.getShuffleResult(i);
            ConsoleFormatter.printQuestion(i + 1, questions.size(), q, shuffle);
            questionStart = System.currentTimeMillis();
            String answer = getValidatedAnswer();
            int timeTaken = (int) ((System.currentTimeMillis() - questionStart) / 1000);
            boolean correct = engine.submitAnswer(i, answer, timeTaken);
            reviewQuestions.add(q);
            reviewShuffles.add(shuffle);
            reviewAnswers.add(answer);
            reviewCorrects.add(correct);
        }

        QuizSession session = engine.complete();
        ConsoleFormatter.printReview(reviewQuestions, reviewShuffles, reviewAnswers, reviewCorrects);
        ConsoleFormatter.printResults(session);
    }

    private void runImport(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: import <filepath.md>");
            return;
        }
        String filePath = args[1];
        List<Question> questions = MarkdownParser.parseFile(filePath);
        QuestionDAO dao = new QuestionDAO(connection);
        int imported = 0;
        for (Question q : questions) {
            dao.insert(q);
            imported++;
        }
        System.out.printf("Imported %d questions from %s%n", imported, filePath);
    }

    private void runHistory(String[] args) throws Exception {
        HistoryService historyService = new HistoryService(connection);
        List<QuizSession> sessions = historyService.getAllSessions();

        if (args.length > 1 && args[1].equals("--json")) {
            System.out.println(historyService.exportToJson(sessions));
        } else if (args.length > 1 && args[1].equals("--csv")) {
            System.out.println(historyService.exportToCsv(sessions));
        } else {
            ConsoleFormatter.printSessionHistory(sessions);
        }
    }

    private void runClear(String[] args) throws Exception {
        System.out.print("Are you sure you want to clear all history? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) {
            SessionDAO sessionDAO = new SessionDAO(connection);
            ResponseDAO responseDAO = new ResponseDAO(connection);
            List<QuizSession> sessions = sessionDAO.getAll();
            for (QuizSession s : sessions) {
                responseDAO.deleteBySessionId(s.getSessionId());
            }
            sessionDAO.deleteAll();
            System.out.println("All history cleared.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private void printHelp() {
        ConsoleFormatter.printBanner();
        System.out.println("Usage: java -jar quiz-engine.jar [command]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  quiz              Start an interactive quiz (default)");
        System.out.println("  import <file.md>  Import questions from markdown file");
        System.out.println("  history           Show quiz history");
        System.out.println("  history --json    Export history as JSON");
        System.out.println("  history --csv     Export history as CSV");
        System.out.println("  clear             Clear all quiz history");
        System.out.println("  help              Show this help message");
    }

    private int promptNumQuestions() {
        System.out.print("How many questions? [default: 10]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return 10;
        try {
            int n = Integer.parseInt(input);
            return n > 0 ? n : 10;
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    private String getValidatedAnswer() {
        while (true) {
            System.out.print("Your answer (A/B/C/D/E): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.matches("[A-E]")) return input;
            System.out.println("Please enter A, B, C, D, or E.");
        }
    }
}
