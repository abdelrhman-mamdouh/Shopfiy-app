package com.example.exclusive.ui.home.repository


import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.data.remote.DiscountDataSource
import com.example.exclusive.data.remote.DiscountDataSourceImpl
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.Brand
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val myRemoteDataSource: DiscountDataSource,

    ) :
    HomeRepository {
    override suspend fun getBrands(): List<Brand> {
        return remoteDataSource.getBrands()
    }

    override suspend fun getPriceRules(): PriceRulesResponse {
        return myRemoteDataSource.getPriceRules()
    }

    override suspend fun getCouponDetails(id: Long): CouponsDetails {
        return myRemoteDataSource.getCouponDetails(id = id)
    }
}