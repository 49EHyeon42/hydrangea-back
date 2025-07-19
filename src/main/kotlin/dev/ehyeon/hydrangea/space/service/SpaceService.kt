package dev.ehyeon.hydrangea.space.service

import dev.ehyeon.hydrangea.space.document.MessageMongoDbDocument
import dev.ehyeon.hydrangea.space.repository.MessageMongoDbRepository
import org.springframework.stereotype.Service

@Service
class SpaceService(
    private val messageRepository: MessageMongoDbRepository,
) {
    fun saveMessage(senderId: Long, senderNickname: String, content: String): String {
        val savedMessage = messageRepository.save(MessageMongoDbDocument(senderId, senderNickname, content))

        return savedMessage.id
    }
}
