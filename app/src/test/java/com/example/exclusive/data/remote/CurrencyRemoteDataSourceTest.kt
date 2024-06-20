package com.example.exclusive.data.remote

import com.example.exclusive.data.model.Currencies
import com.example.fake.FakeApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyRemoteDataSourceTest {

    private lateinit var fakeApiService: FakeApiService
    private lateinit var currencyRemoteDataSource: CurrencyRemoteDataSource

    @Before
    fun setUp() {
        fakeApiService = FakeApiService()
        currencyRemoteDataSource = CurrencyRemoteDataSource(fakeApiService)
    }

    @Test
    fun givenSuccessfulResponse_whenGetCurrencies_thenEmitSuccessState() = runBlockingTest {
        // Given
        val fakeCurrencies = Currencies(
            base = "USD",
            ms = 1000,
            results = mapOf("EUR" to 0.84),
            updated = "2024-06-20"
        )
        fakeApiService.fakeCurrenciesResponse = fakeCurrencies

        // When
        val result = currencyRemoteDataSource.getCurrencies("USD").first()

        // Then
        assertFalse(result is UiState.Success)
    }

    @Test
    fun givenErrorResponse_whenGetCurrencies_thenEmitErrorState() = runTest {
        // Given
        fakeApiService.shouldReturnError = true

        // When
        val result = currencyRemoteDataSource.getCurrencies("USD").first()

        // Then
        assertFalse(result is UiState.Error)
    }

    @Test
    fun givenIOException_whenGetCurrencies_thenEmitErrorState() = runTest {
        // Given
        fakeApiService.shouldReturnError = true

        // When
        val result = currencyRemoteDataSource.getCurrencies("USD").first()

        // Then
        assertFalse(result is UiState.Success)
    }
}