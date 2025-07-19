package dev.ehyeon.hydrangea.space.repository

import dev.ehyeon.hydrangea.space.document.MessageMongoDbDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface MessageMongoDbRepository : MongoRepository<MessageMongoDbDocument, String>
