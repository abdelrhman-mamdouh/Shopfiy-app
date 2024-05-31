package com.example.exclusive.data.remote

import com.example.exclusive.model.Brand
import com.example.exclusive.model.ProductNode


interface ShopifyRemoteDataSource {
    suspend fun getBrands(): List<Brand>

    suspend fun getCategories(): List<Brand>
    suspend fun getProducts(vendor: String): List<ProductNode>

    suspend fun createCustomer(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Boolean

    suspend fun createCustomerAccessToken(email: String, password: String): String?
    suspend fun sendPasswordRecoveryEmail(email: String): Boolean
    suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean

}
