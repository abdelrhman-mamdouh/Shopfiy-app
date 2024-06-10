package com.example.exclusive.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.repository.CartRepository
import com.example.exclusive.model.CartProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartProductsResponse = MutableStateFlow<UiState<List<CartProduct>>>(UiState.Idle)
    val cartProductsResponse: StateFlow<UiState<List<CartProduct>>> = _cartProductsResponse

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            val email = cartRepository.readEmail()?.replace('.', '-')
            email?.let {
                val cartId = cartRepository.fetchCartId(it)
                Log.i("TAG", "CartViewModel:$cartId")
                cartId?.let { getProductsInCart(it) }
            }
        }
    }

    suspend fun deleteProductFromCart(product: CartProduct) {
        try {
            val email = cartRepository.readEmail()?.replace('.', '-')
            email?.let {
                val cartId = cartRepository.fetchCartId(it)
                cartId?.let {
                    val response = cartRepository.removeProductFromCart(it, product.id)
                    response?.let {
                        if (it.userErrors.isEmpty()) {
                            val updatedList = (_cartProductsResponse.value as? UiState.Success)?.data?.toMutableList()
                            updatedList?.remove(product)
                            _cartProductsResponse.value = UiState.Success(updatedList ?: emptyList())
                        } else {
                            _error.value = it.userErrors.joinToString { error -> error.message }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    private suspend fun getProductsInCart(cartId: String) {
        _cartProductsResponse.value = UiState.Loading
        try {
            val currency = cartRepository.getCurrency()
            val products = cartRepository.getProductsInCart(cartId).map { product ->
                val price = product.variantPrice.toDouble() / currency.second
                val formattedPrice = String.format("%.2f", price)
                product.variantPrice = formattedPrice
                product.variantPriceCode = currency.first
                product
            }
            _cartProductsResponse.value = UiState.Success(products)
        } catch (e: Exception) {
            _cartProductsResponse.value = UiState.Error(e)
        }
    }
}