package com.bytik.apprentice.dto

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class LessonCompleteRequest(
    val quizAnswers: List<QuizAnswerDto>? = null,
    val code: String? = null
)

data class QuizAnswerDto(
    val questionId: Long,
    val selectedOptionIds: List<Long>
)

data class LessonCompleteResponse(
    val score: Int,
    val xpEarned: Int,
    val bytesEarned: Int,
    val newLevel: Int?,
    val achievementsUnlocked: List<AchievementDto>
)

data class AchievementDto(
    val id: Long,
    val title: String,
    val description: String,
    val iconUrl: String,
    val earnedAt: LocalDateTime? = null
)

data class CodeRunRequest(
    @field:NotBlank val code: String,
    val language: String = "python"
)

data class CodeRunResponse(
    val output: String,
    val error: String? = null,
    val executionTimeMs: Long
)

data class UserStatsDto(
    val totalXp: Int,
    val level: Int,
    val bytesBalance: Int,
    val coursesCompleted: Int,
    val lessonsCompleted: Long,
    val streak: Int
)

data class UserProfileDto(
    val user: UserDto,
    val stats: UserStatsDto
)

data class EnrollmentDto(
    val id: Long,
    val course: CourseListDto,
    val progressPercent: Int,
    val enrolledAt: LocalDateTime,
    val completedAt: LocalDateTime?
)

data class CertificateDto(
    val id: Long,
    val courseTitle: String,
    val issuedAt: LocalDateTime,
    val certificateNumber: String
)

data class PortfolioItemDto(
    val id: Long,
    val submittedCode: String,
    val output: String,
    val taskDescription: String,
    val lessonTitle: String,
    val courseTitle: String,
    val submittedAt: LocalDateTime
)

data class LinkChildRequest(
    @field:NotBlank val childLogin: String
)

data class ChildProgressDto(
    val child: UserDto,
    val stats: UserStatsDto,
    val recentCourses: List<EnrollmentDto>
)
