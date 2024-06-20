package com.example.exclusive.data.network

import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.Brand
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CheckoutDetails
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.model.DiscountCodeApplication
import com.example.exclusive.model.DiscountValue
import com.example.exclusive.model.LineItem
import com.example.exclusive.model.MyOrder
import com.example.exclusive.model.PriceV2
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.Variant
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.type.MailingAddressInput

class FakeShopifyRemoteDataSource : ShopifyRemoteDataSource {
    private var shouldReturnError = false
    private val fakeCheckoutDetails = CheckoutDetails(
        webUrl = "http://example.com",
        id = "checkout123",
        createdAt = "2024-01-01T00:00:00Z",
        completedAt = null,
        currencyCode = "USD",
        totalPrice = PriceV2(amount = "100.00", currencyCode = "USD"),
        lineItems = listOf(
            LineItem(
                title = "Product1",
                quantity = 1,
                variant = Variant(
                    id = "1",
                    title = "Variant1",
                    price = PriceV2(amount = "100.00", currencyCode = "USD")
                ),
                price = 100
            )
        ),
        discountApplications = listOf(
            DiscountCodeApplication(
                code = "DISCOUNT10",
                value = DiscountValue.Percentage("10")
            )
        )
    )
    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getBrands(): List<Brand> {
        return listOf(
            Brand(id = "1", name = "Brand1","URL1"),
            Brand(id = "2", name = "Brand2","URL2")
        )
    }

    override suspend fun getCategories(): List<Brand> {
        if (shouldReturnError) {
            throw Exception("Test exception")
        }
        return listOf(Brand(id = "1", name = "Category1","url1"), Brand(id = "2", name = "Category2","url2"))
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

    override suspend fun getCustomerAddresses(token: String): List<AddressInput> {
        return listOf(
            AddressInput(
                id = "1",
                firstName = "abdelrhman",
                lastName = "mamdouh",
                phone = "123456789",
                address1 = "123 Main St",
                city = "City",
                country = "Country",
                zip = "12345",
                province = "Province"
            )
        )
    }

    override suspend fun deleteCustomerAddress(
        customerAccessToken: String,
        addressId: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean {
        return discountCode == "DISCOUNT10"
    }


    override suspend fun getCheckoutDetails(checkoutId: String): CheckoutDetails? {
        return fakeCheckoutDetails
    }


    override suspend fun getAllOrders(customerAccessToken: String): List<MyOrder> {
        TODO("Not yet implemented")
    }

    override suspend fun applyShippingAddress(checkoutId: String, shippingAddress: MailingAddressInput): Boolean {
        return true
    }

}