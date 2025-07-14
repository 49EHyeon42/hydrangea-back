package dev.ehyeon.hydrangea.message.response

data class SendMessageResponse(
    val id: String,
    val senderNickname: String,
    val content: String,
)
