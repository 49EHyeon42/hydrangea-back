package dev.ehyeon.hydrangea.space.controller

import dev.ehyeon.hydrangea.space.request.MoveUserRequest
import dev.ehyeon.hydrangea.space.request.SendMessageRequest
import dev.ehyeon.hydrangea.space.response.JoinUserResponse
import dev.ehyeon.hydrangea.space.response.MoveUserResponse
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

    @MessageMapping("/spaces/users/move")
    fun moveUser(
        headerAccessor: SimpMessageHeaderAccessor,
        request: MoveUserRequest,
    ) {
        val userId = findUserId(headerAccessor)
        val userNickname = findUserNickname(headerAccessor)

        val response = MoveUserResponse(
            userId = userId,
            userNickname = userNickname,
            x = request.x,
            y = request.y,
        )

        // TODO-NOTE: 전체가 아닌 일부만 보내는 방법 필요
        messagingTemplate.convertAndSend("/topic/spaces/users/move", response)
    }

    // TODO: leaveUser

    @MessageMapping("/spaces/chat")
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

        // TODO: destination 분리?
        messagingTemplate.convertAndSend(
            "/topic/spaces/chat",
            SendMessageResponse(
                messageId = savedMessageId,
                senderId = userId,
                senderNickname = userNickname,
                content = request.content,
            )
        )
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
