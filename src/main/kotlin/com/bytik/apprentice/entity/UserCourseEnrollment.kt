package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_course_enrollment",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "course_id"])]
)
class UserCourseEnrollment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    var course: Course,

    var progressPercent: Int = 0,

    var enrolledAt: LocalDateTime = LocalDateTime.now(),

    var completedAt: LocalDateTime? = null
)
