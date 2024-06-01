package com.example.exclusive.ui.products.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.ProductNode
import com.example.exclusive.type.CartLineInput
import com.example.exclusive.ui.products.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProductsViewModel"
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ProductNode>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<ProductNode>>> get() = _uiState

    private val _addToCartState = MutableStateFlow<UiState<AddToCartResponse>>(UiState.Idle)
    val addToCartState: StateFlow<UiState<AddToCartResponse>> get() = _addToCartState

    fun fetchProducts(vendor: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val products = productRepository.getProducts(vendor)
                _uiState.value = UiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
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
                val cartId = productRepository.getCartId()
                Log.d(TAG, "addToCart: $cartId")
                val response = productRepository.addProductToCart(cartId, listOf(cartLineInput))
                _addToCartState.value = if (response != null) UiState.Success(response) else UiState.Error(Exception("Failed to add to cart"))
            } catch (e: Exception) {
                _addToCartState.value = UiState.Error(e)
            }
        }
    }
}
