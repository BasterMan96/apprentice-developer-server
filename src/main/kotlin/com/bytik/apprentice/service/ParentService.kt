package com.bytik.apprentice.service

import com.bytik.apprentice.dto.ChildProgressDto
import com.bytik.apprentice.dto.CourseListDto
import com.bytik.apprentice.dto.EnrollmentDto
import com.bytik.apprentice.dto.LinkChildRequest
import com.bytik.apprentice.dto.UserDto
import com.bytik.apprentice.dto.UserStatsDto
import com.bytik.apprentice.dto.toDto
import com.bytik.apprentice.entity.ProgressStatus
import com.bytik.apprentice.entity.UserRole
import com.bytik.apprentice.exception.ApiException
import com.bytik.apprentice.repository.ModuleRepository
import com.bytik.apprentice.repository.UserCourseEnrollmentRepository
import com.bytik.apprentice.repository.UserProgressRepository
import com.bytik.apprentice.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParentService(
    private val userRepository: UserRepository,
    private val userProgressRepository: UserProgressRepository,
    private val userCourseEnrollmentRepository: UserCourseEnrollmentRepository,
    private val moduleRepository: ModuleRepository
) {

    @Transactional
    fun linkChild(parentId: Long, request: LinkChildRequest): UserDto {
        val parent = userRepository.findById(parentId).orElseThrow {
            ApiException("Parent not found", HttpStatus.NOT_FOUND)
        }

        val child = userRepository.findByLogin(request.childLogin)
            ?: throw ApiException("Child account not found", HttpStatus.NOT_FOUND)

        if (child.role != UserRole.STUDENT) {
            throw ApiException("Account '${request.childLogin}' is not a student", HttpStatus.BAD_REQUEST)
        }

        child.parent = parent
        userRepository.save(child)

        return child.toDto()
    }

    @Transactional(readOnly = true)
    fun getChildren(parentId: Long): List<ChildProgressDto> {
        val children = userRepository.findByParentId(parentId)

        return children.map { child ->
            val lessonsCompleted = userProgressRepository.countByUserIdAndStatus(child.id, ProgressStatus.COMPLETED)
            val enrollments = userCourseEnrollmentRepository.findByUserId(child.id)
            val coursesCompleted = enrollments.count { it.completedAt != null }

            val stats = UserStatsDto(
                totalXp = child.xp,
                level = child.level,
                bytesBalance = child.bytesBalance,
                coursesCompleted = coursesCompleted,
                lessonsCompleted = lessonsCompleted,
                streak = 0
            )

            val recentCourses = enrollments.sortedByDescending { it.enrolledAt }.take(3).map { enrollment ->
                val course = enrollment.course
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

                EnrollmentDto(
                    id = enrollment.id,
                    course = courseListDto,
                    progressPercent = enrollment.progressPercent,
                    enrolledAt = enrollment.enrolledAt,
                    completedAt = enrollment.completedAt
                )
            }

            ChildProgressDto(
                child = child.toDto(),
                stats = stats,
                recentCourses = recentCourses
            )
        }
    }
}
