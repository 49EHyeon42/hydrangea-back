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

        // TODO: custom exception
        val userNickname = headerAccessor.sessionAttributes?.get("userNickname") as? String
            ?: throw RuntimeException()

        // TODO: 서비스, 레포 분리
        val saved = messageRepository.save(
            MessageMongoDbDocument(
                senderId = userId,
                senderNickname = userNickname,
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
