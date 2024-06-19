package com.example.exclusive.data.remote.api

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.AddToCartMutation
import com.example.exclusive.CreateCartMutation
import com.example.exclusive.GetProductsInCartQuery
import com.example.exclusive.RemoveProductFromCartMutation
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.Cart
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.model.UserError
import com.example.exclusive.type.CartBuyerIdentityInput
import com.example.exclusive.type.CartLineInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartService @Inject constructor(private val apolloClient: ApolloClient) {
    suspend fun createCart(token: String): CreateCartResponse? {

        val mutation = CreateCartMutation(
            buyerIdentity = Optional.present(
                CartBuyerIdentityInput(
                    customerAccessToken = Optional.present(token)
                )
            )
        )
        try {
            val response = apolloClient.mutation(mutation)
            val cartCreate = response.execute().data?.cartCreate
            val cart = cartCreate?.cart?.let { Cart(it.id) }
            val userErrors =
                cartCreate?.userErrors?.map { UserError(it.field, it.message) } ?: emptyList()

            if (userErrors.isNotEmpty()) {
                for (error in userErrors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else if (cart != null) {
                Log.d("GraphQL", "Cart ID: ${cart.id}")
            }

            return CreateCartResponse(cart, userErrors)
        } catch (e: ApolloException) {
            Log.e("GraphQL", "ApolloException: ${e.message}", e)
        }
        return null
    }

    suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        val cartProducts = mutableListOf<CartProduct>()

        try {
            val response: ApolloResponse<GetProductsInCartQuery.Data> =
                apolloClient.query(GetProductsInCartQuery(cartId)).execute()

            response.data?.cart?.lines?.edges?.forEach { line ->
                val node = line.node
                val merchandise = node.merchandise.onProductVariant
                if (merchandise != null) {
                    val product = merchandise.product
                    val productId = product.id
                    val productTitle = product.title ?: ""
                    val productImageUrl = product.featuredImage?.url ?: ""
                    val variantId = merchandise.id
                    val variantTitle = merchandise.title ?: ""
                    val variantPrice = merchandise.price.amount

                    cartProducts.add(
                        CartProduct(
                            id = node.id,
                            quantity = node.quantity,
                            productId = productId,
                            productTitle = productTitle,
                            productImageUrl = productImageUrl as String,
                            variantId = variantId,
                            variantTitle = variantTitle,
                            variantPrice = variantPrice as String
                        )
                    )
                    Log.d("TAG", "getProductsInCart: ${node.quantity}")
                }
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return cartProducts
    }

    suspend fun addToCartById(cartId: String, lines: List<CartLineInput>): AddToCartResponse? {
        val mutation = AddToCartMutation(
            cartId = cartId, lines = lines
        )
        try {
            val response = apolloClient.mutation(mutation)
            val cartLinesAdd = response.execute().data?.cartLinesAdd
            val cart = cartLinesAdd?.cart?.let { Cart(it.id) }
            val userErrors =
                cartLinesAdd?.userErrors?.map { UserError(it.field, it.message) } ?: emptyList()

            if (userErrors.isNotEmpty()) {
                for (error in userErrors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else if (cart != null) {
                Log.d("GraphQL", "Products added to cart successfully. Cart ID: ${cart.id}")
            }

            return AddToCartResponse(cart, userErrors)
        } catch (e: ApolloException) {
            Log.e("GraphQL", "ApolloException: ${e.message}", e)
        }
        return null
    }

    suspend fun removeFromCartById(cartId: String, lineIds: List<String>): AddToCartResponse? {
        val mutation = RemoveProductFromCartMutation(
            cartId = cartId, lineIds = lineIds
        )
        try {
            val response = apolloClient.mutation(mutation)
            val cartLinesRemove = response.execute().data?.cartLinesRemove
            val cart = cartLinesRemove?.cart?.let { Cart(it.id) }
            val userErrors =
                cartLinesRemove?.userErrors?.map { UserError(it.field, it.message) } ?: emptyList()

            if (userErrors.isNotEmpty()) {
                for (error in userErrors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else if (cart != null) {
                Log.d("GraphQL", "Products removed from cart successfully. Cart ID: ${cart.id}")
            }

            return AddToCartResponse(cart, userErrors)
        } catch (e: ApolloException) {
            Log.e("GraphQL", "ApolloException: ${e.message}", e)
        }
        return null
    }
}