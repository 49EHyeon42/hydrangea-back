package dev.ehyeon.hydrangea.advertising.controller

import dev.ehyeon.hydrangea.advertising.exception.SafetyDreamOpenApiException
import dev.ehyeon.hydrangea.advertising.restclient.SafetyDreamOpenApiRestClient
import dev.ehyeon.hydrangea.common.response.BaseResponse
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/advertising/safety-dream")
class SafetyDreamOpenApiController(
    private val restClient: SafetyDreamOpenApiRestClient,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/lcm/amberListTForm")
    fun findAmberListTForm(
        @RequestParam
        @Pattern(regexp = "^(01|02|03)$")
        viewType: String,
    ): ResponseEntity<BaseResponse<String>> {
        val response = restClient.amberListTForm(viewType)

        return ResponseEntity.ok(BaseResponse.success(response))
    }

    @GetMapping("/lcm/amberListTFormHtml")
    fun findAmberListTFormHtml(
        @RequestParam
        @Pattern(regexp = "^(01|02|03)$")
        viewType: String,
    ): ResponseEntity<String> {
        val response = restClient.amberListTForm(viewType)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("text/html; charset=UTF-8"))
            .body(response)
    }

    @ExceptionHandler(SafetyDreamOpenApiException::class)
    fun handleSafetyDreamOpenApiException(
        exception: SafetyDreamOpenApiException,
    ): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(BaseResponse.failure(message = exception.message))
    }
}
