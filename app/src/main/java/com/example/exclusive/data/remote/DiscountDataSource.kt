package com.example.exclusive.data.remote

import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.PriceRulesResponse

interface DiscountDataSource {
    suspend fun getPriceRules(): PriceRulesResponse
    suspend fun getCouponDetails(id: Long): CouponsDetails
}
