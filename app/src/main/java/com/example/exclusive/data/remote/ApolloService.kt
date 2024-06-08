// ApolloService.kt
package com.example.exclusive.data.remote

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.exclusive.AddAddressToCustomerMutation
import com.example.exclusive.AddToCartMutation
import com.example.exclusive.ApplyDiscountCodeMutation
import com.example.exclusive.BrandsQuery
import com.example.exclusive.CategoriesQuery
import com.example.exclusive.CreateCartMutation
import com.example.exclusive.CreateCheckoutMutation
import com.example.exclusive.CustomerAccessTokenCreateMutation
import com.example.exclusive.CustomerCreateMutation
import com.example.exclusive.DeleteCustomerAddressMutation
import com.example.exclusive.GetAllProductsQuery
import com.example.exclusive.GetCustomerAddressesQuery
import com.example.exclusive.GetProductsInCartQuery
import com.example.exclusive.ProductsQuery
import com.example.exclusive.RemoveProductFromCartMutation
import com.example.exclusive.ResetPasswordByUrlMutation
import com.example.exclusive.SendPasswordRecoverEmailMutation
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.Brand
import com.example.exclusive.model.Cart
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.Checkout
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.model.LineItem
import com.example.exclusive.model.ProductNode
import com.example.exclusive.model.UserError
import com.example.exclusive.model.Variant
import com.example.exclusive.model.Variants
import com.example.exclusive.type.CartBuyerIdentityInput
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.type.CustomerAccessTokenCreateInput
import com.example.exclusive.type.CustomerCreateInput
import com.example.exclusive.type.MailingAddressInput
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
                    node.description,
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

    suspend fun getAllProducts(): List<ProductNode> {
        val products = mutableListOf<ProductNode>()

        try {
            val response: ApolloResponse<GetAllProductsQuery.Data> =
                apolloClient.query(GetAllProductsQuery()).execute()
            val data = response.data
            Log.d("in aplllo", data.toString())
            data?.products?.edges?.forEach { edge ->
                val node = edge.node
                val productNode = ProductNode(
                    node.id,
                    node.title,
                    node.vendor,
                    node.description,
                    node.productType,
                    mapImages(node.images),
                    mapVariants(node.variants)
                )
                products.add(productNode)
            }
        } catch (e: ApolloException) {
            println("ApolloException: ${e.message}")
        }
        Log.d("in apllo", products.toString())
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
            cartId = cartId,
            lines = lines
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

    suspend fun createCheckout(
        lineItems: List<CheckoutLineItemInput>,
        email: String?
    ): CheckoutResponse? {
        val mutation = CreateCheckoutMutation(
            lineItems = lineItems,
            email = Optional.Present(email)
        )

        return try {
            val response = apolloClient.mutation(mutation).execute()
            val checkoutCreate = response.data?.checkoutCreate
            val checkout = checkoutCreate?.checkout?.let {
                Checkout(
                    id = it.id,
                    webUrl = it.webUrl.toString(),
                    lineItems = it.lineItems.edges.map { edge ->
                        LineItem(
                            title = edge.node.title,
                            quantity = edge.node.quantity,
                            variant = Variant(
                                id = edge.node.variant!!.id,
                                title = edge.node.variant.title,
                                price = edge.node.variant.price.amount.toString()
                            )
                        )
                    }
                )
            }
            val userErrors =
                checkoutCreate?.userErrors?.map { UserError(it.field, it.message) } ?: emptyList()

            CheckoutResponse(checkout, userErrors)
        } catch (e: ApolloException) {
            Log.e("GraphQL", "ApolloException: ${e.message}", e)
            null
        }
    }

    suspend fun removeFromCartById(cartId: String, lineIds: List<String>): AddToCartResponse? {
        val mutation = RemoveProductFromCartMutation(
            cartId = cartId,
            lineIds = lineIds
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

    suspend fun addAddressToCustomer(
        addressInput: MailingAddressInput,
        customerAccessToken: String
    ): Boolean {
        val mutation = AddAddressToCustomerMutation(
            addressInput = addressInput,
            customerAccessToken = customerAccessToken
        )

        try {
            val response = apolloClient.mutation(mutation)
            val userErrors = response.execute().data?.customerAddressCreate?.customerUserErrors
            if (userErrors != null && userErrors.isNotEmpty()) {
                for (error in userErrors) {
                    println("Error: ${error.field}, ${error.message}")
                }
                return false
            }
            return true
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            return false
        }
    }

    suspend fun getCustomerAddresses(customerAccessToken: String): List<AddressInput> {
        val query = GetCustomerAddressesQuery(customerAccessToken)
        val response = apolloClient.query(query).execute()

        val addresses = response.data?.customer?.addresses?.edges?.map {
            it.node.let { node ->
                AddressInput(
                    id = node.id,
                    firstName = node.firstName ?: "",
                    address1 = node.address1 ?: "",
                    city = node.city ?: "",
                    country = node.country ?: "",
                    zip = node.zip ?: "",
                    phone = node.phone ?: ""
                )
            }
        } ?: emptyList()

        return addresses
    }

    suspend fun deleteCustomerAddress(customerAccessToken: String, addressId: String): Boolean {
        val mutation = DeleteCustomerAddressMutation(
            customerAccessToken = customerAccessToken,
            id = addressId
        )

        try {
            val response = apolloClient.mutation(mutation).execute()
            val userErrors = response.data?.customerAddressDelete?.customerUserErrors
            if (userErrors != null && userErrors.isNotEmpty()) {
                for (error in userErrors) {
                    println("Error: ${error.field}, ${error.message}")
                }
                return false
            }
            return true
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            return false
        }
    }
    suspend fun applyDiscountCode(checkoutId: String, discountCode: String): Boolean {
        val mutation = ApplyDiscountCodeMutation(
            checkoutId = checkoutId,
            discountCode = discountCode
        )
        return try {
            val response: ApolloResponse<ApplyDiscountCodeMutation.Data> =
                apolloClient.mutation(mutation).execute()

            val errors = response.data?.checkoutDiscountCodeApplyV2?.checkoutUserErrors
            if (errors != null && errors.isNotEmpty()) {
                for (error in errors) {
                    Log.e("GraphQL", "Error: ${error.message}")
                }
                false
            } else {
                val checkout = response.data?.checkoutDiscountCodeApplyV2?.checkout
                Log.d("GraphQL", "Discount code applied successfully to checkout ID: ${checkout?.id}")
                true
            }
        } catch (e: ApolloException) {
            Log.e("GraphQL", "ApolloException: ${e.message}", e)
            false
        }
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

fun mapImages(productsQueryImages: GetAllProductsQuery.Images): com.example.exclusive.model.Images {
    val imageEdges = productsQueryImages.edges.map { imageEdge ->
        com.example.exclusive.model.ImageEdge(
            com.example.exclusive.model.ImageNode(imageEdge.node.src.toString())
        )
    }
    return com.example.exclusive.model.Images(imageEdges)
}

fun mapVariants(productsQueryVariants: GetAllProductsQuery.Variants): com.example.exclusive.model.Variants {
    val variantEdges = productsQueryVariants.edges.map { variantEdge ->
        com.example.exclusive.model.VariantEdge(
            com.example.exclusive.model.VariantNode(
                variantEdge.node.id,
                variantEdge.node.title,
                variantEdge.node.sku!!,
                com.example.exclusive.model.PriceV2(
                    variantEdge.node.priceV2.amount.toString(),
                    variantEdge.node.priceV2.currencyCode.toString()
                ), variantEdge.node.quantityAvailable!!,
            )
        )
    }
    return Variants(variantEdges)
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
                ),variantEdge.node.quantityAvailable!!
            )
        )
    }
    return Variants(variantEdges)
}