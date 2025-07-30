package com.gp.weather_dashboard.presentation

import com.gp.weather_dashboard.data.models.*

class WeatherViewModel {
    
    fun getWeatherDisplayData(): WeatherDisplayData {
        return WeatherDisplayData(
            cityCode = "JKT",
            cityName = "Jakarta",
            temperature = 28,
            temperatureUnit = "°",
            isToday = true,
            hourlyForecast = listOf(
                HourlyForecast("9 AM", WeatherIcon.SUNNY, 6),
                HourlyForecast("10 AM", WeatherIcon.SUNNY, 7),
                HourlyForecast("11 AM", WeatherIcon.PARTLY_CLOUDY, 8),
                HourlyForecast("12 PM", WeatherIcon.SUNNY, 12),
                HourlyForecast("1 PM", WeatherIcon.CLOUDY, 20),
                HourlyForecast("2 PM", WeatherIcon.CLOUDY, 20),
                HourlyForecast("3 PM", WeatherIcon.CLOUDY, 20),
                HourlyForecast("4 PM", WeatherIcon.CLOUDY, 20),
                HourlyForecast("5 PM", WeatherIcon.CLOUDY, 20),
                HourlyForecast("6 PM", WeatherIcon.CLOUDY, 20),
                HourlyForecast("7 PM", WeatherIcon.CLOUDY, 20)
            ),
            backgroundGradient = WeatherConstants.SUNNY_GRADIENT
        )
    }
}