package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.CodeTask
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CodeTaskRepository : JpaRepository<CodeTask, Long> {
    fun findByLessonId(lessonId: Long): List<CodeTask>
}
