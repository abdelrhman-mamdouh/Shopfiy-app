package com.example.exclusive.data.remote

import com.example.exclusive.model.Brand


object ShopifyRemoteDataSourceImpl : ShopifyRemoteDataSource {
    private val apolloService: ApolloService by lazy {
        ApolloService
    }

    override suspend fun getBrands(): List<Brand> {
        return apolloService.getBrands()
    }
}