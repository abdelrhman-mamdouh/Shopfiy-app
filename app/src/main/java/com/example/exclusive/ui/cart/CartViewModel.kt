package com.example.exclusive.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _cartProductsResponse = MutableStateFlow<List<CartProduct>?>(null)
    val cartProductsResponse: StateFlow<List<CartProduct>?> = _cartProductsResponse

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {

            val email = cartRepository.readEmail()
            email?.replace('.', '-')
            val cartId = cartRepository.fetchCartId(email!!)
            Log.i("TAG", "CartViewModel:${cartId}")
            cartId?.let { getProductsInCart(cartId) }
        }
    }

    suspend fun deleteProductFromCart(product: CartProduct) {
        try {
           val email = cartRepository.readEmail()
            email?.replace('.', '-')
            val cartId = cartRepository.fetchCartId(email!!)
            cartId?.let {
                val response = cartRepository.removeProductFromCart(it, product.id)
                response?.let {
                    if (it.userErrors.isEmpty()) {
                        val updatedList = _cartProductsResponse.value?.toMutableList()
                        updatedList?.remove(product)
                        _cartProductsResponse.value = updatedList
                    } else {
                        _error.value = it.userErrors.joinToString { error -> error.message }
                    }

                }
            }
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    private suspend fun getProductsInCart(cartId: String) {
        try {
            val currency = cartRepository.getCurrency()
            val products = cartRepository.getProductsInCart(cartId).map { product ->
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