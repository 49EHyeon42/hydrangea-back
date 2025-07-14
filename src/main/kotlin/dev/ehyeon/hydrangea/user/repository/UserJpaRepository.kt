package dev.ehyeon.hydrangea.user.repository

import dev.ehyeon.hydrangea.user.entity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long> {
    fun findByUsername(username: String): UserJpaEntity?

    fun existsByUsername(username: String): Boolean
}
