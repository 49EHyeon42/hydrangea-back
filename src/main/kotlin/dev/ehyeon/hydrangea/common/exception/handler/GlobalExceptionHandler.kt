package dev.ehyeon.hydrangea.common.exception.handler

import dev.ehyeon.hydrangea.common.response.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidationException(
        exception: HandlerMethodValidationException,
    ): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity
            .badRequest()
            .body(BaseResponse.failure(message = exception.message))
    }

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
