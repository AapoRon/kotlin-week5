package com.example.kotlinviikko5.repository

import com.example.kotlinviikko5.BuildConfig
import com.example.kotlinviikko5.local.WeatherDao
import com.example.kotlinviikko5.model.WeatherEntity
import com.example.kotlinviikko5.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val dao: WeatherDao
) {
    fun observeLatest(): Flow<WeatherEntity?> = dao.observeLatest()

    suspend fun refreshIfStale(city: String, maxAgeMillis: Long = 30 * 60 * 1000L) {
        val now = System.currentTimeMillis()

        val cached = dao.getLatestOnce()
        val isStale = cached == null || (now - cached.fetchedAtMillis) > maxAgeMillis
        val isDifferentCity = cached?.city?.equals(city, ignoreCase = true) == false

        if (!isStale && !isDifferentCity) return

        withContext(Dispatchers.IO) {
            val response = RetrofitInstance.api.getWeatherByCity(
                city = city,
                apiKey = BuildConfig.OPENWEATHER_API_KEY
            )

            val entity = WeatherEntity(
                id = 0,
                city = response.name,
                temp = response.main.temp,
                description = response.weather.firstOrNull()?.description ?: "-",
                fetchedAtMillis = now
            )

            dao.upsertLatest(entity)
        }
    }
}