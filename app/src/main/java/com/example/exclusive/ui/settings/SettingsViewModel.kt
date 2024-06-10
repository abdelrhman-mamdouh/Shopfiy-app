package com.example.exclusive.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.repository.CurrencyRepository
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.MyOrder
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {
    private val _ordersState = MutableStateFlow<UiState<List<MyOrder>>>(UiState.Idle)
    val ordersState: StateFlow<UiState<List<MyOrder>>> = _ordersState
    private val _addToCartState = MutableStateFlow<UiState<AddToCartResponse>>(UiState.Idle)
    val addToCartState: StateFlow<UiState<AddToCartResponse>> get() = _addToCartState
    private val _currenciesStateFlow = MutableStateFlow<UiState<Pair<String, Double>>>(UiState.Idle)
    val currenciesStateFlow: StateFlow<UiState<Pair<String, Double>>> get() = _currenciesStateFlow
    private val _watchlist = MutableStateFlow<UiState<List<ProductNode>>>(UiState.Idle)
    val watchlist: StateFlow<UiState<List<ProductNode>>> = _watchlist
    var email : String? =null
    init {
        fetchWatchlist()
        getAllOrders()
    }

    private fun getAllOrders() {
        _ordersState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val orders = remoteDataSource.getAllOrders(localDataSource.readToken()!!)
                _ordersState.value = UiState.Success(orders)
            } catch (e: Exception) {
                _ordersState.value = UiState.Error(e)
            }
        }
    }

    private fun fetchWatchlist() {
        _watchlist.value = UiState.Loading
        viewModelScope.launch {
            email = localDataSource.readEmail()
            val result = remoteDataSource.fetchWatchlistProducts(email.toString())
            Log.d("ViewModelWatchlist", result.toString())
            _watchlist.value = if (result.size > 2) {
                UiState.Success(result.subList(0, 2))
            } else {
                UiState.Success(result)
            }
        }
    }

    fun fetchCurrencies() {
        viewModelScope.launch {
            currencyRepository.getCurrentCurrency().collect { currency ->
                _currenciesStateFlow.value = UiState.Success(currency)
            }
        }
    }

    fun addToCart(productId: String,quantity:Int) {
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
                val cartId = remoteDataSource.fetchCartId(email!!)
                Log.d("TAG", "addToCart: $cartId")
                val response = remoteDataSource.addProductToCart(cartId!!, listOf(cartLineInput))
                _addToCartState.value =
                    if (response != null) UiState.Success(response) else UiState.Error(Exception("Failed to add to cart"))
            } catch (e: Exception) {
                _addToCartState.value = UiState.Error(e)
            }
        }
    }

    fun removeProductFromWatchList(id: String) {
        viewModelScope.launch {
            email = localDataSource.readEmail()
            remoteDataSource.deleteProduct(id, email.toString())
            fetchWatchlist()
        }
    }
}