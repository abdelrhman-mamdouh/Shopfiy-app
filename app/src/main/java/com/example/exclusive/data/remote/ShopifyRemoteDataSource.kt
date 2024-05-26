package com.example.exclusive.data.remote

import com.example.exclusive.model.Brand
import kotlinx.coroutines.flow.Flow


interface ShopifyRemoteDataSource {
    suspend fun getBrands(): List<Brand>
}
