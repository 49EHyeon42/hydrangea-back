package dev.ehyeon.hydrangea.message.repository

import dev.ehyeon.hydrangea.message.document.MessageMongoDbDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface MessageMongoDbRepository : MongoRepository<MessageMongoDbDocument, String>
