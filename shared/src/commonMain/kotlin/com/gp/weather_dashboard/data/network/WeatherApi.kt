package com.gp.weather_dashboard.data.network

import com.gp.weather_dashboard.data.models.ForecastResponse
import com.gp.weather_dashboard.data.models.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherApi(private val httpClient: HttpClient) {
    
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5"
        private const val API_KEY = "" // Replace with your actual API key
    }

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherResponse {
        return httpClient.get("$BASE_URL/weather") {
            parameter("lat", latitude)
            parameter("lon", longitude)
            parameter("appid", API_KEY)
            parameter("units", "metric") // We'll convert to Fahrenheit in the mapper
        }.body()
    }

    suspend fun getHourlyForecast(latitude: Double, longitude: Double): ForecastResponse {
        return httpClient.get("$BASE_URL/forecast") {
            parameter("lat", latitude)
            parameter("lon", longitude)
            parameter("appid", API_KEY)
            parameter("units", "metric")
            parameter("cnt", 8) // 8 forecasts for next 24 hours (3-hour intervals)
        }.body()
    }
}

expect fun createHttpClient(): HttpClient