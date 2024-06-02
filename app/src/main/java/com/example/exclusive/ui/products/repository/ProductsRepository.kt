package com.example.exclusive.ui.products.repository

import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput


interface ProductsRepository {
    suspend fun getProducts(vendor: String): List<ProductNode>
    suspend fun getCartId(): String
    suspend fun addProductToCart(cartId: String, lines: List<CartLineInput>): AddToCartResponse?
    suspend fun getCurrentCurrency(): Pair<String, Double>
}