package com.project.university.specializedproject.bktechstore.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("orders")
data class Order(
    @Id @Column("order_id") val id : UUID? = null,
    @Column("shipping_addr") val shippingAddress: Address,
    val ownerId: UUID,
    val status: OrderStatus = OrderStatus.UNRESOLVED,
    val createdAt: LocalDateTime? = null,
    val lastUpdated: LocalDateTime? = LocalDateTime.now(),
    val items: MutableSet<OrderItem> = mutableSetOf()
)

enum class OrderStatus {
    UNRESOLVED, PROCESSING, SHIPPING, SUCCESS, CANCELED
}

@Table("order_item")
data class OrderItem(
    @Id @Column("order_id") val id: UUID,
    val productId : Int,
    val quantity: Int = 1,
)