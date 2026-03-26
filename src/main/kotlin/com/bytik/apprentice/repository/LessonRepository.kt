package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.Lesson
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : JpaRepository<Lesson, Long> {
    fun findByModuleCourseId(courseId: Long): List<Lesson>
}
