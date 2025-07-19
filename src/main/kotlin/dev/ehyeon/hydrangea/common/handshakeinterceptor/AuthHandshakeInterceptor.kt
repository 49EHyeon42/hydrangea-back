package dev.ehyeon.hydrangea.common.handshakeinterceptor

import dev.ehyeon.hydrangea.common.exception.InvalidCredentialsException
import dev.ehyeon.hydrangea.common.property.TokenProperty
import dev.ehyeon.hydrangea.common.service.AuthService
import dev.ehyeon.hydrangea.user.exception.UserNotFoundException
import dev.ehyeon.hydrangea.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class AuthHandshakeInterceptor(
    private val tokenProperty: TokenProperty,
    private val authService: AuthService,
    private val userService: UserService,
) : HandshakeInterceptor {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>,
    ): Boolean {
        // NOTE: 핸드셰이킹 과정 중 accessToken 만료에 대한 고민이 필요하다.
        val accessToken = findAccessToken(request) ?: return false

        // NOTE: 핸드셰이킹 이전에 userId와 userNickname을 찾는 것이 맞는가?
        // NOTE: 인터셉터 분리하면 될 것 같은데
        val userId = findUserId(accessToken) ?: return false

        val userNickname = findUserNickname(userId) ?: return false

        attributes["userId"] = userId
        attributes["userNickname"] = userNickname

        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {
        val servletRequest = (request as ServletServerHttpRequest).servletRequest

        val remoteAddr = servletRequest.remoteAddr
        val userAgent = servletRequest.getHeader("User-Agent")
        val uri = servletRequest.requestURI

        val accessToken = findAccessToken(request) ?: return

        val userId = findUserId(accessToken) ?: return

        val userNickname = findUserNickname(userId) ?: return

        logger.info("""
            {
              "event": "WebSocketHandshake",
              "status": "${if (exception == null) "SUCCESS" else "FAILURE"}",
              "ip": "$remoteAddr",
              "uri": "$uri",
              "userAgent": "$userAgent",
              "userId": "$userId",
              "userNickname": "$userNickname"
            }
        """.trimIndent()
        )
    }

    private fun findAccessToken(request: ServerHttpRequest): String? {
        val servletRequest = (request as? ServletServerHttpRequest)?.servletRequest ?: return null

        val cookies = servletRequest.cookies ?: return null

        return cookies
            .firstOrNull { it.name == tokenProperty.accessToken.name }
            ?.value
            ?.takeIf { it.isNotBlank() }
    }

    private fun findUserId(accessToken: String): Long? {
        return try {
            authService.findUserIdByAccessToken(accessToken).toLong()
        } catch (_: InvalidCredentialsException) {
            return null
        }
    }

    private fun findUserNickname(userId: Long): String? {
        return try {
            userService.getUserNickname(userId)
        } catch (_: UserNotFoundException) {
            return null
        }
    }
}
