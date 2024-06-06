// ShopifyRemoteDataSourceImpl.kt
package com.example.exclusive.data.remote

import android.util.Log
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.Brand
import com.example.exclusive.model.CartProduct
import com.example.exclusive.model.CheckoutResponse
import com.example.exclusive.model.CreateCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.type.CheckoutLineItemInput
import com.example.exclusive.type.MailingAddressInput
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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
    override suspend fun getAllProducts(): List<ProductNode> {
        return apolloService.getAllProducts()
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

    override fun addProductToRealtimeDatabase(product: ProductNode, accessToken: String) {
        val database =
            FirebaseDatabase.getInstance("https://exclusice-6129d-default-rtdb.firebaseio.com/")

        val myRef = database.getReference(accessToken.replace('.', '-'))
        myRef.child("products").child(product.id.substring(22)).setValue(product)
    }

    override suspend fun addProductToCart(
        cartId: String,
        lines: List<CartLineInput>
    ): AddToCartResponse? {
        return apolloService.addToCartById(cartId, lines)
    }

    override suspend fun fetchWatchlistProducts(accessToken: String): List<ProductNode> =
        withContext(Dispatchers.IO) {
            val database =
                FirebaseDatabase.getInstance("https://exclusice-6129d-default-rtdb.firebaseio.com/")
                    .getReference(accessToken.replace('.', '-'))
            val productsRef = database.child("products")

            val deferred = CompletableDeferred<List<ProductNode>>()

            productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val products = mutableListOf<ProductNode>()
                    Log.d("watch", dataSnapshot.value.toString())
                    for (productSnapshot in dataSnapshot.children) {
                        Log.d("wathclist item", "I'm here")
                        val gson = Gson()
                        val productType = object : TypeToken<ProductNode>() {}.type
                        val productsJson = productSnapshot.value?.let { gson.toJson(it) }
                        val product = gson.fromJson<ProductNode>(productsJson, productType)

                        if (product != null) {
                            products.add(product)
                            Log.d("wathclist item", product.toString())
                        }
                    }
                    deferred.complete(products)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    deferred.completeExceptionally(databaseError.toException())
                }
            })
            val list = deferred.await()
            Log.d("watchlist form remote", list.toString())
            return@withContext list
        }

    override suspend fun deleteProduct(productId: String, accessToken: String): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val database =
                    FirebaseDatabase.getInstance("https://exclusice-6129d-default-rtdb.firebaseio.com/")
                        .getReference(accessToken.replace('.', '-'))
                Log.d("access token", accessToken)
                val productRef = database.child("products").child(productId)
                productRef.removeValue().await()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun getProductsInCart(cartId: String): List<CartProduct> {
        return apolloService.getProductsInCart(cartId)
    }

    override suspend fun createCheckout(
        lineItems: List<CheckoutLineItemInput>,
        email: String?
    ): CheckoutResponse? {
        return apolloService.createCheckout(lineItems, email)
    }

    override suspend fun removeFromCartById(
        cartId: String,
        lineIds: List<String>
    ): AddToCartResponse? {
        return apolloService.removeFromCartById(cartId, lineIds)
    }

    override suspend fun addAddress(
        addressInput: MailingAddressInput,
        customerAccessToken: String
    ): Boolean {
        return apolloService.addAddressToCustomer(addressInput, customerAccessToken)
    }

    override suspend fun getCustomerAddresses(customerAccessToken: String): List<AddressInput> {
        return apolloService.getCustomerAddresses(customerAccessToken)
    }

    override suspend fun deleteCustomerAddress(
        customerAccessToken: String,
        addressId: String
    ): Boolean {
        return apolloService.deleteCustomerAddress(customerAccessToken, addressId)
    }
}