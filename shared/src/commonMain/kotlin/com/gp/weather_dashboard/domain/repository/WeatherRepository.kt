package com.gp.weather_dashboard.domain.repository

import com.gp.weather_dashboard.data.model.Location
import com.gp.weather_dashboard.data.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(location: Location): Result<WeatherData>
    suspend fun getWeatherForecast(location: Location): Result<WeatherData>
    suspend fun searchLocations(query: String): Result<List<Location>>
    suspend fun getFavoriteLocations(): Flow<List<Location>>
    suspend fun addFavoriteLocation(location: Location)
    suspend fun removeFavoriteLocation(locationId: String)
    suspend fun isFavoriteLocation(locationId: String): Boolean
    suspend fun clearCache()
}