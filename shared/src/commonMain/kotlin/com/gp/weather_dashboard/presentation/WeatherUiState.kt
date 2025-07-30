package com.gp.weather_dashboard.presentation

import com.gp.weather_dashboard.data.models.HourlyForecast
import com.gp.weather_dashboard.data.models.WeatherData

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherData: WeatherData? = null,
    val hourlyForecasts: List<HourlyForecast> = emptyList(),
    val errorMessage: String? = null,
    val isLocationPermissionGranted: Boolean = false,
    val isRefreshing: Boolean = false
)

sealed class WeatherUiEvent {
    object LoadWeather : WeatherUiEvent()
    object RefreshWeather : WeatherUiEvent()
    object RequestLocationPermission : WeatherUiEvent()
    object ClearError : WeatherUiEvent()
}