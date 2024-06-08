package com.example.exclusive.data.repository

import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.CartProduct

interface CartRepository {
    suspend fun getUserCartId(): String?
    suspend fun getProductsInCart(cartId: String): List<CartProduct>
    suspend fun removeProductFromCart(cartId: String, productId: String): AddToCartResponse?
    suspend fun getCurrency(): Pair<String, Double>
}