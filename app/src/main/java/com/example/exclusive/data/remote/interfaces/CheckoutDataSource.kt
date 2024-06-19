package com.example.exclusive.data.remote.interfaces

import com.example.exclusive.model.CheckoutDetails
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.type.MailingAddressInput

interface CheckoutDataSource {
    suspend fun createCheckout(lineItems: List<CheckoutLineItemInput>, email: String?): CheckoutResponse
    suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean
    suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails?
    suspend fun applyShippingAddress(checkoutId: String, shippingAddress: MailingAddressInput): Boolean
}