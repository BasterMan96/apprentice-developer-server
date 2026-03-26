package com.bytik.apprentice.controller

import com.bytik.apprentice.dto.LessonCompleteRequest
import com.bytik.apprentice.dto.LessonCompleteResponse
import com.bytik.apprentice.dto.LessonDetailDto
import com.bytik.apprentice.security.CustomUserDetails
import com.bytik.apprentice.service.LessonService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/lessons")
class LessonController(private val lessonService: LessonService) {

    @GetMapping("/{id}")
    fun getLesson(
        @PathVariable id: Long,
        @AuthenticationPrincipal user: CustomUserDetails
    ): LessonDetailDto = lessonService.getLesson(id, user.user.id)

    @PostMapping("/{id}/complete")
    fun completeLesson(
        @PathVariable id: Long,
        @RequestBody request: LessonCompleteRequest,
        @AuthenticationPrincipal user: CustomUserDetails
    ): LessonCompleteResponse = lessonService.completeLesson(id, user.user.id, request)
}
