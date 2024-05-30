package com.example.exclusive.data.remote

import com.example.exclusive.data.network.CurrencyApi
import com.example.exclusive.model.Brand
import com.example.exclusive.model.MyProduct
import javax.inject.Inject


class ShopifyRemoteDataSourceImpl @Inject constructor(private val apolloService: ApolloService): ShopifyRemoteDataSource {

    override suspend fun getBrands(): List<Brand> {
        return apolloService.getBrands()
    }

    override suspend fun getCategories(): List<Brand> {
        return apolloService.getCategories()
    }

    override suspend fun getProducts(vendor: String): List<MyProduct> {
        return apolloService.getProducts(vendor)
    }
}
