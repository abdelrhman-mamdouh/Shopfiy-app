package com.example.exclusive.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.remote.interfaces.CartDataSource
import com.example.exclusive.data.remote.interfaces.OrderDataSource
import com.example.exclusive.data.remote.interfaces.RealtimeDatabaseDataSource
import com.example.exclusive.data.repository.CurrencyRepository
import com.example.exclusive.data.model.AddToCartResponse
import com.example.exclusive.data.model.MyOrder
import com.example.exclusive.data.model.ProductNode
import com.example.exclusive.type.CartLineInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val realtimeDatabaseDataSource: RealtimeDatabaseDataSource,
    private val orderDataSource: OrderDataSource,
    private val cartDataSource: CartDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {
    private val _ordersState = MutableStateFlow<UiState<List<MyOrder>>>(UiState.Idle)
    val ordersState: StateFlow<UiState<List<MyOrder>>> = _ordersState
    private val _currenciesStateFlow = MutableStateFlow<UiState<Pair<String, Double>>>(UiState.Idle)
    val currenciesStateFlow: StateFlow<UiState<Pair<String, Double>>> get() = _currenciesStateFlow
    private val _watchlist = MutableStateFlow<UiState<List<ProductNode>>>(UiState.Idle)
    val watchlist: StateFlow<UiState<List<ProductNode>>> = _watchlist
    var email : String? =null
    private val _isGuest = MutableStateFlow<Boolean>(false)
    val isGuest: StateFlow<Boolean> = _isGuest
    init {
        fetchWatchlist()
        getAllOrders()
        getIsGuest()
    }
    fun getIsGuest() {
        viewModelScope.launch {
            _isGuest.value = localDataSource.getIsGuest()
        }
    }
    private fun getAllOrders() {
        _ordersState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val orders = orderDataSource.getAllOrders(localDataSource.readToken()!!)
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
            val result = realtimeDatabaseDataSource.fetchWatchlistProducts(email.toString())
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
                cartDataSource.addProductToCart(cartId, listOf(cartLineInput))
            } catch (_: Exception) {
            }
        }
    }

    fun removeProductFromWatchList(id: String) {
        viewModelScope.launch {
            email = localDataSource.readEmail()
            realtimeDatabaseDataSource.deleteProduct(id, email.toString())
            fetchWatchlist()
        }
    }
    fun clearEmailAndToken() {
        viewModelScope.launch {
            localDataSource.clearEmailAndToken()
        }
    }
}