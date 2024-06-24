package com.example.exclusive.ui.category.repository

import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import com.example.exclusive.model.Brand
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoriesRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var categoriesRepository: CategoriesRepository
    private lateinit var fakeShopifyRemoteDataSource: FakeShopifyRemoteDataSource

    @Before
    fun setup() {
        fakeShopifyRemoteDataSource = FakeShopifyRemoteDataSource()
        categoriesRepository = CategoriesRepositoryImpl(fakeShopifyRemoteDataSource)
    }

    @Test
    fun getCategories_returnsCategoryList() = mainCoroutineRule.runBlockingTest {

        // When
        val categories = categoriesRepository.getCategories()

        // Then
        assertEquals(2, categories.size)
        assertEquals("Category1", categories[0].name)
    }
}