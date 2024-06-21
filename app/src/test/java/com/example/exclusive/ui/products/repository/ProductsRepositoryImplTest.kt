package com.example.exclusive.ui.products.repository


import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.local.ILocalDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import com.example.exclusive.data.local.FakeLocalDataSource
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductsRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var productsRepository: ProductsRepository
    private lateinit var fakeRemoteDataSource: ShopifyRemoteDataSource
    private lateinit var fakeLocalDataSource: ILocalDataSource

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeShopifyRemoteDataSource()
        fakeLocalDataSource = FakeLocalDataSource()
        productsRepository = ProductsRepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun getProducts_returnsProductList() = mainCoroutineRule.runBlockingTest {
        // Given
        val vendor = "Vendor1"

        // When
        val products = productsRepository.getProducts(vendor)

        // Then
        assertEquals(2, products.size)
        assertEquals("Product1", products[0].title)
    }

}
