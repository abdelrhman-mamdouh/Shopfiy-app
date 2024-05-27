package com.example.exclusive.screens.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exclusive.screens.products.repository.ProductsRepository


class ProductsViewModelFactory(private val productRepository: ProductsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            ProductsViewModel(productRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}