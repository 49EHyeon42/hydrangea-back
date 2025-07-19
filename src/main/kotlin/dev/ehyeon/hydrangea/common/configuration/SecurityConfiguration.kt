package dev.ehyeon.hydrangea.common.configuration

import dev.ehyeon.hydrangea.common.constant.EnvironmentConstant
import dev.ehyeon.hydrangea.common.filter.AuthenticationFilter
import dev.ehyeon.hydrangea.common.property.CorsProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
class SecurityConfiguration(
    private val corsProperty: CorsProperty,
    private val authenticationFilter: AuthenticationFilter,
) {
    @Bean
    @Profile(EnvironmentConstant.LOCAL)
    fun localFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors ->
                cors.configurationSource {
                    val corsConfiguration = CorsConfiguration()

                    corsConfiguration.allowedOrigins = corsProperty.allowedOrigins
                    corsConfiguration.addAllowedHeader("*")
                    corsConfiguration.addAllowedMethod("*")
                    corsConfiguration.allowCredentials = true

                    corsConfiguration
                }
            }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/error").permitAll()
                    .requestMatchers("/api/auth/me").authenticated()
                    .requestMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
