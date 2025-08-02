package dev.ehyeon.hydrangea.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "safety-dream-open-api")
@Validated
class SafetyDreamOpenApiProperty(
    val lcm: Lcm,
) {
    data class Lcm(
        val amberListTForm: AmberListTForm,
    ) {
        data class AmberListTForm(
            val uri: String,
            val essentialId: String,
            val authenticationKey: String,
        )
    }
}
