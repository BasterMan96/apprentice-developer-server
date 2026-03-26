package com.bytik.apprentice.service

import com.bytik.apprentice.dto.AchievementDto
import com.bytik.apprentice.entity.AchievementCondition
import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.entity.UserAchievement
import com.bytik.apprentice.repository.AchievementRepository
import com.bytik.apprentice.repository.UserAchievementRepository
import com.bytik.apprentice.repository.UserCodeSubmissionRepository
import com.bytik.apprentice.repository.UserCourseEnrollmentRepository
import com.bytik.apprentice.repository.UserProgressRepository
import com.bytik.apprentice.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class GamificationService(
    private val achievementRepository: AchievementRepository,
    private val userAchievementRepository: UserAchievementRepository,
    private val userRepository: UserRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userCourseEnrollmentRepository: UserCourseEnrollmentRepository,
    private val userCodeSubmissionRepository: UserCodeSubmissionRepository
) {

    @Transactional
    fun checkAndAwardAchievements(userId: Long): List<AchievementDto> {
        val user = userRepository.findById(userId).orElse(null) ?: return emptyList()
        val allAchievements = achievementRepository.findAll()
        val earnedIds = userAchievementRepository.findByUserId(userId).map { it.achievement.id }.toSet()
        val newlyUnlocked = mutableListOf<AchievementDto>()

        for (achievement in allAchievements) {
            if (achievement.id in earnedIds) continue

            val conditionMet = when (achievement.conditionType) {
                AchievementCondition.LESSONS_COMPLETED -> {
                    userProgressRepository.countByUserIdAndStatus(userId, ProgressStatus.COMPLETED) >= achievement.conditionValue
                }
                AchievementCondition.COURSE_FINISHED -> {
                    val completed = userCourseEnrollmentRepository.findByUserId(userId)
                        .count { it.completedAt != null }
                    completed >= achievement.conditionValue
                }
                AchievementCondition.XP_EARNED -> {
                    user.xp >= achievement.conditionValue
                }
                AchievementCondition.BYTES_EARNED -> {
                    user.bytesBalance >= achievement.conditionValue
                }
                AchievementCondition.CODE_TASKS_COMPLETED -> {
                    userCodeSubmissionRepository.countByUserIdAndIsCorrectTrue(userId) >= achievement.conditionValue
                }
                AchievementCondition.QUIZZES_PERFECT -> {
                    val perfectCount = userProgressRepository.findByUserId(userId)
                        .count { it.score == 100 && it.status == ProgressStatus.COMPLETED }
                    perfectCount >= achievement.conditionValue
                }
                AchievementCondition.ACHIEVEMENTS_COUNT -> {
                    earnedIds.size >= achievement.conditionValue
                }
                AchievementCondition.STREAK -> {
                    // streak not implemented yet
                    false
                }
            }

            if (conditionMet) {
                val userAchievement = UserAchievement(
                    user = user,
                    achievement = achievement,
                    earnedAt = LocalDateTime.now()
                )
                userAchievementRepository.save(userAchievement)
                newlyUnlocked.add(
                    AchievementDto(
                        id = achievement.id,
                        title = achievement.title,
                        description = achievement.description,
                        iconUrl = achievement.iconUrl,
                        earnedAt = userAchievement.earnedAt
                    )
                )
            }
        }

        return newlyUnlocked
    }
}
