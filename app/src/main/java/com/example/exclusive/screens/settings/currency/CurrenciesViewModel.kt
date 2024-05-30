package com.example.exclusive.screens.settings.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.model.Currencies
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _currenciesStateFlow = MutableStateFlow<UiState<Currencies>>(UiState.Idle)
    val currenciesStateFlow: StateFlow<UiState<Currencies>> get() = _currenciesStateFlow

    init {
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        viewModelScope.launch {
            currencyRepository.getCurrencies()
                .catch { e -> _currenciesStateFlow.value = UiState.Error(e) }
                .collect { uiState -> _currenciesStateFlow.value = uiState }
        }
    }
}