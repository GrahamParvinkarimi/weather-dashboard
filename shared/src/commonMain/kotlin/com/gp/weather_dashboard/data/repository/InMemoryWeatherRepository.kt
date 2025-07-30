package com.gp.weather_dashboard.data.repository

import com.gp.weather_dashboard.data.api.WeatherApiClient
import com.gp.weather_dashboard.data.mapper.toDailyForecast
import com.gp.weather_dashboard.data.mapper.toHourlyForecast
import com.gp.weather_dashboard.data.mapper.toLocation
import com.gp.weather_dashboard.data.mapper.toWeatherData
import com.gp.weather_dashboard.data.mapper.toCurrentWeather
import com.gp.weather_dashboard.data.model.Location
import com.gp.weather_dashboard.data.model.WeatherData
import com.gp.weather_dashboard.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock

class InMemoryWeatherRepository(
    private val apiClient: WeatherApiClient
) : WeatherRepository {
    
    private val _favoriteLocations = MutableStateFlow<List<Location>>(emptyList())
    private val weatherCache = mutableMapOf<String, Pair<WeatherData, Long>>()
    
    companion object {
        private const val CACHE_DURATION_MS = 10 * 60 * 1000L // 10 minutes
    }
    
    override suspend fun getCurrentWeather(location: Location): Result<WeatherData> {
        return try {
            // Check cache first
            val cached = weatherCache[location.id]
            if (cached != null && Clock.System.now().toEpochMilliseconds() - cached.second < CACHE_DURATION_MS) {
                return Result.success(cached.first)
            }
            
            // Fetch from API
            val response = apiClient.getCurrentWeather(location.latitude, location.longitude)
            val weatherData = response.toWeatherData(location)
            
            // Cache the result
            weatherCache[location.id] = weatherData to Clock.System.now().toEpochMilliseconds()
            
            Result.success(weatherData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getWeatherForecast(location: Location): Result<WeatherData> {
        return try {
            // Check cache first
            val cacheKey = "${location.id}_forecast"
            val cached = weatherCache[cacheKey]
            if (cached != null && Clock.System.now().toEpochMilliseconds() - cached.second < CACHE_DURATION_MS) {
                return Result.success(cached.first)
            }
            
            // Fetch current weather and forecast from API
            val currentWeatherResponse = apiClient.getCurrentWeather(location.latitude, location.longitude)
            val forecastResponse = apiClient.getForecast(location.latitude, location.longitude)
            
            val weatherData = WeatherData(
                location = location,
                current = currentWeatherResponse.toCurrentWeather(),
                forecast = forecastResponse.toDailyForecast(),
                hourlyForecast = forecastResponse.toHourlyForecast()
            )
            
            // Cache the result
            weatherCache[cacheKey] = weatherData to Clock.System.now().toEpochMilliseconds()
            
            Result.success(weatherData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchLocations(query: String): Result<List<Location>> {
        return try {
            val response = apiClient.searchLocations(query)
            val locations = response.map { it.toLocation() }
            
            Result.success(locations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getFavoriteLocations(): Flow<List<Location>> {
        return _favoriteLocations.asStateFlow()
    }
    
    override suspend fun addFavoriteLocation(location: Location) {
        val currentFavorites = _favoriteLocations.value.toMutableList()
        if (!currentFavorites.any { it.id == location.id }) {
            currentFavorites.add(location)
            _favoriteLocations.value = currentFavorites
        }
    }
    
    override suspend fun removeFavoriteLocation(locationId: String) {
        val currentFavorites = _favoriteLocations.value.toMutableList()
        currentFavorites.removeAll { it.id == locationId }
        _favoriteLocations.value = currentFavorites
    }
    
    override suspend fun isFavoriteLocation(locationId: String): Boolean {
        return _favoriteLocations.value.any { it.id == locationId }
    }
    
    override suspend fun clearCache() {
        weatherCache.clear()
    }
}