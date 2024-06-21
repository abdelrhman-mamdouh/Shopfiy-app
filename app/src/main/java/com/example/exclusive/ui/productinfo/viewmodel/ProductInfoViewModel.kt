package com.example.exclusive.ui.productinfo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.remote.interfaces.CartDataSource
import com.example.exclusive.data.remote.interfaces.RealtimeDatabaseDataSource
import com.example.exclusive.data.model.AddToCartResponse
import com.example.exclusive.data.model.ProductNode
import com.example.exclusive.type.CartLineInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProductsViewModel"

@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val realtimeDataSource: RealtimeDatabaseDataSource,
    private val cartDataSource: CartDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _addToCartState = MutableStateFlow<UiState<AddToCartResponse>>(UiState.Idle)
    val addToCartState: StateFlow<UiState<AddToCartResponse>> get() = _addToCartState
    private val _isWatchList = MutableStateFlow(false)
    val isWatchList: StateFlow<Boolean> = _isWatchList
    var accessToken: String? = null
    private val _isGuest = MutableStateFlow(false)
    val isGuest: StateFlow<Boolean> = _isGuest
    init {
        getIsGuest()
    }
    fun getIsGuest() {
        viewModelScope.launch {
            _isGuest.value = localDataSource.getIsGuest()
        }
    }
    fun addProductToRealtimeDatabase(product: ProductNode) {
        viewModelScope.launch {
            var email = localDataSource.readEmail()
            email?.replace('.', '-')
            Log.d("readEmail", email.toString())
            realtimeDataSource.addProductToRealtimeDatabase(product, email.toString())
            _isWatchList.value = true
        }

    }

    fun isInWatchList(product: ProductNode) {
        val result = viewModelScope.launch {
            var email = localDataSource.readEmail()
            val watchlist = realtimeDataSource.fetchWatchlistProducts(email.toString())
            Log.d("watchlist", watchlist.toString())
            var bool = false
            watchlist.forEach {
                if (it.id == product.id)
                    bool = true
            }
            _isWatchList.value = bool
        }
    }

    fun removeProductFromWatchList(id: String) {
        viewModelScope.launch {
            val email =localDataSource.readEmail()
            realtimeDataSource.deleteProduct(id,email.toString())
            _isWatchList.value = false
        }
    }

    fun addToCart(productId: String, quantity: Int) {
        _addToCartState.value = UiState.Loading

        viewModelScope.launch {
            val token = localDataSource.readToken()
            Log.i(TAG, "addToCart: ${token}")
            try {
                val cartLineInput = CartLineInput(
                    attributes = Optional.Absent,
                    quantity = Optional.Present(quantity),
                    merchandiseId = productId,
                    sellingPlanId = Optional.Absent
                )
                val email = localDataSource.readEmail()
                email?.replace('.', '-')
                val cartId = cartDataSource.fetchCartId(email!!)
                Log.d(TAG, "addToCart: $cartId")
                val response = cartDataSource.addProductToCart(cartId!!, listOf(cartLineInput))
                _addToCartState.value =
                    if (response != null) UiState.Success(response) else UiState.Error(Exception("Failed to add to cart"))
            } catch (e: Exception) {
                _addToCartState.value = UiState.Error(e)
            }
        }
    }
}