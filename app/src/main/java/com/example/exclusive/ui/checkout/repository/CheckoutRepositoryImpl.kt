package com.example.exclusive.ui.checkout.repository

import com.example.exclusive.data.local.ILocalDataSource
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.model.OrderRequest
import com.example.exclusive.data.remote.AdminRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CheckoutDetails
import com.example.exclusive.model.MyOrder
import com.example.exclusive.type.MailingAddressInput

import javax.inject.Inject


class CheckoutRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: ILocalDataSource,
    private val adminRemoteDataSource: AdminRemoteDataSource
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

    override suspend fun getUserCheckOut(): String? {
        return localDataSource.getUserCheckOut()
    }

    override suspend fun getUserEmail(): String? {
        return localDataSource.readEmail()
    }

    override suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails? {
        return remoteDataSource.getCheckoutDetails(checkoutId)
    }

    override suspend fun applyShippingAddress(
        checkoutId: String,
        shippingAddress: MailingAddressInput
    ): Boolean {
      return remoteDataSource.applyShippingAddress(checkoutId,shippingAddress)
    }

    override suspend fun createOrder(orderRequest: OrderRequest): MyOrder {
        return adminRemoteDataSource.createOrder(orderRequest)
    }

    override suspend fun fetchCartId(email: String): String? {
        return remoteDataSource.fetchCartId(email)
    }


}