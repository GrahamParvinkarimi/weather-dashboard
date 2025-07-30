package com.gp.weather_dashboard.data.models

data class WeatherDisplayData(
    val cityCode: String,
    val cityName: String,  
    val temperature: Int,
    val temperatureUnit: String = "°",
    val isToday: Boolean = true,
    val hourlyForecast: List<HourlyForecast>,
    val backgroundGradient: WeatherGradient
)

data class HourlyForecast(
    val time: String,
    val weatherIcon: WeatherIcon,
    val precipitationChance: Int,
    val temperature: Int? = null
)

enum class WeatherIcon {
    SUNNY,
    PARTLY_CLOUDY, 
    CLOUDY,
    RAINY
}

data class WeatherGradient(
    val primaryColor: String,
    val secondaryColor: String = primaryColor
)

object WeatherConstants {
    val SUNNY_GRADIENT = WeatherGradient("#f0ecc6")
    val CLOUDY_GRADIENT = WeatherGradient("#e6e6e6")
    val RAINY_GRADIENT = WeatherGradient("#b3d9ff")
    
    const val DARK_TEXT = "#000000"
    const val PRECIPITATION_COLOR = "#f8be28"
}