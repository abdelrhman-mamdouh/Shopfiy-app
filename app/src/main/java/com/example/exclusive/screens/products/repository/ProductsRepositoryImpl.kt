package com.example.exclusive.screens.products.repository

import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.MyProduct

class ProductsRepositoryImpl(private val remoteDataSource: ShopifyRemoteDataSource) : ProductsRepository {
    override suspend fun getProducts(vendor: String): List<MyProduct> {
        return remoteDataSource.getProducts(vendor)
    }
}