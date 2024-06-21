package com.example.exclusive.data.remote

import android.util.Log
import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.OrderRequest
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.data.network.AdminServiceApi
import com.example.exclusive.model.MyOrder
import com.example.exclusive.utilities.Constants
import javax.inject.Inject

class AdminRemoteDataSourceImpl @Inject constructor(private val adminServiceApi: AdminServiceApi) : AdminRemoteDataSource {

    override suspend fun getPriceRules(): PriceRulesResponse {
        try {
            val accessToken = Constants.ACCESS_TOKEN

            val response = adminServiceApi.getPriceRules(accessToken)

            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("API_ERROR", errorBody ?: "Unknown error")

                throw Exception("Failed to get price rules: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Error fetching price rules: ${e.message}", e)
        }
    }

    override suspend fun getCouponDetails(id: Long): CouponsDetails {
        try {
            val response = adminServiceApi.getCouponById(id = id)
            Log.i("TAG", "getCouponDetails: $response")
            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Response body is null")
            } else {
                Log.i("TAG", "getCouponDetails: ${response.code()}")
                throw Exception("Failed to getCouponDetails: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Error getCouponDetails: ${e.message}", e)
        }
    }

    override suspend fun createOrder(orderRequest: OrderRequest): MyOrder {
        try {
            val accessToken = Constants.ACCESS_TOKEN
            val response = adminServiceApi.createOrder(accessToken, orderRequest)

            if (response.isSuccessful) {
                return response.body() ?: throw Exception("Response body is null")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody ?: "Unknown error"
                Log.e("API_ERROR", errorMessage)
                throw Exception("Failed to create order: ${response.code()}, Error: $errorMessage")
            }
        } catch (e: Exception) {
            throw Exception("Error creating order: ${e.message}", e)
        }
    }

}
