package com.example.exclusive.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.CartProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource
) : ViewModel() {

    private val _cartProductsResponse = MutableStateFlow<List<CartProduct>?>(null)
    val cartProductsResponse: StateFlow<List<CartProduct>?> = _cartProductsResponse

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
init{
    getProductsInCart("gid://shopify/Cart/Z2NwLWV1cm9wZS13ZXN0MTowMUhaODk2SFpITU0xRDc3QktZNjFLUzZHMg?key=91c152d577cf784f9cc0893d891cd0a9")
}
    fun getProductsInCart(cartId: String) {
        viewModelScope.launch {
            try {
                val response = remoteDataSource.getProductsInCart(cartId)
                _cartProductsResponse.value = response
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}