package com.example.exclusive.data.repository

import com.example.exclusive.data.model.AddToCartResponse
import com.example.exclusive.data.model.ProductNode
import com.example.exclusive.type.CartLineInput


interface ProductsRepository {
    suspend fun getProducts(vendor: String): List<ProductNode>
    suspend fun getCartId(): String
    suspend fun addProductToCart(cartId: String, lines: List<CartLineInput>): AddToCartResponse?
    suspend fun getCurrentCurrency(): Pair<String, Double>
}