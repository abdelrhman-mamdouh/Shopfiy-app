package com.example.exclusive.data.remote

import android.util.Log
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.data.network.DiscountApi
import com.example.exclusive.utilities.Constants
import javax.inject.Inject


class DiscountDataSource @Inject constructor(private val discountApi: DiscountApi) {

    suspend fun getPriceRules(): PriceRulesResponse {
        try {
            val accessToken = Constants.ACCESS_TOKEN

            val response = discountApi.getPriceRules(accessToken)
            Log.i("TAG", "getPriceRules: ${response}")
            if (response.isSuccessful) {

                return response.body() ?: throw Exception("Response body is null")
            } else {
                Log.i("TAG", "getPriceRules: ${response.code()}")
                throw Exception("Failed to get price rules: ${response.code()}")
            }
        } catch (e: Exception) {
            throw Exception("Error fetching price rules: ${e.message}", e)
        }
    }
}