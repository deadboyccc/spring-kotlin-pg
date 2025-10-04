package dev.dead.springkotlinpg.repositories

import dev.dead.springkotlinpg.entities.UserEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface UserReactiveRepository: ReactiveCrudRepository<UserEntity, UUID> {
    fun findByEmail(email: String): Mono<UserEntity>
}