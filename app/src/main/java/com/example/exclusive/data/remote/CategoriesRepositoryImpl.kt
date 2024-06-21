package com.example.exclusive.data.remote

import com.example.exclusive.data.remote.interfaces.CategoriesRepository
import com.example.exclusive.data.remote.interfaces.ProductDataSource
import com.example.exclusive.data.model.Brand
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(private val productDataSource: ProductDataSource) :
    CategoriesRepository {
    override suspend fun getCategories(): List<Brand> {
        return productDataSource.getCategories()
    }
}