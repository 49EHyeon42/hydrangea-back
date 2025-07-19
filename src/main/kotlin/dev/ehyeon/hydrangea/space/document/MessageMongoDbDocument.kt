package dev.ehyeon.hydrangea.space.document

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document("message")
class MessageMongoDbDocument(
    val senderId: Long,

    val senderNickname: String,

    val content: String,
) {
    @Id
    private lateinit var _id: String

    val id: String
        get() = _id

    @CreatedDate
    @Field("created_date")
    private lateinit var _createdDate: LocalDateTime

    val createdDate: LocalDateTime
        get() = _createdDate
}
