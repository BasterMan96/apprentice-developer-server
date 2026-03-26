package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.UserCourseEnrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserCourseEnrollmentRepository : JpaRepository<UserCourseEnrollment, Long> {
    fun findByUserIdAndCourseId(userId: Long, courseId: Long): UserCourseEnrollment?
    fun findByUserId(userId: Long): List<UserCourseEnrollment>
}
