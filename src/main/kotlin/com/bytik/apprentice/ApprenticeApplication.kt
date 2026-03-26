package com.bytik.apprentice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApprenticeApplication

fun main(args: Array<String>) {
    runApplication<ApprenticeApplication>(*args)
}
