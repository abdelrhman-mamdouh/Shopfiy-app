package com.example.exclusive.data.remote

import com.example.exclusive.model.Brand
import com.example.exclusive.model.MyProduct
import kotlinx.coroutines.flow.Flow


interface ShopifyRemoteDataSource {
    suspend fun getBrands(): List<Brand>
    suspend fun getCategories(): List<Brand>
    suspend fun getProducts(vendor: String): List<MyProduct>
}
