package dev.dead.springkotlinpg.controllers

import dev.dead.springkotlinpg.entities.UserEntity
import dev.dead.springkotlinpg.repositories.UserReactiveRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.UUID

data class UserRequestR(val name: String, val email: String)
data class UserResponseR(val id: UUID?, val name: String, val email: String)

@RestController
@RequestMapping("/api/reactive/users")
class UserReactiveController(private val repo: UserReactiveRepository) {
    @PostMapping
    fun create(@RequestBody req: UserRequestR): Mono<ResponseEntity<UserResponseR>> =
        repo.save(UserEntity(name = req.name, email = req.email))
            .map { ResponseEntity.ok(UserResponseR(it.id, it.name, it.email)) }

    @GetMapping("/{id}")
    fun update(@PathVariable id: String): Mono<ResponseEntity<UserResponseR>> =
        try {
            repo.findById(UUID.fromString(id))
                .map { ResponseEntity.ok(UserResponseR(it.id, it.name, it.email)) }
                .defaultIfEmpty(ResponseEntity.notFound().build())
        } catch (e: IllegalArgumentException) {
            Mono.just(ResponseEntity.badRequest().build())
        }

    @GetMapping
    fun list(): Flux<UserResponseR> = 
        repo.findAll()
            .map { UserResponseR(it.id, it.name, it.email) }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody req: UserRequestR): Mono<ResponseEntity<UserResponseR>> =
        try {
            val uuid = UUID.fromString(id)
            repo.findById(uuid)
                .flatMap { repo.save(it.copy(name = req.name, email = req.email)) }
                .map { ResponseEntity.ok(UserResponseR(it.id, it.name, it.email)) }
                .defaultIfEmpty(ResponseEntity.notFound().build())
        } catch (e: IllegalArgumentException) {
            Mono.just(ResponseEntity.badRequest().build())
        }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): Mono<ResponseEntity<Void>> =
        try {
            val uuid = UUID.fromString(id)
            repo.existsById(uuid)
                .flatMap { exists ->
                    if (exists) repo.deleteById(uuid)
                        .then(Mono.just(ResponseEntity.noContent().build()))
                    else Mono.just(ResponseEntity.notFound().build())
                }
        } catch (e: IllegalArgumentException) {
            Mono.just(ResponseEntity.badRequest().build())
        }
}