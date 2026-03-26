package com.bytik.apprentice.controller

import com.bytik.apprentice.dto.ChildProgressDto
import com.bytik.apprentice.dto.LinkChildRequest
import com.bytik.apprentice.dto.UserDto
import com.bytik.apprentice.security.CustomUserDetails
import com.bytik.apprentice.service.ParentService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/parent")
class ParentController(private val parentService: ParentService) {

    @PostMapping("/link-child")
    fun linkChild(
        @RequestBody request: LinkChildRequest,
        @AuthenticationPrincipal user: CustomUserDetails
    ): UserDto = parentService.linkChild(user.user.id, request)

    @GetMapping("/children")
    fun getChildren(
        @AuthenticationPrincipal user: CustomUserDetails
    ): List<ChildProgressDto> = parentService.getChildren(user.user.id)
}
