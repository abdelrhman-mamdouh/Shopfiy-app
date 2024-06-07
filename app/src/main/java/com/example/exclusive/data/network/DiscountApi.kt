package com.example.exclusive.data.network

import com.example.exclusive.data.model.CouponsDetails
import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.model.PriceRulesResponse
import com.example.exclusive.utilities.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscountApi {

    @GET("admin/api/2024-04/price_rules.json")
    suspend fun getPriceRules(
        @Header("X-Shopify-Access-Token") accessToken: String
    ): Response<PriceRulesResponse>

    @GET("admin/api/2024-04/price_rules/{priceRuleId}/discount_codes.json")
    suspend fun getCouponById(
        @Header("X-Shopify-Access-Token") accessToken: String = Constants.ACCESS_TOKEN,
        @Path("priceRuleId") id: Long
    ): Response<CouponsDetails>
}