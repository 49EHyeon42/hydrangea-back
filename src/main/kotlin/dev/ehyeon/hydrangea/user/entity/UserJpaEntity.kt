package dev.ehyeon.hydrangea.user.entity

import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "`user`")
@EntityListeners(AuditingEntityListener::class)
class UserJpaEntity(
    @Id
    @Tsid
    val id: Long? = null,

    @Column(nullable = false, length = 32)
    val username: String,

    @Column(nullable = false, length = 60)
    val hashedPassword: String,

    @Column(nullable = false, length = 32)
    val nickname: String,
) {
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private lateinit var _createdDate: LocalDateTime

    val createdDate: LocalDateTime
        get() = _createdDate

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private lateinit var _lastModifiedDate: LocalDateTime

    val lastModifiedDate: LocalDateTime
        get() = _lastModifiedDate
}
