package com.example.exclusive.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.Brand
import com.example.exclusive.ui.home.repository.HomeRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val brandRepository: HomeRepositoryImpl) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Brand>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Brand>>> get() = _uiState

    init {
        fetchBrands()
    }

    private fun fetchBrands() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val brandsList = brandRepository.getBrands()
                _uiState.value = UiState.Success(brandsList)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}
