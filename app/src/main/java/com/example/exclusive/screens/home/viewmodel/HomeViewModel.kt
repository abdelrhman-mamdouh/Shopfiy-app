package com.example.exclusive.screens.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.model.Brand
import com.example.exclusive.screens.home.repository.HomeRepository
import kotlinx.coroutines.launch


class BrandsViewModel(private val brandRepository: HomeRepository) : ViewModel() {
    private val _brands = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>> get() = _brands
    fun fetchBrands() {
        viewModelScope.launch {
            try {
                val brandsList = brandRepository.getBrands()
                _brands.postValue(brandsList)
            } catch (e: Exception) {

            }
        }
    }
}
