// ApolloService.kt
package com.example.exclusive.data.remote

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.BrandsQuery
import com.example.exclusive.model.Brand
import com.example.exclusive.CustomerAccessTokenCreateMutation
import com.example.exclusive.CustomerCreateMutation
import com.example.exclusive.ResetPasswordByUrlMutation
import com.example.exclusive.SendPasswordRecoverEmailMutation
import com.example.exclusive.type.CustomerAccessTokenCreateInput
import com.example.exclusive.type.CustomerCreateInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApolloService @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getBrands(): List<Brand> {
        val brands = mutableListOf<Brand>()

        try {
            val response: ApolloResponse<BrandsQuery.Data> =
                apolloClient.query(BrandsQuery()).execute()
            Log.i("TAG", "getBrands: ${response.data}")
            response.data?.collections?.edges?.forEach { brand ->
                val name = brand.node.title ?: ""
                val imageUrl = brand.node.image?.originalSrc ?: ""
                brands.add(Brand(name = name, imageUrl = imageUrl.toString()))
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return brands
    }

    suspend fun createCustomer(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Boolean {
        val mutation = CustomerCreateMutation(
            input = CustomerCreateInput(
                email = email,
                password = password,
                firstName = Optional.presentIfNotNull(firstName),
                lastName = Optional.presentIfNotNull(secondName)
            )
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val customer = response.data?.customerCreate?.customer
            val errors = response.data?.customerCreate?.customerUserErrors

            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")

                }
            } else if (customer != null) {
                Log.d("GraphQL", "Customer ID: ${customer.id}")
                return true
            }
        } catch (e: Exception) {
            Log.e("GraphQL", "Exception: ${e.message}", e)

        }
        return false
    }

    suspend fun createCustomerAccessToken(email: String, password: String): String? {
        val mutation = CustomerAccessTokenCreateMutation(
            input = CustomerAccessTokenCreateInput(
                email = email,
                password = password
            )
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val customerAccessToken = response.data?.customerAccessTokenCreate?.customerAccessToken
            val errors = response.data?.customerAccessTokenCreate?.customerUserErrors

            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else if (customerAccessToken != null) {

                Log.d("GraphQL", "Access Token: ${customerAccessToken.accessToken}")
                Log.d("GraphQL", "Expires At: ${customerAccessToken.expiresAt}")
                return customerAccessToken.accessToken
            }
        } catch (e: Exception) {
            Log.e("GraphQL", "Exception: ${e.message}", e)
        }
        return null
    }

    suspend fun sendPasswordRecoveryEmail(email: String): Boolean {
        val mutation = SendPasswordRecoverEmailMutation(
            email = email
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val errors = response.data?.customerRecover?.customerUserErrors

            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else {
                Log.d("GraphQL", "Password recovery email sent successfully")
                return true
            }
        } catch (e: Exception) {
            Log.e("GraphQL", "Exception: ${e.message}", e)
        }
        return false
    }

    suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean {
        val mutation = ResetPasswordByUrlMutation(
            resetUrl = resetUrl,
            password = newPassword
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val errors = response.data?.customerResetByUrl?.customerUserErrors

            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else {
                val customerId = response.data?.customerResetByUrl?.customer?.id
                Log.d("GraphQL", "Password reset successfully for customer ID: $customerId")
                return true
            }
        } catch (e: Exception) {
            Log.e("GraphQL", "Exception: ${e.message}", e)
        }
        return false
    }
}
