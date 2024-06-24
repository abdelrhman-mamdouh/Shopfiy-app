package com.example.exclusive.ui.category.repository

import com.example.exclusive.model.Brand

class FakeCategoriesRepository : CategoriesRepository {
    var shouldReturnError = false

    private val fakeCategories = listOf(
        Brand(id = "1", name = "Category1",""),
        Brand(id = "2", name = "Category2","")
    )

    override suspend fun getCategories(): List<Brand> {
        if (shouldReturnError) throw Exception("Test exception")
        return fakeCategories
    }
}