package com.example.exclusive.ui.products.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProductsViewModel"

@HiltViewModel
class ProductInfoViewModel @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    private val _addToCartState = MutableStateFlow<UiState<AddToCartResponse>>(UiState.Idle)
    val addToCartState: StateFlow<UiState<AddToCartResponse>> get() = _addToCartState
    private val _isWatchList = MutableStateFlow<Boolean>(false)
    val isWatchList: StateFlow<Boolean> = _isWatchList
    var accessToken: String? = null
    fun addProductToRealtimeDatabase(product: ProductNode){
        viewModelScope.launch {
            var email=localDataSource.readEmail()
            Log.d("readEmail", email.toString())
            remoteDataSource.addProductToRealtimeDatabase(product,email.toString())
            _isWatchList.value = true
        }

    }
    fun isInWatchList(product: ProductNode){
        val result =viewModelScope.launch {
            var  email=localDataSource.readEmail()
            val watchlist = remoteDataSource.fetchWatchlistProducts(email.toString())
            Log.d("watchlist", watchlist.toString())
            _isWatchList.value = watchlist.contains(product)
        }
    }
    fun removeProductFromWatchList(id: String){
        viewModelScope.launch {
            var email=localDataSource.readEmail()
            remoteDataSource.deleteProduct(id,email.toString())
            _isWatchList.value = false
        }
    }

    fun addToCart(productId: String) {
        _addToCartState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val cartLineInput = CartLineInput(
                    attributes = Optional.Absent,
                    quantity = Optional.Present(1),
                    merchandiseId = productId,
                    sellingPlanId = Optional.Absent
                )
                val cartId = localDataSource.getUserCartId()!!
                Log.d(TAG, "addToCart: $cartId")
                val response = remoteDataSource.addProductToCart(cartId, listOf(cartLineInput))
                _addToCartState.value =
                    if (response != null) UiState.Success(response) else UiState.Error(Exception("Failed to add to cart"))
            } catch (e: Exception) {
                _addToCartState.value = UiState.Error(e)
            }
        }
    }
}
