package com.gp.weather_dashboard.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    val location: Location,
    val current: CurrentWeather,
    val forecast: List<ForecastItem> = emptyList(),
    val hourlyForecast: List<HourlyForecast> = emptyList()
)

@Serializable
data class CurrentWeather(
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Double,
    val visibility: Double,
    val uvIndex: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val weatherCondition: WeatherCondition,
    val sunrise: Long,
    val sunset: Long,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class ForecastItem(
    val date: Long,
    val minTemperature: Double,
    val maxTemperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val weatherCondition: WeatherCondition,
    val precipitationProbability: Int
)

@Serializable
data class HourlyForecast(
    val time: Long,
    val temperature: Double,
    val weatherCondition: WeatherCondition,
    val precipitationProbability: Int,
    val windSpeed: Double,
    val humidity: Int
)

@Serializable
data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)