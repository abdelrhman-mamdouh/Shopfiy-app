package com.example.exclusive.ui.products.repository

import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.ProductNode
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val remoteDataSource: ShopifyRemoteDataSource) :
    ProductsRepository {
    override suspend fun getProducts(vendor: String): List<ProductNode> {
        return remoteDataSource.getProducts(vendor)
    }
}