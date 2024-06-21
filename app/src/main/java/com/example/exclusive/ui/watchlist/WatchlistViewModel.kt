package com.example.exclusive.ui.watchlist

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

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val realtimeDatabaseDataSource: RealtimeDatabaseDataSource,
    private val cartDataSource: CartDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {
    private val _isGuest = MutableStateFlow(false)
    val isGuest: StateFlow<Boolean> get() = _isGuest
    private val _addToCartState = MutableStateFlow<UiState<AddToCartResponse>>(UiState.Idle)
    val addToCartState: StateFlow<UiState<AddToCartResponse>> get() = _addToCartState
    private val _watchlist = MutableStateFlow<List<ProductNode>>(emptyList())
    val watchlist: StateFlow<List<ProductNode>> = _watchlist
    var email: String? = null
    init {
        getIsGuest()
    }
    fun fetchWatchlist() {
        viewModelScope.launch {
            email = localDataSource.readEmail()
            val result = realtimeDatabaseDataSource.fetchWatchlistProducts(email.toString())
            Log.d("ViewModelWatchlist", result.toString())
            _watchlist.value = result
        }
    }
    fun getIsGuest() {
        viewModelScope.launch {
            _isGuest.value = localDataSource.getIsGuest()
        }
    }
    fun removeProductFromWatchList(id: String) {
        viewModelScope.launch {
            email = localDataSource.readEmail()
            realtimeDatabaseDataSource.deleteProduct(id, email.toString())
            fetchWatchlist()
        }
    }

    fun addToCart(productId: String, quantity: Int) {
        _addToCartState.value = UiState.Loading
        viewModelScope.launch {
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
                Log.d("TAG", "addToCart: $cartId")
                val response = cartDataSource.addProductToCart(cartId!!, listOf(cartLineInput))
                _addToCartState.value =
                    if (response != null) UiState.Success(response) else UiState.Error(Exception("Failed to add to cart"))
            } catch (e: Exception) {
                _addToCartState.value = UiState.Error(e)
            }
        }
    }
}