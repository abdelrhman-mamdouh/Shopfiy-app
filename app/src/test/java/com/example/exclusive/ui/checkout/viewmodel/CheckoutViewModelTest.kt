package com.example.exclusive.ui.checkout.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.local.FakeLocalDataSource
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.model.AddressInput
import com.example.exclusive.ui.checkout.repository.CheckoutRepository
import com.example.exclusive.ui.checkout.repository.CheckoutRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CheckoutViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var fakeCheckoutRepository: CheckoutRepository

    @Before
    fun setup() {
        val fakeRemoteDataSource = FakeShopifyRemoteDataSource()
        val fakeLocalDataSource = FakeLocalDataSource()
        fakeCheckoutRepository = CheckoutRepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
        checkoutViewModel = CheckoutViewModel(fakeCheckoutRepository)
    }
    @Test
    fun fetchCustomerAddresses_returnsSuccess() = mainCoroutineRule.runBlockingTest {
        // When
        checkoutViewModel.fetchCustomerAddresses()

        // Then
        val addressesState = checkoutViewModel.addresses.first()
        val addresses = (addressesState as UiState.Success).data
        assertEquals(1, addresses.size)
        assertEquals("123 Main St", addresses[0].address1)
    }

    @Test
    fun fetchCheckoutDetails_returnsSuccess() = mainCoroutineRule.runBlockingTest {
        // When
        checkoutViewModel.fetchCheckoutDetails()

        // Then
        val checkoutDetailsState = checkoutViewModel.checkoutDetailsState.first()
        val checkoutDetails = (checkoutDetailsState as UiState.Success).data
        assertEquals("checkout123", checkoutDetails.id)
        assertEquals("100.00", checkoutDetails.totalPrice?.amount)
        assertEquals("Product1", checkoutDetails.lineItems[0].title)
    }

    @Test
    fun applyDiscountCode_returnsSuccess() = mainCoroutineRule.runBlockingTest {
        // Given
        val discountCode = "DISCOUNT10"

        // When
        val result = checkoutViewModel.applyDiscountCode(discountCode)

        // Then
        assertTrue(result)
    }

    @Test
    fun applyShippingAddress_returnsSuccess() = mainCoroutineRule.runBlockingTest {
        // Given

        val address = AddressInput(
            firstName = "abdelrhman",
            address1 = "123 Main St",
            phone = "01153716828",
            city = "cairo",
            country = "Egypy",
            zip = "1123"
        )
        val mailingAddressInput = address.toInput()

        // When
        val result = checkoutViewModel.applyShippingAddress(mailingAddressInput)

        // Then
        assertTrue(result)
    }
}