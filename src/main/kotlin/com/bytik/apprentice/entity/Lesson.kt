package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "lessons")
class Lesson(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    @JsonIgnore
    var module: Module,

    var title: String,

    @Column(columnDefinition = "TEXT")
    var content: String = "",

    @Enumerated(EnumType.STRING)
    var lessonType: LessonType = LessonType.THEORY,

    var orderIndex: Int = 0,

    var xpReward: Int = 10,

    var bytesReward: Int = 5,

    @OneToMany(mappedBy = "lesson", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @OrderBy("orderIndex")
    @JsonIgnore
    var quizQuestions: MutableList<QuizQuestion> = mutableListOf(),

    @OneToMany(mappedBy = "lesson", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    var codeTasks: MutableList<CodeTask> = mutableListOf()
)

enum class LessonType { THEORY, PRACTICE, QUIZ, PROJECT }
