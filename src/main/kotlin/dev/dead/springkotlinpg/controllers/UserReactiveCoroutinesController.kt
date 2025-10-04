package dev.dead.springkotlinpg.controllers

import dev.dead.springkotlinpg.entities.UserEntity
import dev.dead.springkotlinpg.repositories.UserCoroutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class UserRequestC(val name: String, val email: String)
data class UserResponseC(val id: String?, val name: String, val email: String)

@RestController
@RequestMapping("/api/flow/users")
class UserCoroutineController(private val repo: UserCoroutineRepository) {

    @PostMapping
    suspend fun create(@RequestBody req: UserRequestC): ResponseEntity<UserResponseC> {
        val saved = repo.save(UserEntity(name = req.name, email = req.email))
        return ResponseEntity.ok(UserResponseC(saved.id?.toString(), saved.name, saved.email))
    }

    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: String): ResponseEntity<UserResponseC> =
        repo.findById(id)?.let { ResponseEntity.ok(UserResponseC(it.id?.toString(), it.name, it.email)) }
            ?: ResponseEntity.notFound().build()

    @GetMapping
    fun list(): Flow<UserResponseC> = repo.findAll().map { UserResponseC(it.id?.toString(), it.name, it.email) }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, @RequestBody req: UserRequestC): ResponseEntity<UserResponseC> {
        val existing = repo.findById(id) ?: return ResponseEntity.notFound().build()
        val updated = existing.copy(name = req.name, email = req.email)
        val saved = repo.save(updated)
        return ResponseEntity.ok(UserResponseC(saved.id?.toString(), saved.name, saved.email))
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<Void> {
        val exists = repo.existsById(id)
        return if (!exists) ResponseEntity.notFound().build() else {
            repo.deleteById(id)
            ResponseEntity.noContent().build()
        }
    }
}
