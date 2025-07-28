package com.gp.weather_dashboard.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    val coord: Coordinates,
    val weather: List<WeatherResponse>,
    val main: MainResponse,
    val visibility: Int,
    val wind: WindResponse,
    val sys: SysResponse,
    val dt: Long,
    val timezone: Int,
    val id: Long,
    val name: String
)

@Serializable
data class ForecastResponse(
    val list: List<ForecastItemResponse>,
    val city: CityResponse
)

@Serializable
data class ForecastItemResponse(
    val dt: Long,
    @SerialName("dt_txt")
    val dtTxt: String,
    val main: MainResponse,
    val weather: List<WeatherResponse>,
    val wind: WindResponse,
    val visibility: Int,
    val pop: Double, // Probability of precipitation
    val sys: SysResponse
)

@Serializable
data class GeocodeResponse(
    val name: String,
    @SerialName("local_names")
    val localNames: Map<String, String>? = null,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)

@Serializable
data class Coordinates(
    val lon: Double,
    val lat: Double
)

@Serializable
data class WeatherResponse(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class MainResponse(
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    @SerialName("temp_min")
    val tempMin: Double,
    @SerialName("temp_max")
    val tempMax: Double,
    val pressure: Double,
    val humidity: Int,
    @SerialName("sea_level")
    val seaLevel: Double? = null,
    @SerialName("grnd_level")
    val grndLevel: Double? = null
)

@Serializable
data class WindResponse(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)

@Serializable
data class SysResponse(
    val type: Int? = null,
    val id: Int? = null,
    val country: String? = null,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val pod: String? = null // Part of day (d/n)
)

@Serializable
data class CityResponse(
    val id: Long,
    val name: String,
    val coord: Coordinates,
    val country: String,
    val population: Long? = null,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)