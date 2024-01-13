package com.project.university.specializedproject.bktechstore.repository

import com.project.university.specializedproject.bktechstore.model.Product
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Example
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProductRepository : CoroutineCrudRepository<Product, Int>, CustomizedProductRepository{
    fun findAllBy(example: Example<Product>?, pageable: Pageable) : Flow<Product>
    fun findAllBy(pageable: Pageable? = null) : Flow<Product>
}
interface CustomizedProductRepository {
    suspend fun findById(id: Long) : Product
    fun searchForProducts(searchStr: String, pageable: Pageable?) : Flow<Product>
}

