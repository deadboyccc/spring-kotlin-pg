package dev.dead.springkotlinpg.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("users")
data class UserEntity(
    @Id
    val id: UUID? = null,
    val name: String,
    val email: String
)
