package com.example.exclusive.ui.home.repository

import com.example.exclusive.model.Brand

interface HomeRepository {
    suspend fun getBrands(): List<Brand>
}