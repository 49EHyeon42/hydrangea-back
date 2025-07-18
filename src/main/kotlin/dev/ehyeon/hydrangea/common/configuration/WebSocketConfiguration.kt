package dev.ehyeon.hydrangea.common.configuration

import dev.ehyeon.hydrangea.common.handshakeinterceptor.AuthHandshakeInterceptor
import dev.ehyeon.hydrangea.common.property.CorsProperty
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration(
    private val corsProperty: CorsProperty,
    private val authHandshakeInterceptor: AuthHandshakeInterceptor,
) : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns(*corsProperty.allowedOrigins.toTypedArray())
            .addInterceptors(authHandshakeInterceptor)
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableSimpleBroker("/topic")
    }
}
