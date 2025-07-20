package dev.ehyeon.hydrangea.space.controller

import dev.ehyeon.hydrangea.space.request.SendMessageRequest
import dev.ehyeon.hydrangea.space.response.JoinPlayerResponse
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
    @MessageMapping("/space/chat")
    fun handleMessage(
        headerAccessor: SimpMessageHeaderAccessor,
        request: SendMessageRequest,
    ) {
        // TODO: custom exception
        val userId = headerAccessor.sessionAttributes?.get("userId") as? Long
            ?: throw RuntimeException()

        // TODO: custom exception
        val userNickname = headerAccessor.sessionAttributes?.get("userNickname") as? String
            ?: throw RuntimeException()

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

    // TODO: refactor
    @MessageMapping("/space/join")
    fun handleJoin(
        headerAccessor: SimpMessageHeaderAccessor,
    ) {
        // TODO: custom exception
        val userId = headerAccessor.sessionAttributes?.get("userId") as? Long
            ?: throw RuntimeException()

        // TODO: custom exception
        val userNickname = headerAccessor.sessionAttributes?.get("userNickname") as? String
            ?: throw RuntimeException()

        val joinPlayerResponse = JoinPlayerResponse(
            playerId = userId.toString(),
            username = userNickname,
            x = 100.0,
            y = 100.0,
        )

        messagingTemplate.convertAndSend("/topic/space/join", joinPlayerResponse)
    }

    // TODO: refactor or fix
    @MessageMapping("/space/move")
    fun handleMove(
        headerAccessor: SimpMessageHeaderAccessor,
        moveRequest: MoveRequest,
    ) {
        // TODO: custom exception
        val userId = headerAccessor.sessionAttributes?.get("userId") as? Long
            ?: throw RuntimeException()

        // TODO: custom exception
        val userNickname = headerAccessor.sessionAttributes?.get("userNickname") as? String
            ?: throw RuntimeException()

        val moveResponse = MoveResponse(
            playerId = userId.toString(),
            x = moveRequest.x,
            y = moveRequest.y,
            type = moveRequest.type,
            username = userNickname
        )

        messagingTemplate.convertAndSend("/topic/space/move", moveResponse)
    }
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
