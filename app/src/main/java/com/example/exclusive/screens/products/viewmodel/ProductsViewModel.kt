package com.example.exclusive.screens.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.MyProduct
import com.example.exclusive.screens.products.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<MyProduct>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<MyProduct>>> get() = _uiState

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
}
