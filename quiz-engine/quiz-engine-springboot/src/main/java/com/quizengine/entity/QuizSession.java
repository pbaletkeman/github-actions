package com.quizengine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_sessions", indexes = {
    @Index(name = "idx_started_date", columnList = "started_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSession {

    @Id
    @Column(length = 36)
    private String sessionId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime endedAt;

    @Column(nullable = false)
    private Integer numQuestions;

    @Column
    @Builder.Default
    private Integer numCorrect = 0;

    @Column
    @Builder.Default
    private Double percentageCorrect = 0.0;

    @Column
    private Integer timeTakenSeconds;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<QuizResponse> responses;
}
