package com.bytik.apprentice.service

import com.bytik.apprentice.dto.CodeTaskDto
import com.bytik.apprentice.dto.LessonCompleteRequest
import com.bytik.apprentice.dto.LessonCompleteResponse
import com.bytik.apprentice.dto.LessonDetailDto
import com.bytik.apprentice.dto.QuizOptionDto
import com.bytik.apprentice.dto.QuizQuestionDto
import com.bytik.apprentice.dto.UserProgressDto
import com.bytik.apprentice.entity.LessonType
import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.entity.UserCodeSubmission
import com.bytik.apprentice.entity.UserProgress
import com.bytik.apprentice.exception.ApiException
import com.bytik.apprentice.repository.CodeTaskRepository
import com.bytik.apprentice.repository.LessonRepository
import com.bytik.apprentice.repository.QuizQuestionRepository
import com.bytik.apprentice.repository.UserCourseEnrollmentRepository
import com.bytik.apprentice.repository.UserCodeSubmissionRepository
import com.bytik.apprentice.repository.UserProgressRepository
import com.bytik.apprentice.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.math.floor
import kotlin.math.sqrt

@Service
class LessonService(
    private val lessonRepository: LessonRepository,
    private val quizQuestionRepository: QuizQuestionRepository,
    private val codeTaskRepository: CodeTaskRepository,
    private val userRepository: UserRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userCourseEnrollmentRepository: UserCourseEnrollmentRepository,
    private val userCodeSubmissionRepository: UserCodeSubmissionRepository,
    private val codeExecutionService: CodeExecutionService,
    private val gamificationService: GamificationService
) {

    @Transactional(readOnly = true)
    fun getLesson(lessonId: Long, userId: Long): LessonDetailDto {
        val lesson = lessonRepository.findById(lessonId).orElseThrow {
            ApiException("Lesson not found", HttpStatus.NOT_FOUND)
        }

        val questions = quizQuestionRepository.findByLessonIdOrderByOrderIndex(lessonId).map { question ->
            QuizQuestionDto(
                id = question.id,
                questionText = question.questionText,
                questionType = question.questionType,
                orderIndex = question.orderIndex,
                options = question.options.map { option ->
                    QuizOptionDto(
                        id = option.id,
                        optionText = option.optionText
                        // isCorrect intentionally omitted
                    )
                }
            )
        }

        val codeTasks = codeTaskRepository.findByLessonId(lessonId).map { task ->
            CodeTaskDto(
                id = task.id,
                description = task.description,
                initialCode = task.initialCode,
                hint = task.hint
                // expectedOutput intentionally omitted
            )
        }

        val progress = userProgressRepository.findByUserIdAndLessonId(userId, lessonId)?.let {
            UserProgressDto(
                status = it.status,
                score = it.score,
                completedAt = it.completedAt
            )
        }

        return LessonDetailDto(
            id = lesson.id,
            title = lesson.title,
            content = lesson.content,
            lessonType = lesson.lessonType,
            xpReward = lesson.xpReward,
            bytesReward = lesson.bytesReward,
            quizQuestions = questions,
            codeTasks = codeTasks,
            progress = progress
        )
    }

    @Transactional
    fun completeLesson(lessonId: Long, userId: Long, request: LessonCompleteRequest): LessonCompleteResponse {
        val lesson = lessonRepository.findById(lessonId).orElseThrow {
            ApiException("Lesson not found", HttpStatus.NOT_FOUND)
        }
        val user = userRepository.findById(userId).orElseThrow {
            ApiException("User not found", HttpStatus.NOT_FOUND)
        }

        val score = when (lesson.lessonType) {
            LessonType.THEORY -> 100

            LessonType.QUIZ -> {
                val answers = request.quizAnswers ?: emptyList()
                val questions = quizQuestionRepository.findByLessonIdOrderByOrderIndex(lessonId)
                if (questions.isEmpty()) 100
                else {
                    var correctCount = 0
                    for (question in questions) {
                        val answer = answers.find { it.questionId == question.id }
                        val correctOptionIds = question.options.filter { it.isCorrect }.map { it.id }.toSet()
                        val selectedIds = answer?.selectedOptionIds?.toSet() ?: emptySet()
                        if (selectedIds == correctOptionIds) correctCount++
                    }
                    (correctCount * 100) / questions.size
                }
            }

            LessonType.PRACTICE, LessonType.PROJECT -> {
                val code = request.code
                if (code == null) {
                    0
                } else {
                    val codeTasks = codeTaskRepository.findByLessonId(lessonId)
                    if (codeTasks.isEmpty()) {
                        100
                    } else {
                        val task = codeTasks.first()
                        val result = codeExecutionService.runCode(code)
                        val actualOutput = result.output.trim()
                        val expectedOutput = task.expectedOutput.trim()
                        val isCorrect = actualOutput == expectedOutput

                        val submission = UserCodeSubmission(
                            user = user,
                            codeTask = task,
                            submittedCode = code,
                            output = result.output,
                            isCorrect = isCorrect,
                            submittedAt = LocalDateTime.now()
                        )
                        userCodeSubmissionRepository.save(submission)

                        if (isCorrect) 100 else 0
                    }
                }
            }
        }

        val xpMultiplier = if (score == 100) 2 else 1
        val xpEarned = lesson.xpReward * xpMultiplier
        val bytesEarned = if (score >= 50) lesson.bytesReward else 0

        val oldLevel = user.level
        user.xp += xpEarned
        user.bytesBalance += bytesEarned
        val newLevel = floor(sqrt(user.xp / 100.0)).toInt() + 1
        user.level = newLevel
        userRepository.save(user)

        val existing = userProgressRepository.findByUserIdAndLessonId(userId, lessonId)
        if (existing != null) {
            if (score > existing.score) {
                existing.score = score
                existing.status = ProgressStatus.COMPLETED
                existing.completedAt = LocalDateTime.now()
                userProgressRepository.save(existing)
            }
        } else {
            val progress = UserProgress(
                user = user,
                lesson = lesson,
                status = ProgressStatus.COMPLETED,
                score = score,
                completedAt = LocalDateTime.now()
            )
            userProgressRepository.save(progress)
        }

        updateCourseProgress(userId, lesson.module.course.id)

        val unlockedAchievements = gamificationService.checkAndAwardAchievements(userId)

        return LessonCompleteResponse(
            score = score,
            xpEarned = xpEarned,
            bytesEarned = bytesEarned,
            newLevel = if (newLevel > oldLevel) newLevel else null,
            achievementsUnlocked = unlockedAchievements
        )
    }

    private fun updateCourseProgress(userId: Long, courseId: Long) {
        val enrollment = userCourseEnrollmentRepository.findByUserIdAndCourseId(userId, courseId) ?: return
        val allLessons = lessonRepository.findByModuleCourseId(courseId)
        if (allLessons.isEmpty()) return

        val completedLessonIds = userProgressRepository.findByUserId(userId)
            .filter { it.status == ProgressStatus.COMPLETED }
            .map { it.lesson.id }
            .toSet()

        val completedCount = allLessons.count { it.id in completedLessonIds }
        enrollment.progressPercent = (completedCount * 100) / allLessons.size

        if (enrollment.progressPercent == 100 && enrollment.completedAt == null) {
            enrollment.completedAt = LocalDateTime.now()
        }

        userCourseEnrollmentRepository.save(enrollment)
    }
}
