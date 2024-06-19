package com.example.exclusive.data.remote

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.exclusive.AddAddressToCustomerMutation
import com.example.exclusive.CustomerAccessTokenCreateMutation
import com.example.exclusive.CustomerCreateMutation
import com.example.exclusive.DeleteCustomerAddressMutation
import com.example.exclusive.GetCustomerAddressesQuery
import com.example.exclusive.ResetPasswordByUrlMutation
import com.example.exclusive.SendPasswordRecoverEmailMutation
import com.example.exclusive.model.AddressInput
import com.example.exclusive.type.CustomerAccessTokenCreateInput
import com.example.exclusive.type.CustomerCreateInput
import com.example.exclusive.type.MailingAddressInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerService @Inject constructor(private val apolloClient: ApolloClient) {
    suspend fun createCustomer(
        email: String, password: String, firstName: String, secondName: String
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

            if (!errors.isNullOrEmpty()) {
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
                email = email, password = password
            )
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val customerAccessToken = response.data?.customerAccessTokenCreate?.customerAccessToken
            val errors = response.data?.customerAccessTokenCreate?.customerUserErrors

            if (!errors.isNullOrEmpty()) {
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

            if (!errors.isNullOrEmpty()) {
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
            resetUrl = resetUrl, password = newPassword
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

    suspend fun addAddressToCustomer(
        addressInput: MailingAddressInput, customerAccessToken: String
    ): Boolean {
        val mutation = AddAddressToCustomerMutation(
            addressInput = addressInput, customerAccessToken = customerAccessToken
        )

        try {
            val response = apolloClient.mutation(mutation)
            val userErrors = response.execute().data?.customerAddressCreate?.customerUserErrors
            if (!userErrors.isNullOrEmpty()) {
                for (error in userErrors) {
                    println("Error: ${error.field}, ${error.message}")
                }
                return false
            }
            return true
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            return false
        }
    }

    suspend fun getCustomerAddresses(customerAccessToken: String): List<AddressInput> {
        val query = GetCustomerAddressesQuery(customerAccessToken)
        val response = apolloClient.query(query).execute()

        val addresses = response.data?.customer?.addresses?.edges?.map {
            it.node.let { node ->
                AddressInput(
                    id = node.id,
                    firstName = node.firstName ?: "",
                    address1 = node.address1 ?: "",
                    city = node.city ?: "",
                    country = node.country ?: "",
                    zip = node.zip ?: "",
                    phone = node.phone ?: ""
                )
            }
        } ?: emptyList()

        return addresses
    }

    suspend fun deleteCustomerAddress(customerAccessToken: String, addressId: String): Boolean {
        val mutation = DeleteCustomerAddressMutation(
            customerAccessToken = customerAccessToken, id = addressId
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val userErrors = response.data?.customerAddressDelete?.customerUserErrors
            if (!userErrors.isNullOrEmpty()) {
                for (error in userErrors) {
                    println("Error: ${error.field}, ${error.message}")
                }
                return false
            }
            return true
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            return false
        }
    }
}