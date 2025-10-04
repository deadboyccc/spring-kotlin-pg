package dev.dead.springkotlinpg.controllers

import dev.dead.springkotlinpg.entities.UserEntity
import dev.dead.springkotlinpg.repositories.UserReactiveRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

data class UserRequestR(val name: String, val email: String)
data class UserResponseR(val id: String?, val name: String, val email: String)

@RestController
@RequestMapping("/api/reactive/users")
class UserReactiveController(private val repo: UserReactiveRepository) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody req: UserRequestR): Mono<ResponseEntity<UserResponseR>> =
        repo.findByEmail(req.email)
            .flatMap<ResponseEntity<UserResponseR>> {
                Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build())
            }
            .switchIfEmpty(
                repo.save(UserEntity(name = req.name, email = req.email))
                    .map {
                        ResponseEntity.status(HttpStatus.CREATED)
                            .body(UserResponseR(it.id, it.name, it.email))
                    }
            )

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): Mono<ResponseEntity<UserResponseR>> =
        repo.findById(id)
            .map { ResponseEntity.ok(UserResponseR(it.id, it.name, it.email)) }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @GetMapping
    fun list(): Flux<UserResponseR> =
        repo.findAll()
            .map { UserResponseR(it.id, it.name, it.email) }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody req: UserRequestR): Mono<ResponseEntity<UserResponseR>> =
        repo.findById(id)
            .flatMap { existing ->
                repo.findByEmail(req.email)
                    .filter { it.id != existing.id }
                    .flatMap<ResponseEntity<UserResponseR>> {
                        Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build())
                    }
                    .switchIfEmpty(
                        repo.save(existing.copy(name = req.name, email = req.email))
                            .map { ResponseEntity.ok(UserResponseR(it.id, it.name, it.email)) }
                    )
            }
            .defaultIfEmpty(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): Mono<ResponseEntity<Void>> =
        repo.existsById(id)
            .flatMap { exists -> if (exists) repo.deleteById(id).then(Mono.just(ResponseEntity.noContent().build())) else Mono.just(ResponseEntity.notFound().build()) }
}

