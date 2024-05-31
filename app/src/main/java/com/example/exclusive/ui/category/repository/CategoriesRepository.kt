package com.example.exclusive.ui.category.repository

import com.example.exclusive.model.Brand

interface CategoriesRepository {
    suspend fun getCategories(): List<Brand>
}