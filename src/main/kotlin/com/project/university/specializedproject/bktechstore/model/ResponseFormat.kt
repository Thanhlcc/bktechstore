package com.project.university.specializedproject.bktechstore.model

data class ResponseFormat<T> (
    val success: Boolean = true,
    val data: T? = null,
    val message: String? = null
)