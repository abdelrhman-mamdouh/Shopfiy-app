package com.example.exclusive.data.remote.interfaces

import com.example.exclusive.model.Brand

interface CategoriesRepository {
    suspend fun getCategories(): List<Brand>
}