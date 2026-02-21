package com.example.kotlinviikko5.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: Int = 0,          // aina 0 = “viimeisin”
    val city: String,
    val temp: Double,
    val description: String,
    val fetchedAtMillis: Long            // milloin haettu (ms)
)