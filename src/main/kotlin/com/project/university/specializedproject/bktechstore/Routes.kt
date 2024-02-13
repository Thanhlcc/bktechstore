package com.project.university.specializedproject.bktechstore

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter
@Configuration(proxyBeanMethods = false)
class Routes {
    @Bean
    fun mainRouter(productHandler: ProductHandler) = coRouter {
        add(productRouter(productHandler))
    }

    private fun productRouter(productHandler: ProductHandler) = coRouter {
        "/products".nest {
            GET("/", productHandler::retrieveCatalog)
            POST("/", productHandler::create)
            GET("/{id}", productHandler::getDetail)
            DELETE("/{id}", productHandler::remove)
        }
    }
}