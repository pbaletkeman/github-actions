package com.quizengine.repository;

import com.quizengine.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT MIN(q.usageCycle) FROM Question q")
    Integer findCurrentCycle();

    @Query("SELECT q FROM Question q WHERE q.usageCycle = :cycle")
    List<Question> findByUsageCycle(@Param("cycle") Integer cycle);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Question q SET q.timesUsed = q.timesUsed + 1, q.lastUsedAt = CURRENT_TIMESTAMP WHERE q.id = :id")
    void markQuestionUsed(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Question q SET q.usageCycle = q.usageCycle + 1 WHERE q.timesUsed > 0 AND q.usageCycle = (SELECT MIN(q2.usageCycle) FROM Question q2)")
    void advanceCycle();

    @Query("SELECT COUNT(q) FROM Question q WHERE q.usageCycle = (SELECT MIN(q2.usageCycle) FROM Question q2) AND q.timesUsed = 0")
    long countUnusedInCurrentCycle();

    long countBySection(String section);
}
