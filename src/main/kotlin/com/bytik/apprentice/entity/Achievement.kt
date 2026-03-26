package com.bytik.apprentice.entity

import jakarta.persistence.*

@Entity
@Table(name = "achievements")
class Achievement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var title: String,

    var description: String = "",

    var iconUrl: String = "",

    @Enumerated(EnumType.STRING)
    var conditionType: AchievementCondition,

    var conditionValue: Int = 1
)

enum class AchievementCondition {
    LESSONS_COMPLETED,
    XP_EARNED,
    COURSE_FINISHED,
    STREAK,
    QUIZZES_PERFECT,
    CODE_TASKS_COMPLETED,
    BYTES_EARNED,
    ACHIEVEMENTS_COUNT
}
