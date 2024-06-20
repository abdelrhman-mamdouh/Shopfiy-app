package com.example.exclusive.features.products.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.features.products.repository.FakeProductsRepository
import com.example.exclusive.model.PriceV2
import com.example.exclusive.model.ProductNode
import com.example.exclusive.ui.products.repository.ProductsRepository
import com.example.exclusive.ui.products.repository.ProductsRepositoryImpl
import com.example.exclusive.ui.products.viewmodel.ProductsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var productsRepository: ProductsRepository

    @Before
    fun setup() {
        productsRepository = FakeProductsRepository()
        productsViewModel = ProductsViewModel(productsRepository)
    }

    @Test
    fun fetchProducts_returnsSuccess() = mainCoroutineRule.runBlockingTest {
        // Given
        val vendor = "Vendor1"

        // When
        productsViewModel.fetchProducts(vendor)

        // Then
        val uiState = productsViewModel.uiState.first()
        assertThat(uiState is UiState.Success, `is`(true))
        val products = (uiState as UiState.Success).data
        assertThat(products.size, `is`(1))
        assertThat(products[0].title, `is`("Product1"))
    }
}
