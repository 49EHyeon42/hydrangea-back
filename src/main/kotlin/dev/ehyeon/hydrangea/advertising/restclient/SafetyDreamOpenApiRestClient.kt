package dev.ehyeon.hydrangea.advertising.restclient

import dev.ehyeon.hydrangea.advertising.exception.SafetyDreamOpenApiException
import dev.ehyeon.hydrangea.common.property.SafetyDreamOpenApiProperty
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class SafetyDreamOpenApiRestClient(
    private val property: SafetyDreamOpenApiProperty,
    private val restClient: RestClient,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun amberListTForm(viewType: String): String {
        val fromUri = URI.create(property.lcm.amberListTForm.uri)

        val uri = UriComponentsBuilder
            .fromUri(fromUri)
            .queryParam("esntlId", property.lcm.amberListTForm.essentialId)
            .queryParam("authKey", property.lcm.amberListTForm.authenticationKey)
            .queryParam("viewType", viewType)
            .build()
            .toUri()

        val response = executeAndExtract(uri, String::class.java)

        return response
    }

    private fun <T> executeAndExtract(
        uri: URI,
        responseClass: Class<T>,
    ): T {
        try {
            return restClient.post()
                .uri(uri)
                .retrieve()
                .body(responseClass)
                ?: throw SafetyDreamOpenApiException()
        } catch (exception: RestClientException) {
            logger.error(exception.message)

            throw SafetyDreamOpenApiException(cause = exception)
        }
    }
}
