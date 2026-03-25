package com.quizengine.repository;

import com.quizengine.entity.QuizResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<QuizResponse, Long> {
    List<QuizResponse> findBySessionSessionId(String sessionId);
    List<QuizResponse> findByQuestionIdAndIsCorrect(Long questionId, Integer isCorrect);
}
