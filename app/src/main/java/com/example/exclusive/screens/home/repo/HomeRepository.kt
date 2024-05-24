package com.example.exclusive.screens.home.repo

import com.example.exclusive.model.Brand

interface HomeRepository {
    suspend fun getBrands(): List<Brand>
}