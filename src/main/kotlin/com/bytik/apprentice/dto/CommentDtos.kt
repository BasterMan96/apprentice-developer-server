package com.bytik.apprentice.dto

import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CommentDto(
    val id: Long,
    val authorName: String,
    val authorLogin: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isOwn: Boolean
)

data class CreateCommentRequest(
    @field:NotBlank(message = "Комментарий не может быть пустым")
    val content: String
)

data class CommentsResponse(
    val comments: List<CommentDto>,
    val total: Long
)
