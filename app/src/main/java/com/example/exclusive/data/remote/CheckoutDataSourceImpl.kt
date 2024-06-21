package com.example.exclusive.data.remote

import com.example.exclusive.data.remote.api.CheckoutService
import com.example.exclusive.data.remote.interfaces.CheckoutDataSource
import com.example.exclusive.data.model.CheckoutDetails
import com.example.exclusive.data.model.CheckoutResponse
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.type.MailingAddressInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckoutDataSourceImpl @Inject constructor(
    private val checkoutService: CheckoutService
) : CheckoutDataSource {

    override suspend fun createCheckout(lineItems: List<CheckoutLineItemInput>, email: String?): CheckoutResponse {
        return checkoutService.createCheckout(lineItems, email)!!
    }

    override suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean {
        return checkoutService.applyDiscountCode(checkoutId, discountCode)
    }

    override suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails? {
        return checkoutService.getCheckoutDetails(checkoutId)
    }

    override suspend fun applyShippingAddress(checkoutId: String, shippingAddress: MailingAddressInput): Boolean {
        return checkoutService.applyShippingAddress(checkoutId, shippingAddress)
    }
}