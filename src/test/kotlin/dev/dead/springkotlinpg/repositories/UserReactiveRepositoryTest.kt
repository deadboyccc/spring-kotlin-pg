package dev.dead.springkotlinpg.repositories

import dev.dead.springkotlinpg.entities.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import reactor.test.StepVerifier

@DataR2dbcTest
class UserReactiveRepositoryTest @Autowired constructor(
    val repo: UserReactiveRepository
) {
    @Test
    fun `should save and find user by email`() {
        val user = UserEntity(name = "RepoTest", email = "repotest@example.com")
        val saved = repo.save(user)
        StepVerifier.create(saved)
            .assertNext { assertEquals("RepoTest", it.name) }
            .verifyComplete()

        val found = repo.findByEmail("repotest@example.com")
        StepVerifier.create(found)
            .assertNext { assertEquals("RepoTest", it.name) }
            .verifyComplete()
    }
}
