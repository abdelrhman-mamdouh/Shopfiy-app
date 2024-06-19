package com.example.exclusive.data.remote.interfaces

import com.example.exclusive.model.Brand
import com.example.exclusive.model.ProductNode

interface ProductDataSource {
    suspend fun getBrands(): List<Brand>
    suspend fun getCategories(): List<Brand>
    suspend fun getProducts(vendor: String): List<ProductNode>
    suspend fun getAllProducts(): List<ProductNode>
}