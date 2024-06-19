package com.example.exclusive.data.remote.interfaces

import com.example.exclusive.model.AddressInput
import com.example.exclusive.type.MailingAddressInput

interface CustomerDataSource {
    suspend fun createCustomer(email: String, password: String, firstName: String, secondName: String): Boolean
    suspend fun createCustomerAccessToken(email: String, password: String): String?
    suspend fun sendPasswordRecoveryEmail(email: String): Boolean
    suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean
    suspend fun addAddress(addressInput: MailingAddressInput, customerAccessToken: String): Boolean
    suspend fun getCustomerAddresses(customerAccessToken: String): List<AddressInput>
    suspend fun deleteCustomerAddress(customerAccessToken: String, addressId: String): Boolean
}