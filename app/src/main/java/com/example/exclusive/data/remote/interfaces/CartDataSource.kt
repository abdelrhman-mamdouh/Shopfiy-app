package com.example.exclusive.data.remote.interfaces

import com.example.exclusive.data.model.AddToCartResponse
import com.example.exclusive.data.model.CartProduct
import com.example.exclusive.data.model.CreateCartResponse
import com.example.exclusive.type.CartLineInput

interface CartDataSource {
    suspend fun createCart(token: String): CreateCartResponse?
    suspend fun addProductToCart(cartId: String, lines: List<CartLineInput>): AddToCartResponse?
    suspend fun getProductsInCart(cartId: String): List<CartProduct>
    suspend fun removeFromCartById(cartId: String, lineIds: List<String>): AddToCartResponse?

    suspend fun saveCardId(cardId: String, email: String)
    suspend fun fetchCartId(email: String): String
}