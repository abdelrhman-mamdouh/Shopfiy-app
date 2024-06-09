package com.example.exclusive.data.remote

import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.Brand
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CheckoutDetails
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.type.MailingAddressInput


interface ShopifyRemoteDataSource {
    suspend fun getBrands(): List<Brand>

    suspend fun getCategories(): List<Brand>
    suspend fun getProducts(vendor: String): List<ProductNode>
    suspend fun getAllProducts(): List<ProductNode>
     suspend fun fetchCartId(email: String): String?
    suspend fun saveCardId(cardId: String, email: String)
    suspend fun createCustomer(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Boolean
    suspend fun fetchWatchlistProducts(accessToken:String): List<ProductNode>
    suspend fun deleteProduct(productId: String,accessToken:String): Boolean
    suspend fun createCustomerAccessToken(email: String, password: String): String?
    suspend fun sendPasswordRecoveryEmail(email: String): Boolean
    suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean
    fun addProductToRealtimeDatabase(product:ProductNode,accessToken:String)
    suspend fun createCart(token: String): CreateCartResponse?

    suspend fun getProductsInCart(cartId: String): List<CartProduct>
    suspend fun addProductToCart(cartId: String, lines: List<CartLineInput>): AddToCartResponse?
    suspend fun createCheckout(
        lineItems: List<CheckoutLineItemInput>,
        email: String?
    ): CheckoutResponse?

    suspend fun removeFromCartById(cartId: String, lineIds: List<String>): AddToCartResponse?

    suspend fun addAddress(addressInput: MailingAddressInput, customerAccessToken: String): Boolean

    suspend fun getCustomerAddresses(customerAccessToken: String): List<AddressInput>
    suspend fun deleteCustomerAddress(customerAccessToken: String, addressId: String): Boolean
    suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean
    suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails?
}