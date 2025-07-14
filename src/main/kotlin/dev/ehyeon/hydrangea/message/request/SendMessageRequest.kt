package dev.ehyeon.hydrangea.message.request

// TODO: 유효성 감사
data class SendMessageRequest(
    val senderId: Long,
    val senderNickname: String,
    val content: String,
)
