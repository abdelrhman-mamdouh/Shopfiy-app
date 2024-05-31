package com.example.exclusive.ui.products.repository


import com.example.exclusive.model.MyProduct


interface ProductsRepository {
    suspend fun getProducts(vendor: String): List<MyProduct>
}