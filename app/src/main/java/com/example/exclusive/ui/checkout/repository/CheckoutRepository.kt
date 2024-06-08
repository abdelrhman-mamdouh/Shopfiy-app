package com.example.exclusive.ui.checkout.repository

import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.CartProduct

interface CheckoutRepository {
    suspend fun getCurrency(): Pair<String, Double>
    suspend fun getProductsInCart(cartId: String): List<CartProduct>
    suspend fun fetchCustomerAddresses(): List<AddressInput>
    suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean
}