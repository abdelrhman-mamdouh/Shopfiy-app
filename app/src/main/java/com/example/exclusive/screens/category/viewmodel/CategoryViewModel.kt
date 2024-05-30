package com.example.exclusive.screens.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.Brand
import com.example.exclusive.model.MyProduct
import com.example.exclusive.screens.category.repository.CategoriesRepository
import com.example.exclusive.screens.category.repository.CategoriesRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Brand>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Brand>>> get() = _uiState
    init {
        fetchCategories()
    }
    private fun fetchCategories() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val categories = categoriesRepository.getCategories()
                _uiState.value = UiState.Success(categories)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}
