package com.example.exclusive.screens.category.repository

import com.example.exclusive.model.Brand
import com.example.exclusive.model.MyProduct

interface CategoriesRepository {
    suspend fun getCategories(): List<Brand>
}