package com.example.exclusive.screens.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exclusive.screens.home.repository.HomeRepository

class HomeViewModelFactory(private val brandRepository: HomeRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BrandsViewModel::class.java)) {
            BrandsViewModel(brandRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}