package dev.ehyeon.hydrangea.advertising.controller

import dev.ehyeon.hydrangea.advertising.restclient.SafetyDreamOpenApiRestClient
import dev.ehyeon.hydrangea.common.response.BaseResponse
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}
