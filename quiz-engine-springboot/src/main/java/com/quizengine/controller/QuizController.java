package com.quizengine.controller;

import com.quizengine.entity.Question;
import com.quizengine.entity.QuizSession;
import com.quizengine.exception.QuizEngineException;
import com.quizengine.repository.QuestionRepository;
import com.quizengine.service.HistoryService;
import com.quizengine.service.ImportService;
import com.quizengine.service.QuizService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class QuizController {

    private final QuizService quizService;
    private final HistoryService historyService;
    private final ImportService importService;
    private final QuestionRepository questionRepository;

    public QuizController(QuizService quizService,
                          HistoryService historyService,
                          ImportService importService,
                          QuestionRepository questionRepository) {
        this.quizService = quizService;
        this.historyService = historyService;
        this.importService = importService;
        this.questionRepository = questionRepository;
    }

    @GetMapping
    public String index(Model model) {
        long totalQuestions = questionRepository.count();
        HistoryService.SessionSummary summary = historyService.getSummary();
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("summary", summary);
        return "index";
    }

    @GetMapping("/quiz")
    public String quizPage(Model model) {
        long totalQuestions = questionRepository.count();
        model.addAttribute("totalQuestions", totalQuestions);
        return "quiz";
    }

    @GetMapping("/history")
    public String historyPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<QuizSession> sessions = historyService.getSessionsPaginated(page, size);
        model.addAttribute("sessions", sessions);
        model.addAttribute("currentPage", page);
        return "history";
    }

    @GetMapping("/api/questions/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getQuestionCount() {
        long count = questionRepository.count();
        Integer currentCycle = questionRepository.findCurrentCycle();
        Map<String, Object> result = new HashMap<>();
        result.put("total", count);
        result.put("currentCycle", currentCycle != null ? currentCycle : 1);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/quiz/start")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> startQuiz(@RequestBody Map<String, Object> body) {
        int numQuestions = body.containsKey("numQuestions")
            ? ((Number) body.get("numQuestions")).intValue() : 10;

        try {
            QuizSession session = quizService.startQuiz(numQuestions);
            List<Question> questions = quizService.getSessionQuestions(session.getSessionId());

            Map<String, Object> result = new HashMap<>();
            result.put("sessionId", session.getSessionId());
            result.put("numQuestions", session.getNumQuestions());
            result.put("questions", questions.stream().map(this::toQuestionDto).toList());
            return ResponseEntity.ok(result);
        } catch (QuizEngineException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/quiz/{sessionId}/answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @PathVariable String sessionId,
            @RequestBody Map<String, Object> body) {
        int questionIndex = ((Number) body.getOrDefault("questionIndex", 0)).intValue();
        String userAnswer = (String) body.getOrDefault("answer", "");
        int timeTaken = ((Number) body.getOrDefault("timeTaken", 0)).intValue();

        try {
            QuizService.SubmitResult result = quizService.submitAnswer(
                sessionId, questionIndex, userAnswer, timeTaken);

            Map<String, Object> response = new HashMap<>();
            response.put("correct", result.isCorrect());
            response.put("correctAnswer", result.correctAnswer());
            response.put("explanation", result.explanation());
            return ResponseEntity.ok(response);
        } catch (QuizEngineException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/quiz/{sessionId}/finalize")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> finalizeQuiz(@PathVariable String sessionId) {
        try {
            QuizSession session = quizService.finalizeQuiz(sessionId);
            Map<String, Object> result = new HashMap<>();
            result.put("sessionId", session.getSessionId());
            result.put("numCorrect", session.getNumCorrect());
            result.put("numQuestions", session.getNumQuestions());
            result.put("percentageCorrect", session.getPercentageCorrect());
            result.put("timeTakenSeconds", session.getTimeTakenSeconds());
            return ResponseEntity.ok(result);
        } catch (QuizEngineException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/api/history")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<QuizSession> sessions = historyService.getSessionsPaginated(page, size);
        Map<String, Object> result = new HashMap<>();
        result.put("sessions", sessions.getContent().stream().map(this::toSessionDto).toList());
        result.put("totalPages", sessions.getTotalPages());
        result.put("currentPage", page);
        result.put("totalSessions", sessions.getTotalElements());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/history/{sessionId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSessionDetails(@PathVariable String sessionId) {
        try {
            QuizSession session = historyService.getSessionDetails(sessionId);
            return ResponseEntity.ok(toSessionDto(session));
        } catch (QuizEngineException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/import")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> importQuestions(@RequestBody Map<String, String> body) {
        String content = body.getOrDefault("content", "");
        String sourceName = body.getOrDefault("source", "api-import");

        ImportService.ImportResult result = importService.importFromContent(content, sourceName);
        return ResponseEntity.ok(Map.of(
            "imported", result.imported(),
            "skipped", result.skipped(),
            "message", result.message()
        ));
    }

    private Map<String, Object> toQuestionDto(Question q) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", q.getId());
        dto.put("questionText", q.getQuestionText());
        dto.put("optionA", q.getOptionA());
        dto.put("optionB", q.getOptionB());
        dto.put("optionC", q.getOptionC());
        dto.put("optionD", q.getOptionD());
        if (q.getOptionE() != null) dto.put("optionE", q.getOptionE());
        dto.put("section", q.getSection());
        dto.put("difficulty", q.getDifficulty());
        return dto;
    }

    private Map<String, Object> toSessionDto(QuizSession s) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("sessionId", s.getSessionId());
        dto.put("startedAt", s.getStartedAt());
        dto.put("endedAt", s.getEndedAt());
        dto.put("numQuestions", s.getNumQuestions());
        dto.put("numCorrect", s.getNumCorrect());
        dto.put("percentageCorrect", s.getPercentageCorrect());
        dto.put("timeTakenSeconds", s.getTimeTakenSeconds());
        return dto;
    }
}
