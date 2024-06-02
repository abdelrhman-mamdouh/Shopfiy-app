package com.example.exclusive.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.Checkout
import com.example.exclusive.type.CheckoutLineItemInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val productsRepository: ShopifyRemoteDataSource
) : ViewModel() {

    private val _checkoutState = MutableStateFlow<UiState<Checkout>>(UiState.Idle)
    val checkoutState: StateFlow<UiState<Checkout>> get() = _checkoutState

    fun createCheckout(lineItems: List<CheckoutLineItemInput>, email: String?) {
        _checkoutState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val checkoutResponse = productsRepository.createCheckout(lineItems, email)
                _checkoutState.value = if (checkoutResponse?.checkout != null) {
                    UiState.Success(checkoutResponse.checkout)
                } else {
                    UiState.Error(Exception("Failed to create checkout"))
                }
            } catch (e: Exception) {
                _checkoutState.value = UiState.Error(e)
            }
        }
    }
}