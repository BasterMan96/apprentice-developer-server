package com.bytik.apprentice.dto

import com.bytik.apprentice.entity.LessonType
import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.entity.QuestionType
import java.time.LocalDateTime

data class LessonDetailDto(
    val id: Long,
    val title: String,
    val content: String,
    val lessonType: LessonType,
    val xpReward: Int,
    val bytesReward: Int,
    val quizQuestions: List<QuizQuestionDto>,
    val codeTasks: List<CodeTaskDto>,
    val progress: UserProgressDto?
)

data class QuizQuestionDto(
    val id: Long,
    val questionText: String,
    val questionType: QuestionType,
    val orderIndex: Int,
    val options: List<QuizOptionDto>
)

data class QuizOptionDto(
    val id: Long,
    val optionText: String
    // isCorrect NOT included — client shouldn't see correct answers
)

data class CodeTaskDto(
    val id: Long,
    val description: String,
    val initialCode: String,
    val hint: String?
    // expectedOutput NOT included
)

data class UserProgressDto(
    val status: ProgressStatus,
    val score: Int,
    val completedAt: LocalDateTime?
)
