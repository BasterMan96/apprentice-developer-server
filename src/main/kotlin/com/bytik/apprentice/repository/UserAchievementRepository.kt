package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.UserAchievement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAchievementRepository : JpaRepository<UserAchievement, Long> {
    fun findByUserId(userId: Long): List<UserAchievement>
    fun existsByUserIdAndAchievementId(userId: Long, achievementId: Long): Boolean
}
