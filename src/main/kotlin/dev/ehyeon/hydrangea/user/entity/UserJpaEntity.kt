package dev.ehyeon.hydrangea.user.entity

import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "`user`")
class UserJpaEntity(
    @Id
    @Tsid
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 32)
    val username: String,

    @Column(nullable = false, length = 60)
    val hashedPassword: String,

    @Column(nullable = false, length = 32)
    val nickname: String,

    @Column(name = "created_date", nullable = false, updatable = false)
    val createdDate: Instant = Instant.now(),

    @Column(name = "last_modified_date", nullable = false)
    val lastModifiedDate: Instant = Instant.now(),
)
