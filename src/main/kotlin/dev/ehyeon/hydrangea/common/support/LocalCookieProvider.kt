package dev.ehyeon.hydrangea.common.support

import dev.ehyeon.hydrangea.common.constant.EnvironmentConstant
import dev.ehyeon.hydrangea.common.property.TokenProperty
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Profile(EnvironmentConstant.LOCAL)
@Component
class LocalCookieProvider(
    private val tokenProperty: TokenProperty,
) : CookieProvider {
    override fun createAccessTokenCookie(accessToken: String): ResponseCookie {
        return ResponseCookie.from(tokenProperty.accessToken.name, accessToken)
            .path("/")
            .httpOnly(true)
            .sameSite("Lax")
            .maxAge(tokenProperty.accessToken.expirationSeconds)
            .build()
    }

    override fun findAccessTokenFromRequest(request: HttpServletRequest): String? {
        return request.cookies?.firstOrNull { it.name == tokenProperty.accessToken.name }?.value
    }

    override fun deleteAccessTokenCookie(): ResponseCookie {
        return ResponseCookie.from(tokenProperty.accessToken.name, "")
            .path("/")
            .httpOnly(true)
            .sameSite("Lax")
            .maxAge(0)
            .build()
    }
}
