package com.example.exclusive.data.repository

import com.example.exclusive.data.model.AddressInput
import com.example.exclusive.type.MailingAddressInput

interface AddressRepository {
    suspend fun fetchCustomerAddresses(): List<AddressInput>
    suspend fun deleteCustomerAddress(addressId: String): Boolean
    suspend fun addCustomerAddress(address: MailingAddressInput): Boolean
}