package com.example.exclusive.ui.checkout.repository

import com.example.exclusive.data.model.AddressInput
import com.example.exclusive.data.model.CartProduct
import com.example.exclusive.data.model.CheckoutDetails
import com.example.exclusive.type.MailingAddressInput


interface CheckoutRepository {
    suspend fun getCurrency(): Pair<String, Double>
    suspend fun getProductsInCart(cartId: String): List<CartProduct>
    suspend fun fetchCustomerAddresses(): List<AddressInput>
    suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean
    suspend fun getUserCheckOut(): String?
    suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails?
    suspend fun applyShippingAddress(checkoutId: String, shippingAddress: MailingAddressInput): Boolean

}