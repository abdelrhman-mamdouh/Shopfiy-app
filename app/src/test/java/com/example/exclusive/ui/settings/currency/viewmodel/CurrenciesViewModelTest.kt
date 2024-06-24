package com.example.exclusive.ui.settings.currency.viewmodel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.ui.settings.currency.CurrenciesViewModel
import com.example.exclusive.ui.settings.currency.repository.FakeCurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrenciesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var currenciesViewModel: CurrenciesViewModel
    private lateinit var fakeCurrencyRepository: FakeCurrencyRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeCurrencyRepository = FakeCurrencyRepository()
        currenciesViewModel = CurrenciesViewModel(fakeCurrencyRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenCurrencyCode_whenFetchCurrencies_thenEmitsUiState() = runTest {
        // Given
        val currencyCode = "USD"
        val expectedUiState = UiState.Success(
            Currencies("USD", 1000, mapOf("EUR" to 0.84), "2024-06-20")
        )
        fakeCurrencyRepository.setCurrenciesState(expectedUiState)

        // When
        currenciesViewModel.fetchCurrencies(currencyCode)

        // Then
        val result = currenciesViewModel.currenciesStateFlow.first()
        assertEquals(expectedUiState, result)
    }

    @Test
    fun givenCurrencyPair_whenSaveCurrency_thenUpdatesLocalCurrency() = runTest {
        // Given
        val pair = Pair("EUR", 0.84)

        // When
        currenciesViewModel.saveCurrency(pair)

        // Then
        val result = fakeCurrencyRepository.getCurrentCurrency().first()
        assertEquals(pair, result)
    }
}