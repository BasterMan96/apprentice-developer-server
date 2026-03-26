package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.QuizQuestion
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizQuestionRepository : JpaRepository<QuizQuestion, Long> {
    fun findByLessonIdOrderByOrderIndex(lessonId: Long): List<QuizQuestion>
}
