package com.example.exclusive.ui.checkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.AddressInput
import com.example.exclusive.model.CartProduct
import com.example.exclusive.ui.checkout.repository.CheckoutRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepositoryImpl
) : ViewModel() {

    private val _cartProductsResponse = MutableStateFlow<List<CartProduct>?>(null)
    val cartProductsResponse: StateFlow<List<CartProduct>?> = _cartProductsResponse
    private val _addresses = MutableStateFlow<UiState<List<AddressInput>>>(UiState.Idle)
    val addresses: StateFlow<UiState<List<AddressInput>>> = _addresses
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    fun fetchCustomerAddresses() {
        viewModelScope.launch {
            _addresses.value = UiState.Loading
            try {
                val addresses = checkoutRepository.fetchCustomerAddresses()
                _addresses.value = UiState.Success(addresses)
            } catch (e: Exception) {
                _addresses.value = UiState.Error(e)
            }
        }
    }

    private suspend fun getProductsInCart(cartId: String) {
        try {
            val currency = checkoutRepository.getCurrency()
            val products = checkoutRepository.getProductsInCart(cartId).map { product ->
                val price = product.variantPrice.toDouble() / currency.second
                val formattedPrice = String.format("%.2f", price)
                product.variantPrice = formattedPrice
                product.variantPriceCode = currency.first
                product
            }
            _cartProductsResponse.value = products
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

}