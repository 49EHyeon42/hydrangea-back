package dev.ehyeon.hydrangea.user.repository

import dev.ehyeon.hydrangea.user.entity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserJpaEntity, Long>
