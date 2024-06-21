package com.example.exclusive.data.remote

import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.OrderRequest
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.model.MyOrder

interface AdminRemoteDataSource {
    suspend fun getPriceRules(): PriceRulesResponse
    suspend fun getCouponDetails(id: Long): CouponsDetails
    suspend fun createOrder(orderRequest: OrderRequest): MyOrder
}
