package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_achievements",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "achievement_id"])]
)
class UserAchievement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id")
    @JsonIgnore
    var achievement: Achievement,

    var earnedAt: LocalDateTime = LocalDateTime.now()
)
