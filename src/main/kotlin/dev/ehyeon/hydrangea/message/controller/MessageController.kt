package dev.ehyeon.hydrangea.message.controller

import dev.ehyeon.hydrangea.message.document.MessageMongoDbDocument
import dev.ehyeon.hydrangea.message.repository.MessageMongoDbRepository
import dev.ehyeon.hydrangea.message.request.SendMessageRequest
import dev.ehyeon.hydrangea.message.response.SendMessageResponse
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class MessageController(
    private val messagingTemplate: SimpMessagingTemplate,
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

        val saved = messageRepository.save(
            MessageMongoDbDocument(
                senderId = userId,
                senderNickname = request.senderNickname,
                content = request.content
            )
        )

        // TODO: destination 분리
        messagingTemplate.convertAndSend(
            "/topic/message",
            SendMessageResponse(
                id = saved.id,
                senderNickname = saved.senderNickname,
                content = saved.content,
            )
        )
    }
}
