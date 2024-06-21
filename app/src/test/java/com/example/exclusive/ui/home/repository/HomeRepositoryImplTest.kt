package com.example.exclusive.ui.home.repository

import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.remote.DiscountDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.data.network.FakeDiscountDataSource
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var homeRepository: HomeRepository
    private lateinit var fakeShopifyRemoteDataSource: ShopifyRemoteDataSource
    private lateinit var fakeDiscountDataSource: DiscountDataSource

    @Before
    fun setup() {
        fakeShopifyRemoteDataSource = FakeShopifyRemoteDataSource()
        fakeDiscountDataSource = FakeDiscountDataSource()
        homeRepository = HomeRepositoryImpl(fakeShopifyRemoteDataSource, fakeDiscountDataSource)
    }

    @Test
    fun getBrands_returnsBrandList() = mainCoroutineRule.runBlockingTest {
        // When
        val brands = homeRepository.getBrands()

        // Then
        assertEquals(2, brands.size)
        assertEquals("Brand1", brands[0].name)
    }

    @Test
    fun getPriceRules_returnsPriceRulesResponse() = mainCoroutineRule.runBlockingTest {
        // When
        val priceRulesResponse = homeRepository.getPriceRules()

        // Then
        assertEquals(1, priceRulesResponse.priceRules.size)
        assertEquals("Rule1", priceRulesResponse.priceRules[0].title)
    }

    @Test
    fun getCouponDetails_returnsCouponDetails() = mainCoroutineRule.runBlockingTest {
        // When
        val couponDetails = homeRepository.getCouponDetails(1)

        // Then
        assertEquals(1, couponDetails.discount_codes.size)
        assertEquals("DISCOUNT10", couponDetails.discount_codes[0].code)
    }
}
