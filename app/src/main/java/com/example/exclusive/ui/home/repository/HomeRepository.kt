package com.example.exclusive.ui.home.repository

import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.model.Brand

interface HomeRepository {
    suspend fun getBrands(): List<Brand>
    suspend fun getPriceRules(): PriceRulesResponse
    suspend fun getCouponDetails(id: Long): CouponsDetails
}