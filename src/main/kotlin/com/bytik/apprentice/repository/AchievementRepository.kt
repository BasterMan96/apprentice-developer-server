package com.bytik.apprentice.repository

import com.bytik.apprentice.entity.Achievement
import com.bytik.apprentice.entity.AchievementCondition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AchievementRepository : JpaRepository<Achievement, Long> {
    fun findByConditionType(conditionType: AchievementCondition): List<Achievement>
}
