package dev.ehyeon.hydrangea.space.controller

import dev.ehyeon.hydrangea.space.request.SendMessageRequest
import dev.ehyeon.hydrangea.space.response.JoinUserResponse
import dev.ehyeon.hydrangea.space.response.SendMessageResponse
import dev.ehyeon.hydrangea.space.service.SpaceService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class SpaceController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val spaceService: SpaceService,
) {
    @MessageMapping("/spaces/users/join")
    fun joinUser(
        messageHeaderAccessor: SimpMessageHeaderAccessor,
    ) {
        val userId = findUserId(messageHeaderAccessor)
        val userNickname = findUserNickname(messageHeaderAccessor)

        // TODO: 나중에 Space DB 만들어서 설정
        val joinUserResponse = JoinUserResponse(
            userId = userId,
            userNickname = userNickname,
            initialX = 100.0,
            initialY = 100.0,
        )

        messagingTemplate.convertAndSend("/topic/spaces/users/join", joinUserResponse)
    }

    @MessageMapping("/space/chat")
    fun handleMessage(
        headerAccessor: SimpMessageHeaderAccessor,
        request: SendMessageRequest,
    ) {
        val userId = findUserId(headerAccessor)
        val userNickname = findUserNickname(headerAccessor)

        val savedMessageId = spaceService.saveMessage(
            senderId = userId,
            senderNickname = userNickname,
            content = request.content
        )

        // TODO: destination 분리
        messagingTemplate.convertAndSend(
            "/topic/space/chat",
            SendMessageResponse(
                messageId = savedMessageId,
                senderId = userId,
                senderNickname = userNickname,
                content = request.content,
            )
        )
    }

    // TODO: refactor or fix
    @MessageMapping("/space/move")
    fun handleMove(
        headerAccessor: SimpMessageHeaderAccessor,
        moveRequest: MoveRequest,
    ) {
        val userId = findUserId(headerAccessor)
        val userNickname = findUserNickname(headerAccessor)

        val moveResponse = MoveResponse(
            playerId = userId.toString(),
            x = moveRequest.x,
            y = moveRequest.y,
            type = moveRequest.type,
            username = userNickname
        )

        messagingTemplate.convertAndSend("/topic/space/move", moveResponse)
    }

    private fun findUserId(messageHeaderAccessor: SimpMessageHeaderAccessor): Long {
        // TODO: custom exception
        return messageHeaderAccessor.sessionAttributes?.get("userId") as? Long
            ?: throw RuntimeException()
    }

    private fun findUserNickname(messageHeaderAccessor: SimpMessageHeaderAccessor): String {
        // TODO: custom exception
        return messageHeaderAccessor.sessionAttributes?.get("userNickname") as? String
            ?: throw RuntimeException()
    }

    // TODO-NOTE: WebSocket Exception 처리는 어떻게 하지
}

// 요청 DTO
data class MoveRequest(
    val x: Double,
    val y: Double,
    val type: String, // "move", "leave"
)

// 응답 DTO
data class MoveResponse(
    val playerId: String,
    val x: Double,
    val y: Double,
    val type: String,
    val username: String? = null,
)
