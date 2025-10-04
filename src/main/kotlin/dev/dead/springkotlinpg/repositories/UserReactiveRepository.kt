package dev.dead.springkotlinpg.repositories

import dev.dead.springkotlinpg.entities.UserEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface UserReactiveRepository: ReactiveCrudRepository<UserEntity, String> {
    fun findByEmail(email: String): Mono<UserEntity>
}