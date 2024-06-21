package com.example.exclusive.data.remote.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.exclusive.AddAddressToCustomerMutation
import com.example.exclusive.CustomerAccessTokenCreateMutation
import com.example.exclusive.CustomerCreateMutation
import com.example.exclusive.DeleteCustomerAddressMutation
import com.example.exclusive.GetCustomerAddressesQuery
import com.example.exclusive.ResetPasswordByUrlMutation
import com.example.exclusive.SendPasswordRecoverEmailMutation
import com.example.exclusive.data.model.AddressInput
import com.example.exclusive.type.CustomerAccessTokenCreateInput
import com.example.exclusive.type.CustomerCreateInput
import com.example.exclusive.type.MailingAddressInput
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerService @Inject constructor(private val apolloClient: ApolloClient) {
    companion object {
        private const val TAG = "CustomerService"
    }
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
                    Timber.tag(TAG).d("getBrands: ${error.message}")
                }
            } else if (customer != null) {
                Timber.tag(TAG).d("Customer ID: ${customer.id}")
                return true
            }
        } catch (e: Exception) {
            Timber.tag(TAG).d("createCustomer: ${e.message}")
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
                    Timber.tag(TAG).d(error.message)
                }
            } else if (customerAccessToken != null) {
                Timber.tag(TAG).d("Access Token: ${customerAccessToken.accessToken}")
                Timber.tag(TAG).d("Expires At: ${customerAccessToken.expiresAt}")
                return customerAccessToken.accessToken
            }
        } catch (e: Exception) {
            Timber.tag(TAG).d("Exception: ${e.message}")
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
                    Timber.tag(TAG).d(error.message)
                }
            } else {
                Timber.tag(TAG).d("Password recovery email sent successfully")
                return true
            }
        } catch (e: Exception) {
            Timber.tag(TAG).d("Exception: ${e.message}")
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

            if (!errors.isNullOrEmpty()) {
                for (error in errors) {
                    Timber.tag(TAG).d(error.message)
                }
            } else {
                val customerId = response.data?.customerResetByUrl?.customer?.id
                Timber.tag(TAG).d("Password reset successfully for customer ID: $customerId")
                return true
            }
        } catch (e: Exception) {
            Timber.tag(TAG).d("Exception: ${e.message}")
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