package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : JpaRepository<Course, Long> {
    fun findAllByOrderByOrderIndex(): List<Course>
    fun findByTitleContainingIgnoreCase(search: String): List<Course>
}
