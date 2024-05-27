package com.example.exclusive.screens.products.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.model.MyProduct
import com.example.exclusive.screens.products.repository.ProductsRepository
import kotlinx.coroutines.launch

class ProductsViewModel(private val productRepository: ProductsRepository) : ViewModel() {
    private val _products = MutableLiveData<List<MyProduct>>()
    val products: LiveData<List<MyProduct>> get() = _products
    fun fetchProducts(vendor: String) {
        viewModelScope.launch {
            try {
                val brandsList = productRepository.getProducts(vendor)
                _products.postValue(brandsList)
            } catch (e: Exception) {

            }
        }
    }

    fun filterProductsByType(productType: String) {
        val currentProducts = _products.value ?: return
        val filteredProducts = currentProducts.filter { it.productType == productType }
        _products.value = filteredProducts
    }
}
