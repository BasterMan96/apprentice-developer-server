package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.Certificate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CertificateRepository : JpaRepository<Certificate, Long> {
    fun findByUserId(userId: Long): List<Certificate>
    fun existsByUserIdAndCourseId(userId: Long, courseId: Long): Boolean
}
