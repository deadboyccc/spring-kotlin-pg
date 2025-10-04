package dev.dead.springkotlinpg.controllers

import dev.dead.springkotlinpg.entities.UserEntity
import dev.dead.springkotlinpg.repositories.UserReactiveRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

class UserReactiveControllerTest {
    private val repo: UserReactiveRepository = mock()
    private val controller = UserReactiveController(repo)

    @Test
    fun `should create user`() {
        val user = UserEntity(id = "1", name = "Test", email = "test@example.com")
        whenever(repo.save(org.mockito.kotlin.any())).thenReturn(Mono.just(user))
        val req = UserRequestR(name = "Test", email = "test@example.com")
        val result = controller.create(req).block()
        assertEquals("Test", result?.body?.name)
        assertEquals("test@example.com", result?.body?.email)
    }

    @Test
    fun `should get user by id`() {
        val user = UserEntity(id = "1", name = "Test", email = "test@example.com")
        whenever(repo.findById("1")).thenReturn(Mono.just(user))
        val result = controller.get("1").block()
        assertEquals("Test", result?.body?.name)
    }
}
