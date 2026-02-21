package com.example.kotlinviikko5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinviikko5.local.DatabaseProvider
import com.example.kotlinviikko5.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: WeatherRepository by lazy {
        val db = DatabaseProvider.get(app)
        WeatherRepository(db.weatherDao())
    }

    val latestWeather = repo.observeLatest() // Flow -> UI collectAsState()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun fetchWeather(city: String) {
        if (city.isBlank()) {
            _error.value = "Anna kaupungin nimi."
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                repo.refreshIfStale(city.trim())
            } catch (e: Exception) {
                _error.value = e.message ?: "Virhe haettaessa säätä."
            } finally {
                _loading.value = false
            }
        }
    }
}