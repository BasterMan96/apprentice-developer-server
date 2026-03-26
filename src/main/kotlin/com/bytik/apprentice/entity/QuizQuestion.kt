package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "quiz_questions")
class QuizQuestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    @JsonIgnore
    var lesson: Lesson,

    @Column(columnDefinition = "TEXT")
    var questionText: String,

    @Enumerated(EnumType.STRING)
    var questionType: QuestionType = QuestionType.SINGLE_CHOICE,

    var orderIndex: Int = 0,

    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    var options: MutableList<QuizOption> = mutableListOf()
)

enum class QuestionType { SINGLE_CHOICE, MULTIPLE_CHOICE, CODE }
