package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): User?
    fun findByParentId(parentId: Long): List<User>
}
