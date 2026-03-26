package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.UserCodeSubmission
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserCodeSubmissionRepository : JpaRepository<UserCodeSubmission, Long> {
    fun findByUserId(userId: Long): List<UserCodeSubmission>
    fun findByUserIdAndIsCorrectTrue(userId: Long): List<UserCodeSubmission>
    fun countByUserIdAndIsCorrectTrue(userId: Long): Long
}
