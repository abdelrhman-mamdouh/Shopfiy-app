package com.example.exclusive.ui.checkout.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exclusive.MainCoroutineRule
import com.example.exclusive.data.local.FakeLocalDataSource
import com.example.exclusive.data.local.ILocalDataSource
import com.example.exclusive.data.network.FakeShopifyRemoteDataSource
import com.example.exclusive.data.remote.ShopifyRemoteDataSource
import com.example.exclusive.model.AddressInput
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CheckoutRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var checkoutRepository: CheckoutRepositoryImpl
    private lateinit var fakeRemoteDataSource: ShopifyRemoteDataSource
    private lateinit var fakeLocalDataSource: ILocalDataSource

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeShopifyRemoteDataSource()
        fakeLocalDataSource = FakeLocalDataSource()
        checkoutRepository = CheckoutRepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun fetchCustomerAddresses_returnsAddresses() = mainCoroutineRule.runBlockingTest {
        // When
        val addresses = checkoutRepository.fetchCustomerAddresses()

        // Then
        assertThat(addresses.size, `is`(1))
        assertThat(addresses[0].firstName, `is`("abdelrhman"))
    }

    @Test
    fun getCurrency_returnsCurrency() = mainCoroutineRule.runBlockingTest {
        // When
        val currency = checkoutRepository.getCurrency()

        // Then
        assertThat(currency.first, `is`("USD"))
        assertThat(currency.second, `is`(1.0))
    }

    @Test
    fun getUserCheckOut_returnsCheckOutId() = mainCoroutineRule.runBlockingTest {
        // When
        val checkOutId = checkoutRepository.getUserCheckOut()

        // Then
        assertThat(checkOutId, `is`("checkout123"))
    }

    @Test
    fun getCheckoutDetails_returnsCheckoutDetails() = mainCoroutineRule.runBlockingTest {
        // When
        val checkoutDetails = checkoutRepository.getCheckoutDetails("checkout123")

        // Then
        assertThat(checkoutDetails?.id, `is`("checkout123"))
    }

    @Test
    fun applyDiscountCode_validCode_returnsTrue() = mainCoroutineRule.runBlockingTest {
        // When
        val result = checkoutRepository.applyDiscountCode("checkout123", "DISCOUNT10")

        // Then
        assertThat(result, `is`(true))
    }

    @Test
    fun applyDiscountCode_invalidCode_returnsFalse() = mainCoroutineRule.runBlockingTest {
        // When
        val result = checkoutRepository.applyDiscountCode("checkout123", "INVALIDCODE")

        // Then
        assertThat(result, `is`(false))
    }

    @Test
    fun applyShippingAddress_validAddress_returnsTrue() = mainCoroutineRule.runBlockingTest {
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
        val result = checkoutRepository.applyShippingAddress("1", mailingAddressInput)

        // Then
        assertThat(result, `is`(true))
    }
}
