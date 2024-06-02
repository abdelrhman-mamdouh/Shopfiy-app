// ShopifyRemoteDataSourceImpl.kt
package com.example.exclusive.data.remote

import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.Brand
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CartProductResponse
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.type.CheckoutLineItemInput
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ShopifyRemoteDataSourceImpl @Inject constructor(
    private val apolloService: ApolloService
) : ShopifyRemoteDataSource {


    override suspend fun getBrands(): List<Brand> {
        return apolloService.getBrands()
    }


    override suspend fun getCategories(): List<Brand> {
        return apolloService.getCategories()
    }

    override suspend fun getProducts(vendor: String): List<ProductNode> {
        return apolloService.getProducts(vendor)
    }

    override suspend fun createCustomer(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Boolean {
        return apolloService.createCustomer(
            email = email,
            password = password,
            firstName = firstName,
            secondName = secondName
        )
    }

    override suspend fun createCustomerAccessToken(email: String, password: String): String? {
        return apolloService.createCustomerAccessToken(email = email, password = password)
    }

    override suspend fun sendPasswordRecoveryEmail(email: String): Boolean {
        return apolloService.sendPasswordRecoveryEmail(email)
    }

    override suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean {
        return apolloService.resetPasswordByUrl(resetUrl, newPassword)
    }

    override suspend fun createCart(token: String): CreateCartResponse? {
        return apolloService.createCart(token = token)
    }

    override suspend fun addProductToCart(
        cartId: String,
        lines: List<CartLineInput>
    ): AddToCartResponse? {
        return apolloService.addToCartById(cartId,lines)
    }

    override suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        return apolloService.getProductsInCart(cartId)
    }

    override suspend fun createCheckout(lineItems: List<CheckoutLineItemInput>, email: String?): CheckoutResponse? {
        return apolloService.createCheckout(lineItems, email)
    }

    override suspend fun removeFromCartById(cartId: String, lineIds: List<String>): AddToCartResponse? {
        return apolloService.removeFromCartById(cartId,lineIds)
    }
}