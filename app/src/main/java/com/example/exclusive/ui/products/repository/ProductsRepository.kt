package com.example.exclusive.ui.products.repository

import com.example.exclusive.model.ProductNode


interface ProductsRepository {
    suspend fun getProducts(vendor: String): List<ProductNode>
}