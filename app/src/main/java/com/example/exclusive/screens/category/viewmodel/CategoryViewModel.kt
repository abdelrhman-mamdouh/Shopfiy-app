package com.example.exclusive.screens.category.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.model.Brand
import com.example.exclusive.screens.category.repository.CategoriesRepository
import com.example.exclusive.screens.home.repository.HomeRepository
import kotlinx.coroutines.launch


class CategoryViewModel(private val categoriesRepository: CategoriesRepository) : ViewModel() {
    private val _categories = MutableLiveData<List<Brand>>()
    val categories: LiveData<List<Brand>> get() = _categories
    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val brandsList = categoriesRepository.getCategories()
                _categories.postValue(brandsList)
            } catch (e: Exception) {

            }
        }
    }
}
