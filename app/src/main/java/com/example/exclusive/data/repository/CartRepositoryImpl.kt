package com.example.exclusive.data.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.interfaces.CartDataSource
import com.example.exclusive.data.remote.interfaces.CheckoutDataSource
import com.example.exclusive.data.model.AddToCartResponse
import com.example.exclusive.data.model.CartProduct
import com.example.exclusive.data.model.CheckoutResponse
import com.example.exclusive.type.CheckoutLineItemInput
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartRemoteDataSource: CartDataSource,
    private val checkoutDataSource: CheckoutDataSource,
    private val localDataSource: LocalDataSource
) : CartRepository {
    override suspend fun getIsGuest(): Boolean {
        return localDataSource.getIsGuest()
    }
    override suspend fun getUserCartId(): String? {
        return localDataSource.getUserCartId()
    }

    override suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        return cartRemoteDataSource.getProductsInCart(cartId)
    }

    override suspend fun removeProductFromCart(cartId: String, productId: String): AddToCartResponse? {
        return cartRemoteDataSource.removeFromCartById(cartId, listOf(productId))
    }

    override suspend fun getCurrency(): Pair<String, Double> {
        return localDataSource.getCurrency()
    }

    override suspend fun readEmail(): String? {
       return localDataSource.readEmail()
    }

    override suspend fun fetchCartId(email: String): String? {
     return cartRemoteDataSource.fetchCartId(email)
    }

    override suspend fun removeFromCartById(
        cartId: String,
        lineIds: List<String>
    ): AddToCartResponse? {
       return cartRemoteDataSource.removeFromCartById(cartId,lineIds)
    }
    override suspend fun createCheckout(
        lineItems: List<CheckoutLineItemInput>,
        email: String?
    ): CheckoutResponse {
        return checkoutDataSource.createCheckout(lineItems, email)
    }

    override suspend fun getUserCheckOut(): String? {
        return localDataSource.getUserCheckOut()
    }

    override suspend fun saveUserCheckOut(checkoutId: String) {
        localDataSource.saveUserCheckOut(checkoutId)
    }


}