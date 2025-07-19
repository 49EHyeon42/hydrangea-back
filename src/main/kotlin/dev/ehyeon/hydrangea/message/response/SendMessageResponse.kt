package dev.ehyeon.hydrangea.message.response

data class SendMessageResponse(
    val messageId: String,
    val senderId: Long,
    val senderNickname: String,
    val content: String,
)
