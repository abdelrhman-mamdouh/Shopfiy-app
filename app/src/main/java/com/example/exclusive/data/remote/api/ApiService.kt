package com.example.exclusive.data.remote.api

import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.utilities.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("fetch-all")
    suspend fun getCurrencies(
        @Query("from") base : String = "EGP", @Query("api_key") apiKey : String = Constants.CURRENCY_API_KEY
    ): Response<Currencies>
}