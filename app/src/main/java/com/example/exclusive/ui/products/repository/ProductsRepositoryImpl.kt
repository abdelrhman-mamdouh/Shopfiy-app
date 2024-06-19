package com.example.exclusive.ui.products.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.interfaces.CartDataSource
import com.example.exclusive.data.remote.interfaces.ProductDataSource
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsDataSource: ProductDataSource,
    private val cartDataSource: CartDataSource,
    private val localDataSource: LocalDataSource) :
    ProductsRepository {
    override suspend fun getProducts(vendor: String): List<ProductNode> {
        return productsDataSource.getProducts(vendor)
    }

    override suspend fun addProductToCart(cartId: String, lines: List<CartLineInput>): AddToCartResponse? {
        return cartDataSource.addProductToCart(cartId, lines)
    }

    override suspend fun getCartId(): String {
        val email = localDataSource.readEmail()
        email?.replace('.', '-')
        val cartId = cartDataSource.fetchCartId(email!!)
        return cartId
    }
    override suspend fun getCurrentCurrency(): Pair<String,Double> {
        return localDataSource.getCurrency()
    }
}