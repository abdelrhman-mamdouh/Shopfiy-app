package com.example.exclusive.data.remote

import com.example.exclusive.data.remote.api.CartService
import com.example.exclusive.data.remote.interfaces.CartDataSource
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.type.CartLineInput
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class CartDataSourceImpl @Inject constructor(
    private val cartService: CartService
) : CartDataSource {

    override suspend fun createCart(token: String): CreateCartResponse? {
        return cartService.createCart(token)
    }

    override suspend fun addProductToCart(cartId: String, lines: List<CartLineInput>): AddToCartResponse? {
        return cartService.addToCartById(cartId, lines)
    }

    override suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        return cartService.getProductsInCart(cartId)
    }

    override suspend fun removeFromCartById(cartId: String, lineIds: List<String>): AddToCartResponse? {
        return cartService.removeFromCartById(cartId, lineIds)
    }

    override suspend fun saveCardId(cardId: String, email: String) {
        val database = FirebaseDatabase.getInstance("https://exclusice-6129d-default-rtdb.firebaseio.com/")
        val myRef = database.getReference(email.replace('.', '-'))
        myRef.child("cart-id").setValue(cardId)
    }

    override suspend fun fetchCartId(email: String): String {
        val database = FirebaseDatabase.getInstance("https://exclusice-6129d-default-rtdb.firebaseio.com/")
        val myRef = database.getReference(email.replace('.', '-'))
        return myRef.child("cart-id").get().await().value as String
    }
}