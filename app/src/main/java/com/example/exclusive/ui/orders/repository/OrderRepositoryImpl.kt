package com.example.exclusive.ui.orders.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.MyOrder
import com.example.exclusive.ui.checkout.repository.CheckoutRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : OrderRepository {
    override suspend fun getAllOrders(customerAccessToken: String): List<MyOrder> {
       return remoteDataSource.getAllOrders(customerAccessToken)
    }

    override suspend fun getToken(): String {
        return localDataSource.readToken()!!
    }

}