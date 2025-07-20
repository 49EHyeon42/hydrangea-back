package dev.ehyeon.hydrangea.space.response

data class JoinUserResponse(
    val userId: Long,
    val userNickname: String,
    val initialX: Double,
    val initialY: Double,
)
