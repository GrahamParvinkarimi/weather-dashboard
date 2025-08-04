package com.gp.weather_dashboard.data.repository

import com.gp.weather_dashboard.data.models.*
import com.gp.weather_dashboard.data.mappers.toHourlyForecasts
import com.gp.weather_dashboard.data.mappers.toWeatherData
import com.gp.weather_dashboard.data.network.WeatherApi
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherResult
    suspend fun getHourlyForecast(latitude: Double, longitude: Double): ForecastResult
}

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherResult {
        return try {
            val response = weatherApi.getCurrentWeather(latitude, longitude)
            WeatherResult.Success(response.toWeatherData())
        } catch (e: Exception) {
            WeatherResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getHourlyForecast(latitude: Double, longitude: Double): ForecastResult {
        return try {
            val response = weatherApi.getHourlyForecast(latitude, longitude)
            ForecastResult.Success(response.toHourlyForecasts())
        } catch (e: Exception) {
            ForecastResult.Error(e.message ?: "Unknown error occurred")
        }
    }
}