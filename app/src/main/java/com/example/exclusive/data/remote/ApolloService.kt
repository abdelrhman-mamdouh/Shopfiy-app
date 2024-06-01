// ApolloService.kt
package com.example.exclusive.data.remote

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.BrandsQuery
import com.example.exclusive.CategoriesQuery
import com.example.exclusive.CreateCartMutation
import com.example.exclusive.CustomerAccessTokenCreateMutation
import com.example.exclusive.CustomerCreateMutation
import com.example.exclusive.GetProductsInCartQuery
import com.example.exclusive.ProductsQuery
import com.example.exclusive.ResetPasswordByUrlMutation
import com.example.exclusive.SendPasswordRecoverEmailMutation
import com.example.exclusive.model.Brand
import com.example.exclusive.model.Cart
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CartProductResponse
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.UserError
import com.example.exclusive.type.CartBuyerIdentityInput

import com.example.exclusive.type.CustomerAccessTokenCreateInput
import com.example.exclusive.type.CustomerCreateInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class ApolloService @Inject constructor(private val apolloClient: ApolloClient) {

    suspend fun getBrands(): List<Brand> {
        val brands = mutableListOf<Brand>()

        try {
            val response: ApolloResponse<BrandsQuery.Data> =
                apolloClient.query(BrandsQuery()).execute()

            response.data?.collections?.edges?.forEach { brand ->
                val id = brand.node.id
                val name = brand.node.title ?: ""
                val imageUrl = brand.node.image?.originalSrc ?: ""
                brands.add(Brand(id = id, name = name, imageUrl = imageUrl.toString()))
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return brands
    }


    suspend fun getProducts(vendor: String): List<ProductNode> {
        val products = mutableListOf<ProductNode>()

        try {
            val response = apolloClient.query(ProductsQuery(vendor = vendor)).execute()
            val data = response.data

            data?.products?.edges?.forEach { edge ->
                val node = edge.node
                val productNode = ProductNode(
                    node.id,
                    node.title,
                    node.vendor,
                    node.productType,
                    mapImages(node.images),
                    mapVariants(node.variants)
                )
                products.add(productNode)
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return products
    }


    suspend fun getCategories(): List<Brand> {
        val brands = mutableListOf<Brand>()

        try {
            val response: ApolloResponse<CategoriesQuery.Data> =
                apolloClient.query(CategoriesQuery()).execute()

            response.data?.collections?.edges?.forEach { brand ->
                val id = brand.node.id
                val name = brand.node.title ?: ""
                val imageUrl = brand.node.image?.originalSrc ?: ""
                brands.add(Brand(id = id, name = name, imageUrl = imageUrl.toString()))
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return brands
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
                }
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }

        return cartProducts
    }

    suspend fun createCustomer(
        email: String,
        password: String,
        firstName: String,
        secondName: String
    ): Boolean {
        val mutation = CustomerCreateMutation(
            input = CustomerCreateInput(
                email = email,
                password = password,
                firstName = Optional.presentIfNotNull(firstName),
                lastName = Optional.presentIfNotNull(secondName)
            )
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val customer = response.data?.customerCreate?.customer
            val errors = response.data?.customerCreate?.customerUserErrors

            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")

                }
            } else if (customer != null) {
                Log.d("GraphQL", "Customer ID: ${customer.id}")
                return true
            }
        } catch (e: Exception) {
            Log.e("GraphQL", "Exception: ${e.message}", e)

        }
        return false
    }

    suspend fun createCustomerAccessToken(email: String, password: String): String? {
        val mutation = CustomerAccessTokenCreateMutation(
            input = CustomerAccessTokenCreateInput(
                email = email,
                password = password
            )
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val customerAccessToken = response.data?.customerAccessTokenCreate?.customerAccessToken
            val errors = response.data?.customerAccessTokenCreate?.customerUserErrors

            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else if (customerAccessToken != null) {

                Log.d("GraphQL", "Access Token: ${customerAccessToken.accessToken}")
                Log.d("GraphQL", "Expires At: ${customerAccessToken.expiresAt}")
                return customerAccessToken.accessToken
            }
        } catch (e: Exception) {
            Log.e("GraphQL", "Exception: ${e.message}", e)
        }
        return null
    }

    suspend fun sendPasswordRecoveryEmail(email: String): Boolean {
        val mutation = SendPasswordRecoverEmailMutation(
            email = email
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val errors = response.data?.customerRecover?.customerUserErrors

            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else {
                Log.d("GraphQL", "Password recovery email sent successfully")
                return true
            }
        } catch (e: Exception) {
            Log.e("GraphQL", "Exception: ${e.message}", e)
        }
        return false
    }

    suspend fun resetPasswordByUrl(resetUrl: String, newPassword: String): Boolean {
        val mutation = ResetPasswordByUrlMutation(
            resetUrl = resetUrl,
            password = newPassword
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val errors = response.data?.customerResetByUrl?.customerUserErrors

            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
            } else {
                val customerId = response.data?.customerResetByUrl?.customer?.id
                Log.d("GraphQL", "Password reset successfully for customer ID: $customerId")
                return true
            }
        } catch (e: Exception) {
            Log.e("GraphQL", "Exception: ${e.message}", e)
        }
        return false
    }

    suspend fun createCard(token: String): CreateCartResponse? {
        val mutation = CreateCartMutation(
            buyerIdentity = Optional.Present(CartBuyerIdentityInput(Optional.Present(token)))
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val cartCreate = response.data?.cartCreate
            val cart = cartCreate?.cart?.let { Cart(it.id) }
            val userErrors = cartCreate?.userErrors?.map { UserError(it.field, it.message) } ?: emptyList()

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
}
fun mapImages(productsQueryImages: ProductsQuery.Images): com.example.exclusive.model.Images {
    val imageEdges = productsQueryImages.edges.map { imageEdge ->
        com.example.exclusive.model.ImageEdge(
            com.example.exclusive.model.ImageNode(imageEdge.node.src.toString())
        )
    }
    return com.example.exclusive.model.Images(imageEdges)
}
fun mapVariants(productsQueryVariants: ProductsQuery.Variants): com.example.exclusive.model.Variants {
    val variantEdges = productsQueryVariants.edges.map { variantEdge ->
        com.example.exclusive.model.VariantEdge(
            com.example.exclusive.model.VariantNode(
                variantEdge.node.id,
                variantEdge.node.title,
                variantEdge.node.sku!!,
                com.example.exclusive.model.PriceV2(
                    variantEdge.node.priceV2.amount.toString(),
                    variantEdge.node.priceV2.currencyCode.toString()
                )
            )
        )
    }
    return com.example.exclusive.model.Variants(variantEdges)
}

