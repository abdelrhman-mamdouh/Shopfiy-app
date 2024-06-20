package com.example.exclusive.data.repository

import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.UiState
import com.example.fake.FakeCurrencyRemoteDataSource
import com.example.fake.FakeLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyRepositoryImplTest {

    private lateinit var fakeRemoteDataSource: FakeCurrencyRemoteDataSource
    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var currencyRepository: CurrencyRepositoryImpl

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeCurrencyRemoteDataSource()
        fakeLocalDataSource = FakeLocalDataSource()
        currencyRepository = CurrencyRepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun givenCurrencyCode_whenGetCurrencies_thenReturnsCorrectState() = runTest {
        // Given
        val currencyCode = "USD"
        val expectedUiState = UiState.Success(
            Currencies("USD", 1000, mapOf("EUR" to 0.84), "2024-06-20")
        )
        fakeRemoteDataSource.setCurrenciesState(expectedUiState)

        // When
        val result = currencyRepository.getCurrencies(currencyCode).first()

        // Then
        assertEquals(expectedUiState, result)
    }

    @Test
    fun givenCurrencyPair_whenSaveCurrency_thenUpdatesLocalCurrency() = runTest {
        // Given
        val pair = Pair("EUR", 0.84)

        // When
        currencyRepository.saveCurrency(pair)

        // Then
        val result = fakeLocalDataSource.getCurrency()
        assertEquals(pair, result)
    }
}