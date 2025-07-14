package dev.ehyeon.hydrangea.common.service

import dev.ehyeon.hydrangea.common.exception.DuplicateEmailException
import dev.ehyeon.hydrangea.common.exception.InvalidCredentialsException
import dev.ehyeon.hydrangea.common.property.TokenProperty
import dev.ehyeon.hydrangea.common.service.dto.AuthToken
import dev.ehyeon.hydrangea.user.entity.UserJpaEntity
import dev.ehyeon.hydrangea.user.repository.UserJpaRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.*

@Service
class AuthService(
    private val tokenProperty: TokenProperty,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserJpaRepository,
    private val redisTemplate: StringRedisTemplate,
) {
    fun signIn(username: String, password: String): AuthToken {
        val user = userRepository.findByUsername(username)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        val accessToken = UUID.randomUUID().toString()

        // NOTE: !! 고민
        saveAccessToken(accessToken, user.id!!)

        return AuthToken(accessToken)
    }

    private fun saveAccessToken(accessToken: String, userId: Long) {
        val expiration = Duration.ofSeconds(tokenProperty.accessToken.expirationSeconds)

        redisTemplate.opsForValue().set(accessToken, userId.toString(), expiration)
    }

    fun signOut(accessToken: String) {
        redisTemplate.delete(accessToken)
    }

    fun findUserIdByAccessToken(accessToken: String): String? {
        return redisTemplate.opsForValue().get(accessToken)
    }

    // TODO: AuthService에서 분리
    @Transactional
    fun signUp(username: String, password: String, nickname: String) {
        if (userRepository.existsByUsername(username)) {
            throw DuplicateEmailException()
        }

        val hashedPassword = passwordEncoder.encode(password)

        val newUser = UserJpaEntity(
            username = username,
            hashedPassword = hashedPassword,
            nickname = nickname
        )

        userRepository.save(newUser)
    }
}
