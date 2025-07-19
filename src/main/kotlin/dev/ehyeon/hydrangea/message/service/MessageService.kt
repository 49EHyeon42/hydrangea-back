package dev.ehyeon.hydrangea.message.service

import dev.ehyeon.hydrangea.message.document.MessageMongoDbDocument
import dev.ehyeon.hydrangea.message.repository.MessageMongoDbRepository
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageRepository: MessageMongoDbRepository,
) {
    fun saveMessage(senderId: Long, senderNickname: String, content: String): String {
        val savedMessage = messageRepository.save(MessageMongoDbDocument(senderId, senderNickname, content))

        return savedMessage.id
    }
}
