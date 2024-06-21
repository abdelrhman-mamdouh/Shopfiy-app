package com.example.exclusive.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.interfaces.ProductDataSource
import com.example.exclusive.data.model.ProductNode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val productDataSource: ProductDataSource) :
    ViewModel() {
    private val _proudtcs = MutableStateFlow<List<ProductNode>>(emptyList())
    val products: StateFlow<List<ProductNode>> = _proudtcs

    fun getProducts() {
        viewModelScope.launch {
            val result = productDataSource.getAllProducts()
            _proudtcs.value = result
        }
    }

}