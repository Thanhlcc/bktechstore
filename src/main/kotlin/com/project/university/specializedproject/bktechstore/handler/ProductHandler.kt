package com.project.university.specializedproject.bktechstore.handler

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.project.university.specializedproject.bktechstore.model.Product
import com.project.university.specializedproject.bktechstore.model.ResponseFormat
import com.project.university.specializedproject.bktechstore.model.Review
import com.project.university.specializedproject.bktechstore.repository.ProductRepository
import io.r2dbc.postgresql.codec.Json
import kotlinx.coroutines.flow.map
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
class ProductHandler(private val productRepository: ProductRepository) {
    private val log = LoggerFactory.getLogger(ProductHandler::class.java)
    suspend fun retrieveCatalog(request: ServerRequest): ServerResponse {
        val pageable: Pageable = PageRequest.of(
            request.queryParamOrNull("offset")?.toInt() ?: 0,
            request.queryParamOrNull("size")?.toInt() ?: 10
        )
        return with(request.awaitBodyOrNull<Product>()) {
            val example: Example<Product>? = if (this != null) {
                Example.of(
                    Product.copy(this),
                    ExampleMatcher.matchingAll().withIgnorePaths("reviews", "images", "quantity")
                )
            } else null
            ok().bodyAndAwait(
                productRepository.findAllBy(example, pageable)
                    .map { it.toDTO() }
                    .map {data -> ResponseFormat<ProductDTO>(data=data)}
            )
        }
    }

    suspend fun getProductDetail(request: ServerRequest): ServerResponse {
        try {
            val id  = request.pathVariable("id").toInt()
            val product  = productRepository.findById(id)
                ?: return ServerResponse.badRequest().bodyValueAndAwait(ResponseFormat<String>(success = false, message="Product id:$id does not exist"))
            val response = ResponseFormat<ProductDTO>(data=product.toDTO())
            return ok().bodyValueAndAwait(response)
        }
        catch (exception: IllegalArgumentException){
            return ServerResponse.badRequest().bodyValueAndAwait("The path needs id variable")
        }
    }

    data class ProductDTO(
        val quantity: Int,
        val productLine: Int,
        val price: Int,
        val spec: Map<String, String>,
        val images: List<String>,
        val isStandard: Boolean = false,
        val sku: String,
        val reviews: List<Review>? = null,
        val name: String,
    )

    fun Product.toDTO(): ProductDTO {
        val typeRef = object : TypeReference<Map<String, String>>() {}
        val specification = ObjectMapper().readValue(spec.asString(), typeRef)
        return ProductDTO(
            quantity = quantity,
            productLine = productLine!!,
            price = price!!,
            spec = specification,
            images = images,
            isStandard = isStandard,
            sku = sku,
            reviews = reviews.toList(),
            name = name
        )
    }

    fun Product.fromDTO() = Product().apply {
        quantity = quantity
        productLine = productLine
        price = price
        spec = Json.of(spec.toString())
        images = images
        isStandard = isStandard
        sku = sku
    }
}
