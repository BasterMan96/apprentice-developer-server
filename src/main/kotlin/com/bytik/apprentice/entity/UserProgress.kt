package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_progress",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "lesson_id"])]
)
class UserProgress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @JsonIgnore
    var lesson: Lesson,

    @Enumerated(EnumType.STRING)
    var status: ProgressStatus = ProgressStatus.IN_PROGRESS,

    var score: Int = 0,

    var completedAt: LocalDateTime? = null
)

enum class ProgressStatus { IN_PROGRESS, COMPLETED }
