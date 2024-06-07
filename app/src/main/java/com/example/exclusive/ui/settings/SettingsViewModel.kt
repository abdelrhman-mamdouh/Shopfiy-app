package com.example.exclusive.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exclusive.data.remote.UiState
import com.example.exclusive.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _currenciesStateFlow = MutableStateFlow<UiState<Pair<String, Double>>>(UiState.Idle)
    val currenciesStateFlow: StateFlow<UiState<Pair<String, Double>>> get() = _currenciesStateFlow
    fun fetchCurrencies() {
        viewModelScope.launch {
            currencyRepository.getCurrentCurrency().collect { currency ->
                _currenciesStateFlow.value = UiState.Success(currency)
            }
        }
    }
}