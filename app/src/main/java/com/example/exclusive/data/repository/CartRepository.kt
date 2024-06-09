package com.example.exclusive.data.repository

import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.type.CheckoutLineItemInput

interface CartRepository {
    suspend fun getUserCartId(): String?
    suspend fun getProductsInCart(cartId: String): List<CartProduct>
    suspend fun removeProductFromCart(cartId: String, productId: String): AddToCartResponse?
    suspend fun getCurrency(): Pair<String, Double>
    suspend fun readEmail(): String?
    suspend fun fetchCartId(email: String): String?
    suspend fun removeFromCartById(cartId: String, lineIds: List<String>): AddToCartResponse?
    suspend fun createCheckout(
        lineItems: List<CheckoutLineItemInput>,
        email: String?
    ): CheckoutResponse?
    suspend fun getUserCheckOut(): String?
    suspend fun saveUserCheckOut(checkoutId: String)

}