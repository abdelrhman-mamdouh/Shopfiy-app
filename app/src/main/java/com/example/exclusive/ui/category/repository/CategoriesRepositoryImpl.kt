package com.example.exclusive.ui.category.repository

import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.Brand
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(private val remoteDataSource: ShopifyRemoteDataSource) :
    CategoriesRepository {
    override suspend fun getCategories(): List<Brand> {
        return remoteDataSource.getCategories()
    }
}