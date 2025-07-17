package dev.ehyeon.hydrangea.common.filter

import dev.ehyeon.hydrangea.common.authentication.token.AuthenticationToken
import dev.ehyeon.hydrangea.common.exception.InvalidCredentialsException
import dev.ehyeon.hydrangea.common.property.TokenProperty
import dev.ehyeon.hydrangea.common.service.AuthService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter(
    private val tokenProperty: TokenProperty,
    private val authService: AuthService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val cookies = request.cookies

        val accessToken = cookies?.firstOrNull { it.name == tokenProperty.accessToken.name }?.value

        if (!accessToken.isNullOrBlank()) {
            val userId = getUserId(accessToken)

            if (userId != null) {
                SecurityContextHolder.getContext().authentication = AuthenticationToken(userId)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getUserId(accessToken: String): Long? {
        return try {
            authService.findUserIdByAccessToken(accessToken).toLongOrNull()
        } catch (_: InvalidCredentialsException) {
            null
        }
    }
}
