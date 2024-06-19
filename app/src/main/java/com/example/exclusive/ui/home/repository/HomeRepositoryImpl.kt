package com.example.exclusive.ui.home.repository


import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.data.remote.DiscountDataSource
import com.example.exclusive.data.remote.interfaces.ProductDataSource
import com.example.exclusive.model.Brand
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val productDataSource: ProductDataSource,
    private val myRemoteDataSource: DiscountDataSource,
) :
    HomeRepository {
    override suspend fun getBrands(): List<Brand> {
        return productDataSource.getBrands()
    }

    override suspend fun getPriceRules(): PriceRulesResponse {
        return myRemoteDataSource.getPriceRules()
    }

    override suspend fun getCouponDetails(id: Long): CouponsDetails {
        return myRemoteDataSource.getCouponDetails(id = id)
    }
}