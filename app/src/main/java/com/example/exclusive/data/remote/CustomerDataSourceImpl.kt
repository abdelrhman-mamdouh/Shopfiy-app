package com.example.exclusive.data.remote

import com.example.exclusive.data.remote.api.CustomerService
import com.example.exclusive.data.remote.interfaces.CustomerDataSource
import com.example.exclusive.data.model.AddressInput
import com.example.exclusive.type.MailingAddressInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerDataSourceImpl @Inject constructor(
    private val customerService: CustomerService
) : CustomerDataSource {

    override suspend fun createCustomer(email: String, password: String, firstName: String, secondName: String): Boolean {
        return customerService.createCustomer(email, password, firstName, secondName)
    }

    override suspend fun createCustomerAccessToken(email: String, password: String): String? {
        return customerService.createCustomerAccessToken(email, password)
    }

    override suspend fun sendPasswordRecoveryEmail(email: String): Boolean {
        return customerService.sendPasswordRecoveryEmail(email)
    }

    override suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean {
        return customerService.resetPasswordByUrl(resetUrl, newPassword)
    }

    override suspend fun addAddress(addressInput: MailingAddressInput, customerAccessToken: String): Boolean {
        return customerService.addAddressToCustomer(addressInput, customerAccessToken)
    }

    override suspend fun getCustomerAddresses(customerAccessToken: String): List<AddressInput> {
        return customerService.getCustomerAddresses(customerAccessToken)
    }

    override suspend fun deleteCustomerAddress(customerAccessToken: String, addressId: String): Boolean {
        return customerService.deleteCustomerAddress(customerAccessToken, addressId)
    }
}