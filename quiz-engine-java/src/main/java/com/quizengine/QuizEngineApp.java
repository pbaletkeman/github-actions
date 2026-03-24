package com.quizengine;

import com.quizengine.cli.QuizCLI;
import com.quizengine.dao.DatabaseManager;

import java.sql.Connection;

public class QuizEngineApp {
    public static void main(String[] args) {
        String dbPath = System.getProperty("quiz.db", "quiz.db");
        try (DatabaseManager dbManager = DatabaseManager.create(dbPath)) {
            dbManager.initSchema();
            try (Connection connection = dbManager.getConnection()) {
                QuizCLI cli = new QuizCLI(connection);
                cli.run(args);
            }
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            System.exit(1);
        }
    }
}
