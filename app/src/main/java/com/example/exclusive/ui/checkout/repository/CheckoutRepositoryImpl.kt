package com.example.exclusive.ui.checkout.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.repository.AddressRepository
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.CartProduct
import com.example.exclusive.type.MailingAddressInput
import javax.inject.Inject


class CheckoutRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : CheckoutRepository {

    override suspend fun fetchCustomerAddresses(): List<AddressInput> {
        val token = localDataSource.readToken()!!
        return remoteDataSource.getCustomerAddresses(token)
    }
    override suspend fun getCurrency(): Pair<String, Double> {
        return localDataSource.getCurrency()
    }
    override suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        return remoteDataSource.getProductsInCart(cartId)
    }
    override suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean {
        return remoteDataSource.applyDiscountCode(checkoutId,discountCode)
    }
}