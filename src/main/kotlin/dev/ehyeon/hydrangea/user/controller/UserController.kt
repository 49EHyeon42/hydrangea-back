package dev.ehyeon.hydrangea.user.controller

import dev.ehyeon.hydrangea.user.request.UpdateUserNicknameRequest
import dev.ehyeon.hydrangea.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {
    // TODO: 공통 응답
    @PatchMapping("/api/users")
    fun updateNickname(
        @AuthenticationPrincipal id: Long,
        @RequestBody request: UpdateUserNicknameRequest,
    ): ResponseEntity<Void> {
        userService.updateNickname(id, request.nickname)

        return ResponseEntity.ok().build()
    }
}
