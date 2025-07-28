package com.gp.weather_dashboard.util

expect object ApiKeyProvider {
    fun getOpenWeatherApiKey(): String
}