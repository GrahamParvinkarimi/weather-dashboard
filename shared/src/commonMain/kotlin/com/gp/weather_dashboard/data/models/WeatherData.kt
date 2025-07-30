package com.gp.weather_dashboard.data.models

data class WeatherData(
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val description: String,
    val icon: String,
    val cityName: String,
    val countryCode: String,
    val latitude: Double,
    val longitude: Double,
    val sunrise: Long,
    val sunset: Long,
    val dateTime: Long
)

data class HourlyForecast(
    val dateTime: Long,
    val temperature: Double,
    val description: String,
    val icon: String,
    val probabilityOfPrecipitation: Double
)

sealed class WeatherResult {
    data class Success(val data: WeatherData) : WeatherResult()
    data class Error(val message: String) : WeatherResult()
}

sealed class ForecastResult {
    data class Success(val forecasts: List<HourlyForecast>) : ForecastResult()
    data class Error(val message: String) : ForecastResult()
}