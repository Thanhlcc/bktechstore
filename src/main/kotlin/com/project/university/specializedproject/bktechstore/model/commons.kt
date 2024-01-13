package com.project.university.specializedproject.bktechstore.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("address")
data class Address(
    @Id @Column("address_id")    val id: Int? = null,
    val street: String,
    val city: String,
    val state: String?,
    val district: String,
    val zipCode: String?
)