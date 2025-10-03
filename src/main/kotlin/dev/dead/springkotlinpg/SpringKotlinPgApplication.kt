package dev.dead.springkotlinpg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringKotlinPgApplication

fun main(args: Array<String>) {
    runApplication<SpringKotlinPgApplication>(*args)
}
