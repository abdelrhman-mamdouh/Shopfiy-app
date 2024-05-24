package com.example.exclusive.data.remote

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.BrandsQuery
import com.example.exclusive.model.Brand
import com.example.exclusive.utilities.Constants.API_KEY
import com.example.exclusive.utilities.Constants.BASE_URL

object ApolloService {
    private val apolloClient = ApolloClient.Builder()
        .serverUrl(BASE_URL)
        .addHttpHeader("X-Shopify-Storefront-Access-Token", API_KEY)
        .build()

    suspend fun getBrands(): List<Brand> {
        val brands = mutableListOf<Brand>()

        try {
            val response: ApolloResponse<BrandsQuery.Data> =
                apolloClient.query(BrandsQuery()).execute()
            Log.i("TAG", "getBrands: ${response.data}")
            response.data?.collections?.edges?.forEach { brand ->
                val name = brand.node.title?: ""
                val imageUrl = brand.node.image?.originalSrc?: ""
                brands.add(Brand(name = name, imageUrl = imageUrl.toString()))
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return brands
    }
}
