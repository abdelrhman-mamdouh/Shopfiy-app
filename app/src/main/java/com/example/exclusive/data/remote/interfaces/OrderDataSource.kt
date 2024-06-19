package com.example.exclusive.data.remote.interfaces

import com.example.exclusive.model.MyOrder

interface OrderDataSource {
    suspend fun getAllOrders(customerAccessToken: String): List<MyOrder>
}