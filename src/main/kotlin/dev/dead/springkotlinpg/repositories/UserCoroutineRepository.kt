package dev.dead.springkotlinpg.repositories
import dev.dead.springkotlinpg.entities.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserCoroutineRepository : CoroutineCrudRepository<UserEntity, String> {
    suspend fun findByEmail(email: String): UserEntity?
}
