package com.bytik.apprentice.dto

import com.bytik.apprentice.entity.Difficulty
import com.bytik.apprentice.entity.LessonType

data class CourseListDto(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val difficulty: Difficulty,
    val ageFrom: Int,
    val ageTo: Int,
    val modulesCount: Int,
    val lessonsCount: Int,
    val totalBytesReward: Int
)

data class CourseDetailDto(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val difficulty: Difficulty,
    val ageFrom: Int,
    val ageTo: Int,
    val totalBytesReward: Int,
    val modules: List<ModuleDto>,
    val enrolled: Boolean,
    val progressPercent: Int
)

data class ModuleDto(
    val id: Long,
    val title: String,
    val description: String,
    val orderIndex: Int,
    val lessons: List<LessonListDto>
)

data class LessonListDto(
    val id: Long,
    val title: String,
    val lessonType: LessonType,
    val orderIndex: Int,
    val xpReward: Int,
    val bytesReward: Int,
    val completed: Boolean
)
