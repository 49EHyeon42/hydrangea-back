package dev.ehyeon.hydrangea.message.controller

import dev.ehyeon.hydrangea.message.document.MessageMongoDbDocument
import dev.ehyeon.hydrangea.message.repository.MessageMongoDbRepository
import dev.ehyeon.hydrangea.message.request.SendMessageRequest
import dev.ehyeon.hydrangea.message.response.SendMessageResponse
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller

@Controller
class MessageController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val messageRepository: MessageMongoDbRepository,
) {
    @MessageMapping("/message")
    fun handleMessage(request: SendMessageRequest) {
        val saved = messageRepository.save(
            MessageMongoDbDocument(
                senderId = request.senderId,
                senderNickname = request.senderNickname,
                content = request.content
            )
        )

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
