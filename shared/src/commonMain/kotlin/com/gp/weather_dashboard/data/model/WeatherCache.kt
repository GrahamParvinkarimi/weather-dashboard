package com.gp.weather_dashboard.data.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock

@Serializable
data class WeatherCache(
    val locationId: String,
    val weatherData: String, // JSON serialized WeatherData
    val lastUpdated: Long,
    val expiresAt: Long
) {
    fun isExpired(): Boolean {
        return Clock.System.now().toEpochMilliseconds() > expiresAt
    }
    
    companion object {
        const val CACHE_DURATION_MINUTES = 10
        const val CACHE_DURATION_MS = CACHE_DURATION_MINUTES * 60 * 1000L
        
        fun create(locationId: String, weatherDataJson: String): WeatherCache {
            val now = Clock.System.now().toEpochMilliseconds()
            return WeatherCache(
                locationId = locationId,
                weatherData = weatherDataJson,
                lastUpdated = now,
                expiresAt = now + CACHE_DURATION_MS
            )
        }
    }
}