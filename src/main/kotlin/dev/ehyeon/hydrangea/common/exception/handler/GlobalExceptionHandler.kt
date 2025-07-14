package dev.ehyeon.hydrangea.common.exception.handler

import dev.ehyeon.hydrangea.common.response.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
    ): ResponseEntity<BaseResponse<Unit>> {
        val errorMessage = exception.bindingResult
            .fieldErrors
            .joinToString("; ") { "${it.field}: ${it.defaultMessage ?: "Invalid"}" }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(BaseResponse.failure(message = errorMessage))
    }
}
