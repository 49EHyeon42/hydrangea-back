package dev.ehyeon.hydrangea.message.controller

import dev.ehyeon.hydrangea.message.repository.MessageMongoDbRepository
import dev.ehyeon.hydrangea.message.request.SendMessageRequest
import dev.ehyeon.hydrangea.message.response.SendMessageResponse
import dev.ehyeon.hydrangea.message.service.MessageService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class MessageController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val messageService: MessageService,
    private val messageRepository: MessageMongoDbRepository,
) {
    @MessageMapping("/message")
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

        val savedMessageId = messageService.saveMessage(
            senderId = userId,
            senderNickname = userNickname,
            content = request.content
        )

        // TODO: destination 분리
        messagingTemplate.convertAndSend(
            "/topic/message",
            SendMessageResponse(
                messageId = savedMessageId,
                senderId = userId,
                senderNickname = userNickname,
                content = request.content,
            )
        )
    }
}
