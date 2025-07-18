package dev.ehyeon.hydrangea.common.handshakeinterceptor

import dev.ehyeon.hydrangea.common.exception.InvalidCredentialsException
import dev.ehyeon.hydrangea.common.property.TokenProperty
import dev.ehyeon.hydrangea.common.service.AuthService
import dev.ehyeon.hydrangea.user.exception.UserNotFoundException
import dev.ehyeon.hydrangea.user.service.UserService
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
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>,
    ): Boolean {
        val accessToken = findAccessToken(request) ?: return false

        val userId = try {
            authService.findUserIdByAccessToken(accessToken).toLong()
        } catch (_: InvalidCredentialsException) {
            return false
        }

        val userNickname = try {
            userService.getUserNickname(userId)
        } catch (_: UserNotFoundException) {
            return false
        }

        attributes["userId"] = userId
        attributes["userNickname"] = userNickname

        return true
    }

    private fun findAccessToken(request: ServerHttpRequest): String? {
        val servletRequest = (request as? ServletServerHttpRequest)?.servletRequest ?: return null

        val cookies = servletRequest.cookies ?: return null

        return cookies
            .firstOrNull { it.name == tokenProperty.accessToken.name }
            ?.value
            ?.takeIf { it.isNotBlank() }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {
        // TODO: 로깅
    }
}
