package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.LessonComment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonCommentRepository : JpaRepository<LessonComment, Long> {
    fun findByLessonIdOrderByCreatedAtDesc(lessonId: Long): List<LessonComment>
    fun countByLessonId(lessonId: Long): Long
}
