package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "modules")
class Module(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    var course: Course,

    var title: String,

    var description: String = "",

    var orderIndex: Int = 0,

    @OneToMany(mappedBy = "module", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @OrderBy("orderIndex")
    @JsonIgnore
    var lessons: MutableList<Lesson> = mutableListOf()
)
