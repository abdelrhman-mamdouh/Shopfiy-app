package com.example.exclusive.ui.checkout.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.interfaces.CartDataSource
import com.example.exclusive.data.remote.interfaces.CheckoutDataSource
import com.example.exclusive.data.remote.interfaces.CustomerDataSource
import com.example.exclusive.data.model.AddressInput
import com.example.exclusive.data.model.CartProduct
import com.example.exclusive.data.model.CheckoutDetails
import com.example.exclusive.type.MailingAddressInput

import javax.inject.Inject

class CheckoutRepositoryImpl @Inject constructor(
    private val checkoutDataSource: CheckoutDataSource,
    private val customerDataSource: CustomerDataSource,
    private val cartDataSource: CartDataSource,
    private val localDataSource: LocalDataSource
) : CheckoutRepository {

    override suspend fun fetchCustomerAddresses(): List<AddressInput> {
        val token = localDataSource.readToken()!!
        return customerDataSource.getCustomerAddresses(token)
    }
    override suspend fun getCurrency(): Pair<String, Double> {
        return localDataSource.getCurrency()
    }
    override suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        return cartDataSource.getProductsInCart(cartId)
    }
    override suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean {
        return checkoutDataSource.applyDiscountCode(checkoutId,discountCode)
    }

    override suspend fun getUserCheckOut(): String? {
        return localDataSource.getUserCheckOut()
    }

    override suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails? {
        return checkoutDataSource.getCheckoutDetails(checkoutId)
    }

    override suspend fun applyShippingAddress(
        checkoutId: String,
        shippingAddress: MailingAddressInput
    ): Boolean {
      return checkoutDataSource.applyShippingAddress(checkoutId,shippingAddress)
    }
}