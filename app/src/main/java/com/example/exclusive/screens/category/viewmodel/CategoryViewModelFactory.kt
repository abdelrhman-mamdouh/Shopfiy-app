package com.example.exclusive.screens.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exclusive.screens.category.repository.CategoriesRepository


class CategoryViewModelFactory(private val categoriesRepository: CategoriesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            CategoryViewModel(categoriesRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}