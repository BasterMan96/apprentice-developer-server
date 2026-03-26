package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "code_tasks")
class CodeTask(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @JsonIgnore
    var lesson: Lesson,

    @Column(columnDefinition = "TEXT")
    var description: String = "",

    @Column(columnDefinition = "TEXT")
    var initialCode: String = "",

    @Column(columnDefinition = "TEXT")
    var expectedOutput: String = "",

    @Column(columnDefinition = "TEXT")
    var hint: String? = null
)
