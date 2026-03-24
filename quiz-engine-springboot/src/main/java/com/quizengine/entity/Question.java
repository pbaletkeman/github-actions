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
@Table(name = "questions", indexes = {
    @Index(name = "idx_section", columnList = "section"),
    @Index(name = "idx_difficulty", columnList = "difficulty"),
    @Index(name = "idx_usage_cycle", columnList = "usage_cycle")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionA;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionB;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionC;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionD;

    @Column(columnDefinition = "TEXT")
    private String optionE;

    @Column(nullable = false)
    private String correctAnswer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column
    private String section;

    @Column
    private String difficulty;

    @Column
    private String sourceFile;

    @Column(name = "usage_cycle", nullable = false)
    @Builder.Default
    private Integer usageCycle = 1;

    @Column(nullable = false)
    @Builder.Default
    private Integer timesUsed = 0;

    @Column
    private LocalDateTime lastUsedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<QuizResponse> responses;
}
