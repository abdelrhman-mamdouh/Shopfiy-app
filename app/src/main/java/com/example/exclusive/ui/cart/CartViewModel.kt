package com.example.exclusive.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.local.LocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.CartProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val remoteDataSource: ShopifyRemoteDataSource,
    private val localDataSource: LocalDataSource
) : ViewModel() {


    private val _cartProductsResponse = MutableStateFlow<List<CartProduct>?>(null)
    val cartProductsResponse: StateFlow<List<CartProduct>?> = _cartProductsResponse

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            val cartId = localDataSource.getUserCartId()
            getProductsInCart(cartId = cartId!!)
        }
    }

    suspend fun deleteProductFromCart(product: CartProduct) {
        try {
            val cartId = localDataSource.getUserCartId()
            val response = remoteDataSource.removeFromCartById(cartId!!, listOf(product.id))

            response?.let {
                if (it.userErrors.isEmpty()) {
                    val updatedList = _cartProductsResponse.value?.toMutableList()
                    updatedList?.remove(product)
                    _cartProductsResponse.value = updatedList
                } else {
                    _error.value = it.userErrors.joinToString { error -> error.message }
                }
            }
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    private suspend fun getProductsInCart(cartId: String) {
        try {
            val response = remoteDataSource.getProductsInCart(cartId)
            _cartProductsResponse.value = response
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}