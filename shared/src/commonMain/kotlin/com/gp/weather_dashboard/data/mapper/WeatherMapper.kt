package com.gp.weather_dashboard.data.mapper

import com.gp.weather_dashboard.data.api.response.CurrentWeatherResponse
import com.gp.weather_dashboard.data.api.response.ForecastItemResponse
import com.gp.weather_dashboard.data.api.response.ForecastResponse
import com.gp.weather_dashboard.data.api.response.GeocodeResponse
import com.gp.weather_dashboard.data.api.response.WeatherResponse
import com.gp.weather_dashboard.data.model.CurrentWeather
import com.gp.weather_dashboard.data.model.ForecastItem
import com.gp.weather_dashboard.data.model.HourlyForecast
import com.gp.weather_dashboard.data.model.Location
import com.gp.weather_dashboard.data.model.WeatherCondition
import com.gp.weather_dashboard.data.model.WeatherData
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun GeocodeResponse.toLocation(): Location {
    return Location(
        id = "${lat}_${lon}",
        name = name,
        country = country,
        state = state,
        latitude = lat,
        longitude = lon
    )
}

fun CurrentWeatherResponse.toWeatherData(location: Location): WeatherData {
    return WeatherData(
        location = location,
        current = this.toCurrentWeather()
    )
}

fun CurrentWeatherResponse.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        temperature = main.temp,
        feelsLike = main.feelsLike,
        humidity = main.humidity,
        pressure = main.pressure,
        visibility = visibility.toDouble(),
        uvIndex = 0.0, // UV Index not available in current weather endpoint
        windSpeed = wind.speed,
        windDirection = wind.deg,
        weatherCondition = weather.first().toWeatherCondition(),
        sunrise = sys.sunrise ?: 0L,
        sunset = sys.sunset ?: 0L,
        timestamp = dt * 1000L // Convert to milliseconds
    )
}

fun ForecastResponse.toDailyForecast(): List<ForecastItem> {
    // Group by date and take the first forecast item for each day
    return list
        .groupBy { forecast ->
            val instant = Instant.fromEpochSeconds(forecast.dt)
            val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
            "${localDateTime.year}-${localDateTime.monthNumber}-${localDateTime.dayOfMonth}"
        }
        .values
        .map { dailyForecasts ->
            val midDayForecast = dailyForecasts.find { it.dtTxt.contains("12:00:00") } 
                ?: dailyForecasts.first()
            
            val minTemp = dailyForecasts.minOf { it.main.tempMin }
            val maxTemp = dailyForecasts.maxOf { it.main.tempMax }
            val avgHumidity = dailyForecasts.map { it.main.humidity }.average().toInt()
            val avgWindSpeed = dailyForecasts.map { it.wind.speed }.average()
            val maxPrecipitation = dailyForecasts.maxOf { it.pop }
            
            ForecastItem(
                date = midDayForecast.dt * 1000L,
                minTemperature = minTemp,
                maxTemperature = maxTemp,
                humidity = avgHumidity,
                windSpeed = avgWindSpeed,
                weatherCondition = midDayForecast.weather.first().toWeatherCondition(),
                precipitationProbability = (maxPrecipitation * 100).toInt()
            )
        }
        .take(7) // Take only next 7 days
}

fun ForecastResponse.toHourlyForecast(): List<HourlyForecast> {
    return list.take(24).map { forecast -> // Take next 24 hours
        HourlyForecast(
            time = forecast.dt * 1000L,
            temperature = forecast.main.temp,
            weatherCondition = forecast.weather.first().toWeatherCondition(),
            precipitationProbability = (forecast.pop * 100).toInt(),
            windSpeed = forecast.wind.speed,
            humidity = forecast.main.humidity
        )
    }
}

fun WeatherResponse.toWeatherCondition(): WeatherCondition {
    return WeatherCondition(
        id = id,
        main = main,
        description = description,
        icon = icon
    )
}