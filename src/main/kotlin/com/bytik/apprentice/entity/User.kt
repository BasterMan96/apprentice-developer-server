package com.bytik.apprentice.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var fullName: String,

    @Column(unique = true)
    var login: String,

    var phone: String = "",

    var passwordHash: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.STUDENT,

    var avatarUrl: String? = null,

    var level: Int = 1,

    var xp: Int = 0,

    var bytesBalance: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    var parent: User? = null,

    var createdAt: LocalDateTime = LocalDateTime.now()
)

enum class UserRole { STUDENT, PARENT }
