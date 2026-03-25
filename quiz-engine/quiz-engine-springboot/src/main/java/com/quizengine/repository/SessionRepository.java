package com.quizengine.repository;

import com.quizengine.entity.QuizSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<QuizSession, String> {
    List<QuizSession> findAllByOrderByStartedAtDesc();
    Page<QuizSession> findAllByOrderByStartedAtDesc(Pageable pageable);
    List<QuizSession> findByStartedAtAfter(LocalDateTime since);
}
