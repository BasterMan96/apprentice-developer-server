package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "courses")
class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var title: String,

    var description: String = "",

    var imageUrl: String = "",

    @Enumerated(EnumType.STRING)
    var difficulty: Difficulty = Difficulty.BEGINNER,

    var ageFrom: Int = 7,

    var ageTo: Int = 15,

    var orderIndex: Int = 0,

    var totalBytesReward: Int = 0,

    @OneToMany(mappedBy = "course", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @OrderBy("orderIndex")
    @JsonIgnore
    var modules: MutableList<Module> = mutableListOf()
)

enum class Difficulty { BEGINNER, INTERMEDIATE, ADVANCED }
