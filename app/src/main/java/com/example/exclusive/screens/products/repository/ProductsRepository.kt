package com.example.exclusive.screens.products.repository


import com.example.exclusive.model.MyProduct


interface ProductsRepository {
    suspend fun getProducts(vendor: String): List<MyProduct>
}