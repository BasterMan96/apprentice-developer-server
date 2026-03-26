package com.bytik.apprentice.service

import com.bytik.apprentice.dto.CodeRunResponse
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class CodeExecutionService {

    fun runCode(code: String): CodeRunResponse {
        val startTime = System.currentTimeMillis()
        val process = ProcessBuilder("python3", "-c", code)
            .redirectErrorStream(false)
            .start()
        val completed = process.waitFor(5, TimeUnit.SECONDS)
        if (!completed) {
            process.destroyForcibly()
            return CodeRunResponse(
                output = "",
                error = "Превышено время выполнения (5 сек)",
                executionTimeMs = 5000
            )
        }
        val stdout = process.inputStream.bufferedReader().readText().take(10000)
        val stderr = process.errorStream.bufferedReader().readText().take(5000)
        val elapsed = System.currentTimeMillis() - startTime
        return CodeRunResponse(
            output = stdout,
            error = stderr.ifBlank { null },
            executionTimeMs = elapsed
        )
    }
}
