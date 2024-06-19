package com.example.exclusive.data.remote

import com.example.exclusive.data.remote.api.OrderService
import com.example.exclusive.data.remote.interfaces.OrderDataSource
import com.example.exclusive.model.MyOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderDataSourceImpl @Inject constructor(
    private val orderService: OrderService
) : OrderDataSource {

    override suspend fun getAllOrders(customerAccessToken: String): List<MyOrder> {
        return orderService.getAllOrders(customerAccessToken)
    }
}