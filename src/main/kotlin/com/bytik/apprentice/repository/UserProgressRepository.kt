package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.entity.UserProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProgressRepository : JpaRepository<UserProgress, Long> {
    fun findByUserIdAndLessonId(userId: Long, lessonId: Long): UserProgress?
    fun findByUserId(userId: Long): List<UserProgress>
    fun countByUserIdAndStatus(userId: Long, status: ProgressStatus): Long
}
