package com.example.kotlinviikko5.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinviikko5.BuildConfig
import com.example.kotlinviikko5.model.WeatherResponse
import com.example.kotlinviikko5.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    data object Idle : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val data: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun fetchWeather(city: String) {
        if (city.isBlank()) {
            _uiState.value = WeatherUiState.Error("Anna kaupungin nimi.")
            return
        }

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val result = RetrofitInstance.api.getWeatherByCity(
                    city = city.trim(),
                    apiKey = BuildConfig.OPENWEATHER_API_KEY
                )
                _uiState.value = WeatherUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    e.message ?: "Virhe haettaessa säätä."
                )
            }
        }
    }
}
