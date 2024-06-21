package com.example.exclusive.ui.products.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.AddToCartResponse
import com.example.exclusive.model.PriceV2
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.products.repository.ProductsRepository
import com.example.exclusive.ui.products.repository.ProductsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProductsViewModel"
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ProductNode>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<ProductNode>>> get() = _uiState

    fun fetchProducts(vendor: String){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val currency = productRepository.getCurrentCurrency()
                val products = productRepository.getProducts(vendor).map { product ->
                    product.apply {
                        variants.edges.forEach { variantEdge ->
                            val price = variantEdge.node.priceV2.amount.toDouble() / currency.second
                            val formattedPrice = String.format("%.2f", price)
                            variantEdge.node.priceV2 = PriceV2(formattedPrice, currency.first)
                            Log.i(TAG, "fetchProducts: ${currency.first}")

                        }
                    }
                }
                _uiState.value = UiState.Success(products)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}
