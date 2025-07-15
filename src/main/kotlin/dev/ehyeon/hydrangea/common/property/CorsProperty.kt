package dev.ehyeon.hydrangea.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "cors")
@Validated
data class CorsProperty(
    var allowedOrigins: List<String>,
)
