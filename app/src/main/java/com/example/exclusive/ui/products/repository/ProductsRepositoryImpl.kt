package com.example.exclusive.ui.products.repository

import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.MyProduct
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val remoteDataSource: ShopifyRemoteDataSource) : ProductsRepository {
    override suspend fun getProducts(vendor: String): List<MyProduct> {
        return remoteDataSource.getProducts(vendor)
    }
}