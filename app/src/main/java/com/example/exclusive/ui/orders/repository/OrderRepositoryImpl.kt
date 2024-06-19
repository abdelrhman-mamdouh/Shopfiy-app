package com.example.exclusive.ui.orders.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.interfaces.OrderDataSource
import com.example.exclusive.model.MyOrder
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource,
    private val localDataSource: LocalDataSource
) : OrderRepository {
    override suspend fun getAllOrders(customerAccessToken: String): List<MyOrder> {
       return orderDataSource.getAllOrders(customerAccessToken)
    }

    override suspend fun getToken(): String {
        return localDataSource.readToken()!!
    }

}