package com.bytik.apprentice.service

import com.bytik.apprentice.dto.CourseDetailDto
import com.bytik.apprentice.dto.CourseListDto
import com.bytik.apprentice.dto.EnrollmentDto
import com.bytik.apprentice.dto.LessonListDto
import com.bytik.apprentice.dto.ModuleDto
import com.bytik.apprentice.entity.Difficulty
import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.entity.UserCourseEnrollment
import com.bytik.apprentice.exception.ApiException
import com.bytik.apprentice.repository.CourseRepository
import com.bytik.apprentice.repository.LessonRepository
import com.bytik.apprentice.repository.ModuleRepository
import com.bytik.apprentice.repository.UserCourseEnrollmentRepository
import com.bytik.apprentice.repository.UserProgressRepository
import com.bytik.apprentice.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseService(
    private val courseRepository: CourseRepository,
    private val moduleRepository: ModuleRepository,
    private val lessonRepository: LessonRepository,
    private val userRepository: UserRepository,
    private val userCourseEnrollmentRepository: UserCourseEnrollmentRepository,
    private val userProgressRepository: UserProgressRepository
) {

    @Transactional(readOnly = true)
    fun getCourses(search: String?, difficulty: Difficulty?): List<CourseListDto> {
        val courses = if (search != null) {
            courseRepository.findByTitleContainingIgnoreCase(search)
        } else {
            courseRepository.findAllByOrderByOrderIndex()
        }

        return courses
            .filter { difficulty == null || it.difficulty == difficulty }
            .map { course ->
                val modules = moduleRepository.findByCourseIdOrderByOrderIndex(course.id)
                val lessonCount = modules.sumOf { it.lessons.size }
                CourseListDto(
                    id = course.id,
                    title = course.title,
                    description = course.description,
                    imageUrl = course.imageUrl,
                    difficulty = course.difficulty,
                    ageFrom = course.ageFrom,
                    ageTo = course.ageTo,
                    modulesCount = modules.size,
                    lessonsCount = lessonCount,
                    totalBytesReward = course.totalBytesReward
                )
            }
    }

    @Transactional(readOnly = true)
    fun getCourse(courseId: Long, userId: Long?): CourseDetailDto {
        val course = courseRepository.findById(courseId).orElseThrow {
            ApiException("Курс не найден", HttpStatus.NOT_FOUND)
        }

        val completedLessonIds = if (userId != null) {
            userProgressRepository.findByUserId(userId)
                .filter { it.status == ProgressStatus.COMPLETED }
                .map { it.lesson.id }
                .toSet()
        } else {
            emptySet()
        }

        val enrollment = if (userId != null) {
            userCourseEnrollmentRepository.findByUserIdAndCourseId(userId, courseId)
        } else {
            null
        }

        val modules = moduleRepository.findByCourseIdOrderByOrderIndex(course.id).map { module ->
            ModuleDto(
                id = module.id,
                title = module.title,
                description = module.description,
                orderIndex = module.orderIndex,
                lessons = module.lessons.sortedBy { it.orderIndex }.map { lesson ->
                    LessonListDto(
                        id = lesson.id,
                        title = lesson.title,
                        lessonType = lesson.lessonType,
                        orderIndex = lesson.orderIndex,
                        xpReward = lesson.xpReward,
                        bytesReward = lesson.bytesReward,
                        completed = lesson.id in completedLessonIds
                    )
                }
            )
        }

        return CourseDetailDto(
            id = course.id,
            title = course.title,
            description = course.description,
            imageUrl = course.imageUrl,
            difficulty = course.difficulty,
            ageFrom = course.ageFrom,
            ageTo = course.ageTo,
            totalBytesReward = course.totalBytesReward,
            modules = modules,
            enrolled = enrollment != null,
            progressPercent = enrollment?.progressPercent ?: 0
        )
    }

    @Transactional
    fun enroll(courseId: Long, userId: Long): EnrollmentDto {
        val user = userRepository.findById(userId).orElseThrow {
            ApiException("Пользователь не найден", HttpStatus.NOT_FOUND)
        }
        val course = courseRepository.findById(courseId).orElseThrow {
            ApiException("Курс не найден", HttpStatus.NOT_FOUND)
        }

        val existing = userCourseEnrollmentRepository.findByUserIdAndCourseId(userId, courseId)
        if (existing != null) {
            throw ApiException("Вы уже записаны на этот курс", HttpStatus.CONFLICT)
        }

        val enrollment = UserCourseEnrollment(user = user, course = course)
        val saved = userCourseEnrollmentRepository.save(enrollment)

        val modules = moduleRepository.findByCourseIdOrderByOrderIndex(course.id)
        val lessonCount = modules.sumOf { it.lessons.size }

        val courseListDto = CourseListDto(
            id = course.id,
            title = course.title,
            description = course.description,
            imageUrl = course.imageUrl,
            difficulty = course.difficulty,
            ageFrom = course.ageFrom,
            ageTo = course.ageTo,
            modulesCount = modules.size,
            lessonsCount = lessonCount,
            totalBytesReward = course.totalBytesReward
        )

        return EnrollmentDto(
            id = saved.id,
            course = courseListDto,
            progressPercent = saved.progressPercent,
            enrolledAt = saved.enrolledAt,
            completedAt = saved.completedAt
        )
    }
}
