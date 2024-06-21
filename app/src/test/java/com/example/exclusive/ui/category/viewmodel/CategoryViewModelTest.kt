package com.example.exclusive.ui.category.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.ui.category.repository.CategoriesRepository
import com.example.exclusive.ui.category.repository.CategoriesRepositoryImpl
import com.example.exclusive.ui.category.repository.FakeCategoriesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CategoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()


    private lateinit var categoryRepository: CategoriesRepository
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var fakeRemoteDataSource: FakeShopifyRemoteDataSource
    @Before
    fun setup() {
        fakeRemoteDataSource = FakeShopifyRemoteDataSource()
        categoryRepository = CategoriesRepositoryImpl(fakeRemoteDataSource)
        categoryViewModel = CategoryViewModel(categoryRepository)
    }

    @Test
    fun fetchCategories_returnsSuccess() = mainCoroutineRule.runBlockingTest {

        // Then
        val uiState = categoryViewModel.uiState.first()
        val categories = (uiState as UiState.Success).data
        assertEquals("Category1", categories[0].name)
    }

    @Test
    fun fetchCategories_returnsError() = mainCoroutineRule.runBlockingTest {
        // Given
        fakeRemoteDataSource.setReturnError(true)
        // When
        categoryViewModel.fetchCategories()
        // Then
        val uiState = categoryViewModel.uiState.first()
        assertTrue(uiState is UiState.Error)
    }
}