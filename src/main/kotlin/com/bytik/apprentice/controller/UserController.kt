package com.bytik.apprentice.controller

import com.bytik.apprentice.dto.AchievementDto
import com.bytik.apprentice.dto.CertificateDto
import com.bytik.apprentice.dto.EnrollmentDto
import com.bytik.apprentice.dto.PortfolioItemDto
import com.bytik.apprentice.dto.UserProfileDto
import com.bytik.apprentice.security.CustomUserDetails
import com.bytik.apprentice.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/profile")
    fun getProfile(
        @AuthenticationPrincipal user: CustomUserDetails
    ): UserProfileDto = userService.getProfile(user.user.id)

    @GetMapping("/courses")
    fun getMyCourses(
        @AuthenticationPrincipal user: CustomUserDetails
    ): List<EnrollmentDto> = userService.getMyCourses(user.user.id)

    @GetMapping("/achievements")
    fun getAchievements(
        @AuthenticationPrincipal user: CustomUserDetails
    ): List<AchievementDto> = userService.getAchievements(user.user.id)

    @GetMapping("/certificates")
    fun getCertificates(
        @AuthenticationPrincipal user: CustomUserDetails
    ): List<CertificateDto> = userService.getCertificates(user.user.id)

    @GetMapping("/portfolio")
    fun getPortfolio(
        @AuthenticationPrincipal user: CustomUserDetails
    ): List<PortfolioItemDto> = userService.getPortfolio(user.user.id)
}
