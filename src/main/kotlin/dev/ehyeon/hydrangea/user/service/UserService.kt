package dev.ehyeon.hydrangea.user.service

import dev.ehyeon.hydrangea.user.entity.UserJpaEntity
import dev.ehyeon.hydrangea.user.exception.UserNotFoundException
import dev.ehyeon.hydrangea.user.repository.UserJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class UserService(
    private val userRepository: UserJpaRepository,
) {
    @Transactional(readOnly = true)
    fun getUserNickname(id: Long): String {
        val foundUser = userRepository.findById(id)
            .orElseThrow { UserNotFoundException() }

        return foundUser.nickname
    }

    @Transactional
    fun updateNickname(id: Long, nickname: String) {
        val foundUser = userRepository.findById(id)
            .orElseThrow { UserNotFoundException() }

        val updatedUser = UserJpaEntity(
            id = foundUser.id,
            username = foundUser.username,
            hashedPassword = foundUser.hashedPassword,
            nickname = nickname,
            createdDate = foundUser.createdDate,
            lastModifiedDate = Instant.now(),
        )

        userRepository.save(updatedUser)
    }
}
