package com.example.exclusive.data.remote.interfaces

import com.example.exclusive.model.ProductNode

interface RealtimeDatabaseDataSource {

    fun addProductToRealtimeDatabase(product: ProductNode, accessToken: String)
    suspend fun fetchWatchlistProducts(accessToken: String): List<ProductNode>
    suspend fun deleteProduct(productId: String, accessToken: String): Boolean
}