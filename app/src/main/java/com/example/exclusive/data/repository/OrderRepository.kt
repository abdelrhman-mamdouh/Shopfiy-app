package com.example.exclusive.data.repository

import com.example.exclusive.data.model.MyOrder


interface OrderRepository {
    suspend fun getAllOrders(customerAccessToken: String): List<MyOrder>
    suspend fun getToken():String
}