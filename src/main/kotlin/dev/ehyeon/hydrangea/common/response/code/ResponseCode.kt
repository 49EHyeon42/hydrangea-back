package dev.ehyeon.hydrangea.common.response.code

enum class ResponseCode(val code: String, val defaultMessage: String) {
    SUCCESS("SUCCESS", "SUCCESS"),
    FAILURE("FAILURE", "FAILURE"),
}
