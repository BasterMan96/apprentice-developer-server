package com.bytik.apprentice.controller

import com.bytik.apprentice.dto.CommentDto
import com.bytik.apprentice.dto.CommentsResponse
import com.bytik.apprentice.dto.CreateCommentRequest
import com.bytik.apprentice.security.CustomUserDetails
import com.bytik.apprentice.service.CommentService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CommentController(private val commentService: CommentService) {

    @GetMapping("/lessons/{lessonId}/comments")
    fun getComments(
        @PathVariable lessonId: Long,
        @AuthenticationPrincipal user: CustomUserDetails
    ): CommentsResponse {
        return commentService.getComments(lessonId, user.user.id)
    }

    @PostMapping("/lessons/{lessonId}/comments")
    fun createComment(
        @PathVariable lessonId: Long,
        @Valid @RequestBody request: CreateCommentRequest,
        @AuthenticationPrincipal user: CustomUserDetails
    ): CommentDto {
        return commentService.createComment(lessonId, user.user.id, request)
    }

    @DeleteMapping("/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthenticationPrincipal user: CustomUserDetails
    ) {
        commentService.deleteComment(commentId, user.user.id)
    }
}
