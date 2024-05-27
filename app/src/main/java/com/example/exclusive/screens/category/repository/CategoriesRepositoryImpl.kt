package com.example.exclusive.screens.category.repository

import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.Brand
import com.example.exclusive.screens.home.repository.HomeRepository

class CategoriesRepositoryImpl(private val remoteDataSource: ShopifyRemoteDataSource) : CategoriesRepository {
    override suspend fun getCategories(): List<Brand> {
        return remoteDataSource.getCategories()
    }
}