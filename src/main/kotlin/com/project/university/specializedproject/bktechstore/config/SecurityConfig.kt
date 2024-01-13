package com.project.university.specializedproject.bktechstore.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
@Configuration
class SecurityConfig {
    @Bean
    fun corsWebFilter(): CorsWebFilter {
        val corsConfig = CorsConfiguration().apply {
            addAllowedOrigin(CorsConfiguration.ALL)
            addAllowedMethod(CorsConfiguration.ALL)
            addAllowedHeader(CorsConfiguration.ALL)
        }
        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfig)
        }
        println("Allowed origins: ${corsConfig.allowedOrigins}")
        return CorsWebFilter(source)
    }

}
