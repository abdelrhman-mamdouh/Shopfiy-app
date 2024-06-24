package com.example.exclusive.ui.orders.repository

import com.example.exclusive.model.MyOrder


interface OrderRepository {
    suspend fun getAllOrders(customerAccessToken: String): List<MyOrder>
    suspend fun getToken():String
}