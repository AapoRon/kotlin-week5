package com.example.kotlinviikko5.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinviikko5.model.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE id = 0 LIMIT 1")
    fun observeLatest(): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather WHERE id = 0 LIMIT 1")
    suspend fun getLatestOnce(): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLatest(entity: WeatherEntity)
}