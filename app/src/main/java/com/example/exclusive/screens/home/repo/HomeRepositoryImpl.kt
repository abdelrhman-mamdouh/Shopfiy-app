package com.example.exclusive.screens.home.repo


import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.Brand

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(private val remoteDataSource: ShopifyRemoteDataSource) : HomeRepository {
    override suspend fun getBrands(): List<Brand> {
        return remoteDataSource.getBrands()
    }
}