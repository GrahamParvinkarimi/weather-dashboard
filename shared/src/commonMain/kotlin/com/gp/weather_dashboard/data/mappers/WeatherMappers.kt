package com.gp.weather_dashboard.data.mappers

import com.gp.weather_dashboard.data.models.*

fun WeatherResponse.toWeatherData(): WeatherData {
    return WeatherData(
        temperature = main.temperature,
        feelsLike = main.feelsLike,
        humidity = main.humidity,
        pressure = main.pressure,
        windSpeed = wind.speed,
        description = weather.firstOrNull()?.description ?: "",
        icon = weather.firstOrNull()?.icon ?: "",
        cityName = name,
        countryCode = sys.country,
        latitude = coordinates.latitude,
        longitude = coordinates.longitude,
        sunrise = sys.sunrise,
        sunset = sys.sunset,
        dateTime = dateTime
    )
}

fun ForecastResponse.toHourlyForecasts(): List<HourlyForecast> {
    return list.map { item ->
        HourlyForecast(
            dateTime = item.dateTime,
            temperature = item.main.temperature,
            description = item.weather.firstOrNull()?.description ?: "",
            icon = item.weather.firstOrNull()?.icon ?: "",
            probabilityOfPrecipitation = item.probabilityOfPrecipitation
        )
    }
}

fun Double.celsiusToFahrenheit(): Double {
    return this * 9 / 5 + 32
}

fun Double.kelvinToCelsius(): Double {
    return this - 273.15
}

fun String.toCityAbbreviation(): String {
    return when (this.lowercase()) {
        "jakarta" -> "JKT"
        "new york" -> "NYC"
        "los angeles" -> "LAX"
        "san francisco" -> "SFO"
        "chicago" -> "CHI"
        "london" -> "LON"
        "paris" -> "PAR"
        "tokyo" -> "TYO"
        "sydney" -> "SYD"
        else -> this.take(3).uppercase()
    }
}