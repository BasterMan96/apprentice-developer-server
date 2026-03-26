package com.bytik.apprentice.controller

import com.bytik.apprentice.dto.CodeRunRequest
import com.bytik.apprentice.dto.CodeRunResponse
import com.bytik.apprentice.security.CustomUserDetails
import com.bytik.apprentice.service.CodeExecutionService
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/code")
class CodeController(private val codeExecutionService: CodeExecutionService) {

    @PostMapping("/run")
    fun runCode(
        @Valid @RequestBody request: CodeRunRequest,
        @AuthenticationPrincipal user: CustomUserDetails
    ): CodeRunResponse = codeExecutionService.runCode(request.code)
}
