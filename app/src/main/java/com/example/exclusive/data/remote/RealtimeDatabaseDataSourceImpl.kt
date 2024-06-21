package com.example.exclusive.data.remote

import android.util.Log
import com.example.exclusive.data.remote.interfaces.RealtimeDatabaseDataSource
import com.example.exclusive.data.model.ProductNode
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
class RealtimeDatabaseDataSourceImpl @Inject constructor() : RealtimeDatabaseDataSource {

    override fun addProductToRealtimeDatabase(product: ProductNode, accessToken: String) {
        val database = FirebaseDatabase.getInstance("https://exclusice-6129d-default-rtdb.firebaseio.com/")
        val myRef = database.getReference(accessToken.replace('.', '-'))
        myRef.child("products").child(product.id.substring(22)).setValue(product)
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
                val database = FirebaseDatabase.getInstance("https://exclusice-6129d-default-rtdb.firebaseio.com/")
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
}