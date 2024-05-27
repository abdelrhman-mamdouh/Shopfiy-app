package com.example.exclusive.screens.home.repository


import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.Brand

class HomeRepositoryImpl(private val remoteDataSource: ShopifyRemoteDataSource) : HomeRepository {
    override suspend fun getBrands(): List<Brand> {
        return remoteDataSource.getBrands()
    }
}