package dev.dead.springkotlinpg.entities


import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table("users")
data class UserEntity(
    @Id
    val id: String? = null,
    val name: String,
    val email: String
)
