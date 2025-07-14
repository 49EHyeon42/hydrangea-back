package dev.ehyeon.hydrangea.common.handshakeinterceptor

import dev.ehyeon.hydrangea.common.property.TokenProperty
import dev.ehyeon.hydrangea.common.service.AuthService
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
) : HandshakeInterceptor {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>,
    ): Boolean {
        val servletRequest = (request as ServletServerHttpRequest).servletRequest

        val cookies = servletRequest.cookies

        val accessToken = cookies?.firstOrNull { it.name == tokenProperty.accessToken.name }?.value

        if (!accessToken.isNullOrBlank()) {
            val userId = authService.findUserIdByAccessToken(accessToken)?.toLongOrNull()

            if (userId != null) {
                attributes["userId"] = userId
                return true
            }
        }

        return false
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {
        TODO("Not yet implemented")
    }
}
