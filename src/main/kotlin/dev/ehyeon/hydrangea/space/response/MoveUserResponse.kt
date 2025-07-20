package dev.ehyeon.hydrangea.space.response

data class MoveUserResponse(
    val userId: Long,
    val userNickname: String,
    val x: Double,
    val y: Double,
)
