package com.quizengine.service;

import com.quizengine.entity.Question;
import com.quizengine.repository.QuestionRepository;
import com.quizengine.util.MarkdownParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportService {

    private final QuestionRepository questionRepository;

    public ImportService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Transactional
    public ImportResult importFromFile(Path filePath) throws IOException {
        List<Question> questions = MarkdownParser.parseFile(filePath);
        return saveQuestions(questions);
    }

    @Transactional
    public ImportResult importFromContent(String content, String sourceName) {
        List<Question> questions = MarkdownParser.parseContent(content, sourceName);
        return saveQuestions(questions);
    }

    @Transactional
    public ImportResult importFromDirectory(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Path is not a directory: " + directory);
        }

        List<Question> allQuestions = new ArrayList<>();
        try (var files = Files.walk(directory, 1)) {
            files.filter(p -> p.toString().endsWith(".md"))
                .forEach(f -> {
                    try {
                        allQuestions.addAll(MarkdownParser.parseFile(f));
                    } catch (IOException e) {
                        // Log and continue with other files
                    }
                });
        }

        return saveQuestions(allQuestions);
    }

    private ImportResult saveQuestions(List<Question> questions) {
        if (questions.isEmpty()) {
            return new ImportResult(0, 0, "No questions found to import");
        }

        int saved = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();

        for (Question question : questions) {
            try {
                questionRepository.save(question);
                saved++;
            } catch (Exception e) {
                skipped++;
                errors.add("Skipped: " + question.getQuestionText().substring(0,
                    Math.min(50, question.getQuestionText().length())) + "...");
            }
        }

        String message = String.format("Imported %d questions, skipped %d", saved, skipped);
        return new ImportResult(saved, skipped, message);
    }

    public record ImportResult(int imported, int skipped, String message) {}
}
