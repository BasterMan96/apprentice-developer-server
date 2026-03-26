package com.bytik.apprentice.dto

import com.bytik.apprentice.entity.User
import com.bytik.apprentice.entity.UserRole
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class RegisterRequest(
    @field:NotBlank val fullName: String,
    @field:NotBlank @field:Size(min = 3, max = 50) val login: String,
    val phone: String = "",
    @field:NotBlank @field:Size(min = 6) val password: String,
    val role: UserRole = UserRole.STUDENT
)

data class LoginRequest(
    @field:NotBlank val login: String,
    @field:NotBlank val password: String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Long,
    val fullName: String,
    val login: String,
    val phone: String,
    val role: UserRole,
    val avatarUrl: String?,
    val level: Int,
    val xp: Int,
    val bytesBalance: Int,
    val createdAt: LocalDateTime
)

fun User.toDto() = UserDto(
    id = id,
    fullName = fullName,
    login = login,
    phone = phone,
    role = role,
    avatarUrl = avatarUrl,
    level = level,
    xp = xp,
    bytesBalance = bytesBalance,
    createdAt = createdAt
)
