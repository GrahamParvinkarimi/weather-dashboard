package com.gp.weather_dashboard.data.api

import com.gp.weather_dashboard.data.api.response.CurrentWeatherResponse
import com.gp.weather_dashboard.data.api.response.ForecastResponse
import com.gp.weather_dashboard.data.api.response.GeocodeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface WeatherApiClient {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeatherResponse
    suspend fun getForecast(latitude: Double, longitude: Double): ForecastResponse
    suspend fun searchLocations(query: String, limit: Int = 5): List<GeocodeResponse>
}

class WeatherApiClientImpl(
    private val httpClient: HttpClient,
    private val apiKey: String
) : WeatherApiClient {
    
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org"
        private const val WEATHER_ENDPOINT = "$BASE_URL/data/2.5/weather"
        private const val FORECAST_ENDPOINT = "$BASE_URL/data/2.5/forecast"
        private const val GEOCODE_ENDPOINT = "$BASE_URL/geo/1.0/direct"
        private const val UNITS = "metric"
    }
    
    override suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeatherResponse {
        return httpClient.get(WEATHER_ENDPOINT) {
            parameter("lat", latitude)
            parameter("lon", longitude)
            parameter("appid", apiKey)
            parameter("units", UNITS)
        }.body()
    }
    
    override suspend fun getForecast(latitude: Double, longitude: Double): ForecastResponse {
        return httpClient.get(FORECAST_ENDPOINT) {
            parameter("lat", latitude)
            parameter("lon", longitude)
            parameter("appid", apiKey)
            parameter("units", UNITS)
        }.body()
    }
    
    override suspend fun searchLocations(query: String, limit: Int): List<GeocodeResponse> {
        return httpClient.get(GEOCODE_ENDPOINT) {
            parameter("q", query)
            parameter("limit", limit)
            parameter("appid", apiKey)
        }.body()
    }
}