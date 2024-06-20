package com.example.fake

import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.network.ApiService
import retrofit2.Response

class FakeApiService : ApiService {
    var shouldReturnError = false
    var fakeCurrenciesResponse: Currencies? = null

    override suspend fun getCurrencies(base: String, apiKey: String): Response<Currencies> {
        return if (shouldReturnError) {
            Response.error(404, okhttp3.ResponseBody.create(null, "Error"))
        } else {
            fakeCurrenciesResponse?.let {
                Response.success(it)
            } ?: Response.error(500, okhttp3.ResponseBody.create(null, "Internal Server Error"))
        }
    }
}