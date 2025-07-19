package dev.ehyeon.hydrangea.user.initializer

import dev.ehyeon.hydrangea.common.constant.EnvironmentConstant
import dev.ehyeon.hydrangea.user.entity.UserJpaEntity
import dev.ehyeon.hydrangea.user.repository.UserJpaRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserDataInitializer(
    private val environment: Environment,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserJpaRepository,
) : ApplicationRunner {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun run(args: ApplicationArguments?) {
        when {
            environment.acceptsProfiles(Profiles.of(EnvironmentConstant.LOCAL)) -> {
                initializeLocalUsers()
            }
        }
    }

    private fun initializeLocalUsers() {
        val testUserJpaEntities = (1..10).map { i ->
            createTestUserJpaEntity("test$i")
        }

        userRepository.saveAll(testUserJpaEntities)
    }

    private fun createTestUserJpaEntity(username: String): UserJpaEntity {
        return UserJpaEntity(
            username = username,
            hashedPassword = passwordEncoder.encode(username),
            nickname = username
        )
    }
}
