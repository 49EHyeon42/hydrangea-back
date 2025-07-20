package dev.ehyeon.hydrangea.space.response

data class JoinPlayerResponse(
    val playerId: String,
    val username: String,
    val x: Double,
    val y: Double,
)
