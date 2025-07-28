package com.gp.weather_dashboard.util

actual object ApiKeyProvider {
    actual fun getOpenWeatherApiKey(): String {
        // In a real app, this would come from iOS Info.plist or secure storage
        return "51d6b7558285aefc6d509827acb45b22"
    }
}