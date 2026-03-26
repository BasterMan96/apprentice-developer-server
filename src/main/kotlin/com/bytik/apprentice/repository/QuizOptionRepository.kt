package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.QuizOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizOptionRepository : JpaRepository<QuizOption, Long> {
    fun findByQuestionId(questionId: Long): List<QuizOption>
}
