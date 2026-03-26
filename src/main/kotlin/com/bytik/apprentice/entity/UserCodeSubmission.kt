package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_code_submissions")
class UserCodeSubmission(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_task_id")
    @JsonIgnore
    var codeTask: CodeTask,

    @Column(columnDefinition = "TEXT")
    var submittedCode: String,

    @Column(columnDefinition = "TEXT")
    var output: String = "",

    var isCorrect: Boolean = false,

    var submittedAt: LocalDateTime = LocalDateTime.now()
)
