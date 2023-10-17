package com.example.testuniverproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class TestUniverProjectApplication

fun main(args: Array<String>) {
    runApplication<TestUniverProjectApplication>(*args)
}
