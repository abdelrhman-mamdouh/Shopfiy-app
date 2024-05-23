package com.example.exclusive.screens.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exclusive.screens.home.repo.HomeRepository

class HomeViewModelFactory (private val _repo: HomeRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            ProductViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("HomeViewModel Class not found")
        }
    }
}