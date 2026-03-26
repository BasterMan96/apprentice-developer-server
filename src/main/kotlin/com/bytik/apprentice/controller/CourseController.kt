package com.bytik.apprentice.controller

import com.bytik.apprentice.dto.CourseDetailDto
import com.bytik.apprentice.dto.CourseListDto
import com.bytik.apprentice.dto.EnrollmentDto
import com.bytik.apprentice.entity.Difficulty
import com.bytik.apprentice.security.CustomUserDetails
import com.bytik.apprentice.service.CourseService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/courses")
class CourseController(private val courseService: CourseService) {

    @GetMapping
    fun getCourses(
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) difficulty: Difficulty?
    ): List<CourseListDto> = courseService.getCourses(search, difficulty)

    @GetMapping("/{id}")
    fun getCourse(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: CustomUserDetails?
    ): CourseDetailDto = courseService.getCourse(id, user?.user?.id)

    @PostMapping("/{id}/enroll")
    fun enroll(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: CustomUserDetails
    ): EnrollmentDto = courseService.enroll(id, user.user.id)
}
