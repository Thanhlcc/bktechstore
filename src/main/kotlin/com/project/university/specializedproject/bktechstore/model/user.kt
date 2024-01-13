package com.project.university.specializedproject.bktechstore.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.Date
import java.util.UUID



@Table("user")
open class Account(
    val username: String,
    val password: String,
    val email: String,
    @Id @Column("account_id") val id: UUID? = null
){
    @Column("phonenumber") val phoneNumber: String? = null
    val dob: Date? = null
    val gender: Gender? = null
    val isActive: Boolean = false
}

@Table("admin")
class Admin(
    username: String,
    password: String,
    email: String,
    val employeeId: String,
) : Account(username, password, email)

@Table("customer")
class Customer(
    username: String,
    password: String,
    email: String,
    @Transient val orders: MutableList<Order> = mutableListOf(),
    @Transient val wishlist: MutableSet<Product> = mutableSetOf(),
    @Transient val addresses: MutableSet<Address> = mutableSetOf(),
): Account(username, email, password)

enum class Gender {
    F, // Female
    M // Male
}