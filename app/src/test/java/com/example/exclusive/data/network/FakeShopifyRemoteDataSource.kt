package com.example.exclusive.data.network

import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.Brand
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CheckoutDetails
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.model.MyOrder
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.type.MailingAddressInput

class FakeShopifyRemoteDataSource : ShopifyRemoteDataSource {
    override suspend fun getBrands(): List<Brand> {
        return listOf(
            Brand(id = "1", name = "Brand1","URL1"),
            Brand(id = "2", name = "Brand2","URL2")
        )
    }

    override suspend fun getCategories(): List<Brand> {
        TODO("Not yet implemented")
    }

    override suspend fun getProducts(vendor: String): List<ProductNode> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllProducts(): List<ProductNode> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchCartId(email: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveCardId(cardId: String, email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomer(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchWatchlistProducts(accessToken: String): List<ProductNode> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(productId: String, accessToken: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomerAccessToken(email: String, password: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun sendPasswordRecoveryEmail(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun addProductToRealtimeDatabase(product: ProductNode, accessToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createCart(token: String): CreateCartResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        TODO("Not yet implemented")
    }

    override suspend fun addProductToCart(
        cartId: String,
        lines: List<CartLineInput>
    ): AddToCartResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun createCheckout(
        lineItems: List<CheckoutLineItemInput>,
        email: String?
    ): CheckoutResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromCartById(
        cartId: String,
        lineIds: List<String>
    ): AddToCartResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun addAddress(
        addressInput: MailingAddressInput,
        customerAccessToken: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerAddresses(customerAccessToken: String): List<AddressInput> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCustomerAddress(
        customerAccessToken: String,
        addressId: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllOrders(customerAccessToken: String): List<MyOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun applyShippingAddress(
        checkoutId: String,
        shippingAddress: MailingAddressInput
    ): Boolean {
        TODO("Not yet implemented")
    }
}