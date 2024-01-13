package com.project.university.specializedproject.bktechstore.model

import com.project.university.specializedproject.bktechstore.getFirstWord
import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*
import org.springframework.data.annotation.Transient

@Table("product_line")
data class ProductLine(
    val name: String,
    val category: String?,
    val supplier: String,
    val description: String,
    @Id @Column("product_id") val id: Int? = null
)

@Table("product")
class Product(@Id @Column("product_id") val id: Int? = null) {
    lateinit var name: String
    var quantity : Int = 0
    var productLine: Int? = null
    var price: Int? = null
    @Column("local_specs") var spec: Json = Json.of("")
    var images = mutableListOf<String>()
        set(value) {
            field.addAll(value)
        }
    var isStandard: Boolean = false
    lateinit var sku: String
    @Transient val reviews = mutableListOf<Review>()

    companion object {
        fun copy(it: Product) = Product(it.id).apply {
            quantity = it.quantity
            productLine = it.quantity
            price = it.price
            images.addAll(it.images)
            isStandard = it.isStandard
            sku = it.sku
        }
    }
}

@Table("review")
data class Review(
    @Transient @Id val id: Pair<Int, UUID>,
    val owner: UUID,
    val product: Int,
    val rating: Int,
    val comment: String,
    val postedAt: LocalDateTime,
    val images: MutableList<String> = mutableListOf()
)

data class ReviewId(val productId: Int, val owner: UUID)

@Table("supplier")
data class Supplier(
    @Id val code: String,
    val name: String = code.getFirstWord()
)

@Table("category")
class Category(@Id val name: String) {
    val parent: Category? = null
    val categorySchema: Json? = null
}
