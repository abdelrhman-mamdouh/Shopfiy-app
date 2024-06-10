package com.example.exclusive.ui.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.MyOrder
import com.example.exclusive.ui.orders.repository.OrderRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepositoryImpl
) : ViewModel() {
    private val _ordersState = MutableStateFlow<UiState<List<MyOrder>>>(UiState.Idle)
    val ordersState: StateFlow<UiState<List<MyOrder>>> = _ordersState

    init {
        getAllOrders()
    }
    private fun getAllOrders() {
        _ordersState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val orders = orderRepository.getAllOrders(orderRepository.getToken())
                _ordersState.value = UiState.Success(orders)
            } catch (e: Exception) {
                _ordersState.value = UiState.Error(e)
            }
        }
    }
}
