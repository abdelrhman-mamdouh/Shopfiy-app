package com.example.exclusive.data.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.type.CheckoutLineItemInput
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : CartRepository {

    override suspend fun getUserCartId(): String? {
        return localDataSource.getUserCartId()
    }

    override suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        return remoteDataSource.getProductsInCart(cartId)
    }

    override suspend fun removeProductFromCart(cartId: String, productId: String): AddToCartResponse? {
        return remoteDataSource.removeFromCartById(cartId, listOf(productId))
    }

    override suspend fun getCurrency(): Pair<String, Double> {
        return localDataSource.getCurrency()
    }

    override suspend fun readEmail(): String? {
       return localDataSource.readEmail()
    }

    override suspend fun fetchCartId(email: String): String? {
     return remoteDataSource.fetchCartId(email)
    }

    override suspend fun removeFromCartById(
        cartId: String,
        lineIds: List<String>
    ): AddToCartResponse? {
       return remoteDataSource.removeFromCartById(cartId,lineIds)
    }
    override suspend fun createCheckout(
        lineItems: List<CheckoutLineItemInput>,
        email: String?
    ): CheckoutResponse? {
        return remoteDataSource.createCheckout(lineItems, email)
    }



}