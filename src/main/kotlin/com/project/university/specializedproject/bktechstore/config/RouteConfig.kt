package com.project.university.specializedproject.bktechstore.config

import com.project.university.specializedproject.bktechstore.handler.ProductHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class RouteConfig(
    private val productHandler: ProductHandler
) {
    @Bean
    fun mainRoute(): RouterFunction<ServerResponse> = coRouter {
        ("/v1").nest {
            add(productRouter)
            add(userRouter)
        }
//        add(customerRouter)
        // Fall-back api for not found exception
        path("*") {
            ServerResponse.notFound().buildAndAwait()
        }
    }

    private val userRouter = coRouter {
        ("/users").nest {
            GET("/me") {
                ServerResponse.ok().bodyValueAndAwait("This endpoint is for personal profile")
            }
            GET("") {
                ServerResponse.ok().bodyValueAndAwait("this endpoint will be implemented")
            }
        }
    }
    private val productRouter = coRouter {
        ("/products").nest {
            GET("/{id}", productHandler::getProductDetail)
            GET("/", productHandler::retrieveCatalog)
        }
    }

//    private val customerRouter = coRouter {
//        ("/cart").nest {
//            GET("")
//            POST("/add")
//            DELETE("/remove")
//        }
//        ("/orders").nest {
//            GET("")
//        }
//        ("/wishlist").nest {
//            GET("")
//            POST("/add")
//            DELETE("/remove")
//        }
//    }
}