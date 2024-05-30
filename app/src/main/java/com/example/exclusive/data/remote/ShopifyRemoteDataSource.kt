package com.example.exclusive.data.remote

import com.example.exclusive.model.Brand
import kotlinx.coroutines.flow.Flow


interface ShopifyRemoteDataSource {
    suspend fun getBrands(): List<Brand>

    suspend fun createCustomer(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Boolean
    suspend fun createCustomerAccessToken(email: String, password: String): String?
    suspend fun sendPasswordRecoveryEmail(email: String):Boolean
    suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String):Boolean
}
