package com.project.university.specializedproject.bktechstore.repository.impl

import com.project.university.specializedproject.bktechstore.model.Product
import com.project.university.specializedproject.bktechstore.repository.CustomizedProductRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Repository

@Repository
class CustomizedProductRepositoryImpl(private val template: R2dbcEntityTemplate) : CustomizedProductRepository{

    override suspend fun findById(id: Long): Product {
        TODO("Not yet implemented")
    }

    override fun searchForProducts(searchStr: String, pageable: Pageable?): Flow<Product> {
        TODO("Not yet implemented")
    }

}