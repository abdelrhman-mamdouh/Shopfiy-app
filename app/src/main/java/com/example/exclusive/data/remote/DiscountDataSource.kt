package com.example.exclusive.data.remote

import android.util.Log
import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.utilities.Constants
import javax.inject.Inject


class DiscountDataSource @Inject constructor(private val discountApi: DiscountApi) {

    suspend fun getPriceRules(): PriceRulesResponse {
        try {
            val accessToken = Constants.ACCESS_TOKEN

            val response = discountApi.getPriceRules(accessToken)

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

    suspend fun getCouponDetails(id: Long): CouponsDetails {
        try {
            val response = discountApi.getCouponById(id = id)
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

}