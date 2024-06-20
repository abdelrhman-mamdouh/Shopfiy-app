package com.example.exclusive.features.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.features.home.repository.FakeHomeRepository
import com.example.exclusive.ui.home.repository.HomeRepository
import com.example.exclusive.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fakeHomeRepository: HomeRepository

    @Before
    fun setup() {
        fakeHomeRepository = FakeHomeRepository()
        homeViewModel = HomeViewModel(fakeHomeRepository)
    }

    @Test
    fun fetchBrands_returnsSuccess() = mainCoroutineRule.runBlockingTest {
        // When
        homeViewModel.fetchBrands()

        // Then
        val uiState = homeViewModel.uiState.first()
        val brands = (uiState as UiState.Success).data
        assertThat(brands[0].name, `is`("Brand1"))
    }

    @Test
    fun fetchDiscountPrice_returnsSuccess() = mainCoroutineRule.runBlockingTest {

        // When
        homeViewModel.fetchDiscountPrice()

        // Then
        val discountState = homeViewModel.discountState.first()
        val priceRules = (discountState as UiState.Success).data
        assertThat(priceRules.size, `is`(1))

    }
    @Test
    fun fetchCouponDetails_returnsSuccess() = mainCoroutineRule.runBlockingTest {
        // When
        homeViewModel.fetchCouponDetails(1)

        // Then
        val couponDetailsState = homeViewModel.couponDetailsState.first()
        val discountCode = (couponDetailsState as UiState.Success).data
        assertThat(discountCode.code, `is`("DISCOUNT10"))
    }
}
