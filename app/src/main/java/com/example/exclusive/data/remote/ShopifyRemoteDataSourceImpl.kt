// ShopifyRemoteDataSourceImpl.kt
package com.example.exclusive.data.remote

import com.example.exclusive.model.Brand
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopifyRemoteDataSourceImpl @Inject constructor(
    private val apolloService: ApolloService
) : ShopifyRemoteDataSource {

    override suspend fun getBrands(): List<Brand> {
        return apolloService.getBrands()
    }

    override suspend fun createCustomer(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Boolean {
        return apolloService.createCustomer(
            email = email,
            password = password,
            firstName = firstName,
            secondName = secondName
        )
    }

    override suspend fun createCustomerAccessToken(email: String, password: String): String? {
        return apolloService.createCustomerAccessToken(email = email, password = password)
    }

    override suspend fun sendPasswordRecoveryEmail(email: String): Boolean {
        return apolloService.sendPasswordRecoveryEmail(email)
    }

    override suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean {
        return apolloService.resetPasswordByUrl(resetUrl, newPassword)
    }
}
