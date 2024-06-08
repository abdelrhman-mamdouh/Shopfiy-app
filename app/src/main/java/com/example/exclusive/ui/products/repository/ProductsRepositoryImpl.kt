package com.example.exclusive.ui.products.repository

import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource) :
    ProductsRepository {
    override suspend fun getProducts(vendor: String): List<ProductNode> {
        return remoteDataSource.getProducts(vendor)
    }

    override suspend fun addProductToCart(cartId: String, lines: List<CartLineInput>): AddToCartResponse? {
        return remoteDataSource.addProductToCart(cartId, lines)
    }

    override suspend fun getCartId(): String {
        val email = localDataSource.readEmail()
        if (email != null) {
            email.replace('.', '-')
        }
        val cartId = remoteDataSource.fetchCartId(email!!)
        return cartId!!
    }
    override suspend fun getCurrentCurrency(): Pair<String,Double> {
        return localDataSource.getCurrency()
    }
}