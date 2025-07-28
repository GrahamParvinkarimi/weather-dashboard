package com.gp.weather_dashboard.util

import com.gp.weather_dashboard.shared.BuildConfig

actual object ApiKeyProvider {
    actual fun getOpenWeatherApiKey(): String {
        return BuildConfig.OPENWEATHER_API_KEY
    }
}