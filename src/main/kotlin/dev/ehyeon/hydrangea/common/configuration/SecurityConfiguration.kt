package dev.ehyeon.hydrangea.common.configuration

import dev.ehyeon.hydrangea.common.constant.Environment
import dev.ehyeon.hydrangea.common.filter.AuthenticationFilter
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
    private val authenticationFilter: AuthenticationFilter,
) {
    @Bean
    @Profile(Environment.LOCAL)
    fun localFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors ->
                cors.configurationSource {
                    val corsConfiguration = CorsConfiguration()

                    // TODO: 프로퍼티로 분리
                    corsConfiguration.allowedOrigins = listOf("http://localhost:3000")
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
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/tour-api/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
