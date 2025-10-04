package dev.dead.springkotlinpg.bootstrap

import dev.dead.springkotlinpg.entities.UserEntity
import dev.dead.springkotlinpg.repositories.UserReactiveRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux

@Configuration
class BootstrapLoader {
    @Bean
    fun loadUsers(repo: UserReactiveRepository) = ApplicationRunner {
        Flux.just(
            UserEntity(name = "Alice", email = "alice@example.com"),
            UserEntity(name = "Bob", email = "bob@example.com"),
            UserEntity(name = "Charlie", email = "charlie@example.com"),
            UserEntity(name = "Diana", email = "diana@example.com"),
            UserEntity(name = "Eve", email = "eve@example.com")
        ).flatMap(repo::save)
         .subscribe { println("Loaded user: ${it.name}") }
    }
}
