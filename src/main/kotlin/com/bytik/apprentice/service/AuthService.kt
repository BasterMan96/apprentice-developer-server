package com.bytik.apprentice.service

import com.bytik.apprentice.dto.AuthResponse
import com.bytik.apprentice.dto.LoginRequest
import com.bytik.apprentice.dto.RegisterRequest
import com.bytik.apprentice.dto.toDto
import com.bytik.apprentice.entity.User
import com.bytik.apprentice.exception.ApiException
import com.bytik.apprentice.repository.UserRepository
import com.bytik.apprentice.security.JwtService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.findByLogin(request.login) != null) {
            throw ApiException("Логин '${request.login}' уже занят", HttpStatus.CONFLICT)
        }

        val user = User(
            fullName = request.fullName,
            login = request.login,
            phone = request.phone,
            passwordHash = passwordEncoder.encode(request.password),
            role = request.role
        )

        val saved = userRepository.save(user)
        val token = jwtService.generateToken(saved.id, saved.login)
        return AuthResponse(token = token, user = saved.toDto())
    }

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByLogin(request.login)
            ?: throw ApiException("Неверный логин или пароль", HttpStatus.UNAUTHORIZED)

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw ApiException("Неверный логин или пароль", HttpStatus.UNAUTHORIZED)
        }

        val token = jwtService.generateToken(user.id, user.login)
        return AuthResponse(token = token, user = user.toDto())
    }
}
