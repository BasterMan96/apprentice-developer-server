package com.bytik.apprentice.service

import com.bytik.apprentice.dto.AchievementDto
import com.bytik.apprentice.dto.CertificateDto
import com.bytik.apprentice.dto.CourseListDto
import com.bytik.apprentice.dto.EnrollmentDto
import com.bytik.apprentice.dto.PortfolioItemDto
import com.bytik.apprentice.dto.UserProfileDto
import com.bytik.apprentice.dto.UserStatsDto
import com.bytik.apprentice.dto.toDto
import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.exception.ApiException
import com.bytik.apprentice.repository.AchievementRepository
import com.bytik.apprentice.repository.CertificateRepository
import com.bytik.apprentice.repository.ModuleRepository
import com.bytik.apprentice.repository.UserAchievementRepository
import com.bytik.apprentice.repository.UserCodeSubmissionRepository
import com.bytik.apprentice.repository.UserCourseEnrollmentRepository
import com.bytik.apprentice.repository.UserProgressRepository
import com.bytik.apprentice.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userCourseEnrollmentRepository: UserCourseEnrollmentRepository,
    private val userAchievementRepository: UserAchievementRepository,
    private val achievementRepository: AchievementRepository,
    private val certificateRepository: CertificateRepository,
    private val userCodeSubmissionRepository: UserCodeSubmissionRepository,
    private val moduleRepository: ModuleRepository
) {

    @Transactional(readOnly = true)
    fun getProfile(userId: Long): UserProfileDto {
        val user = userRepository.findById(userId).orElseThrow {
            ApiException("User not found", HttpStatus.NOT_FOUND)
        }

        val lessonsCompleted = userProgressRepository.countByUserIdAndStatus(userId, ProgressStatus.COMPLETED)
        val coursesCompleted = userCourseEnrollmentRepository.findByUserId(userId)
            .count { it.completedAt != null }

        val stats = UserStatsDto(
            totalXp = user.xp,
            level = user.level,
            bytesBalance = user.bytesBalance,
            coursesCompleted = coursesCompleted,
            lessonsCompleted = lessonsCompleted,
            streak = 0
        )

        return UserProfileDto(user = user.toDto(), stats = stats)
    }

    @Transactional(readOnly = true)
    fun getMyCourses(userId: Long): List<EnrollmentDto> {
        return userCourseEnrollmentRepository.findByUserId(userId).map { enrollment ->
            val course = enrollment.course
            val modules = moduleRepository.findByCourseIdOrderByOrderIndex(course.id)
            val lessonCount = modules.sumOf { it.lessons.size }

            val courseListDto = CourseListDto(
                id = course.id,
                title = course.title,
                description = course.description,
                imageUrl = course.imageUrl,
                difficulty = course.difficulty,
                ageFrom = course.ageFrom,
                ageTo = course.ageTo,
                modulesCount = modules.size,
                lessonsCount = lessonCount,
                totalBytesReward = course.totalBytesReward
            )

            EnrollmentDto(
                id = enrollment.id,
                course = courseListDto,
                progressPercent = enrollment.progressPercent,
                enrolledAt = enrollment.enrolledAt,
                completedAt = enrollment.completedAt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getAchievements(userId: Long): List<AchievementDto> {
        val earnedMap = userAchievementRepository.findByUserId(userId)
            .associateBy { it.achievement.id }
        val allAchievements = achievementRepository.findAll()

        return allAchievements.map { achievement ->
            val earned = earnedMap[achievement.id]
            AchievementDto(
                id = achievement.id,
                title = achievement.title,
                description = achievement.description,
                iconUrl = achievement.iconUrl,
                earnedAt = earned?.earnedAt
            )
        }
    }

    @Transactional(readOnly = true)
    fun getCertificates(userId: Long): List<CertificateDto> {
        return certificateRepository.findByUserId(userId).map { cert ->
            CertificateDto(
                id = cert.id,
                courseTitle = cert.course.title,
                issuedAt = cert.issuedAt,
                certificateNumber = cert.certificateNumber
            )
        }
    }

    @Transactional(readOnly = true)
    fun getPortfolio(userId: Long): List<PortfolioItemDto> {
        return userCodeSubmissionRepository.findByUserIdAndIsCorrectTrue(userId).map { submission ->
            val task = submission.codeTask
            val lesson = task.lesson
            val course = lesson.module.course
            PortfolioItemDto(
                id = submission.id,
                submittedCode = submission.submittedCode,
                output = submission.output,
                taskDescription = task.description,
                lessonTitle = lesson.title,
                courseTitle = course.title,
                submittedAt = submission.submittedAt
            )
        }
    }
}
