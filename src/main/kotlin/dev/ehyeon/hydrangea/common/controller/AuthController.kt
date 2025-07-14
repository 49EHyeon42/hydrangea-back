package dev.ehyeon.hydrangea.common.controller

import dev.ehyeon.hydrangea.common.exception.DuplicateEmailException
import dev.ehyeon.hydrangea.common.exception.InvalidCredentialsException
import dev.ehyeon.hydrangea.common.request.SignInRequest
import dev.ehyeon.hydrangea.common.request.SignUpRequest
import dev.ehyeon.hydrangea.common.service.AuthService
import dev.ehyeon.hydrangea.common.support.CookieProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val cookieProvider: CookieProvider,
) {
    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody request: SignInRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val authToken = authService.signIn(request.username, request.password)

        val accessTokenCookie = cookieProvider.createAccessTokenCookie(authToken.accessToken)

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())

        return ResponseEntity.ok().build()
    }

    @PostMapping("/sign-out")
    fun signOut(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val accessToken = cookieProvider.findAccessTokenFromRequest(request)

        if (accessToken != null) {
            authService.signOut(accessToken)

            val deletedAccessTokenCookie = cookieProvider.deleteAccessTokenCookie()

            response.addHeader(HttpHeaders.SET_COOKIE, deletedAccessTokenCookie.toString())
        }

        return ResponseEntity.ok().build()
    }

    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): ResponseEntity<Void> {
        authService.signUp(request.username, request.password, request.nickname)

        return ResponseEntity.ok().build()
    }

    // TODO:
    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(exception: InvalidCredentialsException): ResponseEntity<Void> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    // TODO:
    @ExceptionHandler(DuplicateEmailException::class)
    fun handleDuplicateEmail(exception: DuplicateEmailException): ResponseEntity<Void> {
        return ResponseEntity.badRequest().build()
    }
}
