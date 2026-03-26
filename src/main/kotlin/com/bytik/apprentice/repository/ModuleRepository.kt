package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.Module
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ModuleRepository : JpaRepository<Module, Long> {
    fun findByCourseIdOrderByOrderIndex(courseId: Long): List<Module>
}
