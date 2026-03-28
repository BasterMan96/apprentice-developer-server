package com.bytik.apprentice.service

import com.bytik.apprentice.dto.CommentDto
import com.bytik.apprentice.dto.CommentsResponse
import com.bytik.apprentice.dto.CreateCommentRequest
import com.bytik.apprentice.entity.LessonComment
import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.exception.ApiException
import com.bytik.apprentice.repository.LessonCommentRepository
import com.bytik.apprentice.repository.LessonRepository
import com.bytik.apprentice.repository.UserProgressRepository
import com.bytik.apprentice.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: LessonCommentRepository,
    private val lessonRepository: LessonRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userRepository: UserRepository
) {

    fun getComments(lessonId: Long, userId: Long): CommentsResponse {
        checkLessonCompleted(lessonId, userId)
        val comments = commentRepository.findByLessonIdOrderByCreatedAtDesc(lessonId)
        val total = commentRepository.countByLessonId(lessonId)
        return CommentsResponse(
            comments = comments.map { it.toDto(userId) },
            total = total
        )
    }

    fun createComment(lessonId: Long, userId: Long, request: CreateCommentRequest): CommentDto {
        checkLessonCompleted(lessonId, userId)
        val user = userRepository.findById(userId)
            .orElseThrow { ApiException("Пользователь не найден", HttpStatus.NOT_FOUND) }
        val lesson = lessonRepository.findById(lessonId)
            .orElseThrow { ApiException("Урок не найден", HttpStatus.NOT_FOUND) }
        val comment = commentRepository.save(
            LessonComment(user = user, lesson = lesson, content = request.content.trim())
        )
        return comment.toDto(userId)
    }

    fun deleteComment(commentId: Long, userId: Long) {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { ApiException("Комментарий не найден", HttpStatus.NOT_FOUND) }
        if (comment.user.id != userId) {
            throw ApiException("Нельзя удалить чужой комментарий", HttpStatus.FORBIDDEN)
        }
        commentRepository.delete(comment)
    }

    private fun checkLessonCompleted(lessonId: Long, userId: Long) {
        val progress = userProgressRepository.findByUserIdAndLessonId(userId, lessonId)
        if (progress == null || progress.status != ProgressStatus.COMPLETED) {
            throw ApiException("Пройдите урок, чтобы оставить комментарий", HttpStatus.FORBIDDEN)
        }
    }

    private fun LessonComment.toDto(currentUserId: Long) = CommentDto(
        id = id,
        authorName = user.fullName,
        authorLogin = user.login,
        content = content,
        createdAt = createdAt,
        isOwn = user.id == currentUserId
    )
}
