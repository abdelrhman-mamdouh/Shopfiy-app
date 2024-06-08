package com.example.exclusive.data.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.AddressInput
import com.example.exclusive.type.MailingAddressInput
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : AddressRepository {

    override suspend fun fetchCustomerAddresses(): List<AddressInput> {
        val token = localDataSource.readToken()!!
        return remoteDataSource.getCustomerAddresses(token)
    }

    override suspend fun deleteCustomerAddress(addressId: String): Boolean {
        val token = localDataSource.readToken()!!
        return remoteDataSource.deleteCustomerAddress(token, addressId)
    }

    override suspend fun addCustomerAddress(address: MailingAddressInput): Boolean {
        val token = localDataSource.readToken()!!
        return remoteDataSource.addAddress(address, token)
    }
}