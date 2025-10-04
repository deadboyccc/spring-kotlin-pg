package dev.dead.springkotlinpg.bootstrap

import dev.dead.springkotlinpg.entities.UserEntity
import dev.dead.springkotlinpg.repositories.UserReactiveRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class BootstrapLoaderTest {
    private val repo: UserReactiveRepository = mock()
    private val loader = BootstrapLoader()

    @Test
    fun `should load 5 users`() {
        val users = listOf(
            UserEntity(name = "Alice", email = "alice@example.com"),
            UserEntity(name = "Bob", email = "bob@example.com"),
            UserEntity(name = "Charlie", email = "charlie@example.com"),
            UserEntity(name = "Diana", email = "diana@example.com"),
            UserEntity(name = "Eve", email = "eve@example.com")
        )
        whenever(repo.save(org.mockito.kotlin.any())).thenReturn(Mono.just(users[0]))
        val runner = loader.loadUsers(repo)
        runner.run(org.springframework.boot.ApplicationArguments { emptyArray() })
        verify(repo, org.mockito.kotlin.times(5)).save(org.mockito.kotlin.any())
    }
}
