package com.example.exclusive.data.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.interfaces.CustomerDataSource
import com.example.exclusive.model.AddressInput
import com.example.exclusive.type.MailingAddressInput
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val customerDataSource: CustomerDataSource,
    private val localDataSource: LocalDataSource
) : AddressRepository {

    override suspend fun fetchCustomerAddresses(): List<AddressInput> {
        val token = localDataSource.readToken()!!
        return customerDataSource.getCustomerAddresses(token)
    }

    override suspend fun deleteCustomerAddress(addressId: String): Boolean {
        val token = localDataSource.readToken()!!
        return customerDataSource.deleteCustomerAddress(token, addressId)
    }

    override suspend fun addCustomerAddress(address: MailingAddressInput): Boolean {
        val token = localDataSource.readToken()!!
        return customerDataSource.addAddress(address, token)
    }
}