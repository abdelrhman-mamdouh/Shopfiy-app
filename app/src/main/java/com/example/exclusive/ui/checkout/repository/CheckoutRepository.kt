package com.example.exclusive.ui.checkout.repository

import com.example.exclusive.data.model.OrderRequest
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CheckoutDetails
import com.example.exclusive.model.MyOrder
import com.example.exclusive.type.MailingAddressInput


interface CheckoutRepository {
    suspend fun getCurrency(): Pair<String, Double>
    suspend fun getProductsInCart(cartId: String): List<CartProduct>
    suspend fun fetchCustomerAddresses(): List<AddressInput>
    suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean
    suspend fun getUserCheckOut(): String?
    suspend fun getUserEmail(): String?
    suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails?
    suspend fun applyShippingAddress(
        checkoutId: String,
        shippingAddress: MailingAddressInput
    ): Boolean

    suspend fun createOrder(orderRequest: OrderRequest): MyOrder
    suspend fun fetchCartId(email: String): String?
}