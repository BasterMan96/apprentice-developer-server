package com.bytik.apprentice.controller

import com.bytik.apprentice.dto.AuthResponse
import com.bytik.apprentice.dto.LoginRequest
import com.bytik.apprentice.dto.RegisterRequest
import com.bytik.apprentice.dto.UserDto
import com.bytik.apprentice.dto.toDto
import com.bytik.apprentice.security.CustomUserDetails
import com.bytik.apprentice.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.login(request))
    }

    @GetMapping("/me")
    fun me(@AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userDetails.user.toDto())
    }
}
