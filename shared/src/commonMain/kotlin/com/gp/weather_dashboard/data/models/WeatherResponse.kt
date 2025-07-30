package com.gp.weather_dashboard.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("coord")
    val coordinates: Coordinates,
    @SerialName("weather")
    val weather: List<Weather>,
    @SerialName("base")
    val base: String,
    @SerialName("main")
    val main: Main,
    @SerialName("visibility")
    val visibility: Int,
    @SerialName("wind")
    val wind: Wind,
    @SerialName("clouds")
    val clouds: Clouds,
    @SerialName("dt")
    val dateTime: Long,
    @SerialName("sys")
    val sys: Sys,
    @SerialName("timezone")
    val timezone: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("cod")
    val cod: Int
)

@Serializable
data class Coordinates(
    @SerialName("lon")
    val longitude: Double,
    @SerialName("lat")
    val latitude: Double
)

@Serializable
data class Weather(
    @SerialName("id")
    val id: Int,
    @SerialName("main")
    val main: String,
    @SerialName("description")
    val description: String,
    @SerialName("icon")
    val icon: String
)

@Serializable
data class Main(
    @SerialName("temp")
    val temperature: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    @SerialName("temp_min")
    val tempMin: Double,
    @SerialName("temp_max")
    val tempMax: Double,
    @SerialName("pressure")
    val pressure: Int,
    @SerialName("humidity")
    val humidity: Int,
    @SerialName("sea_level")
    val seaLevel: Int? = null,
    @SerialName("grnd_level")
    val groundLevel: Int? = null
)

@Serializable
data class Wind(
    @SerialName("speed")
    val speed: Double,
    @SerialName("deg")
    val degrees: Int,
    @SerialName("gust")
    val gust: Double? = null
)

@Serializable
data class Clouds(
    @SerialName("all")
    val all: Int
)

@Serializable
data class Sys(
    @SerialName("type")
    val type: Int? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("country")
    val country: String,
    @SerialName("sunrise")
    val sunrise: Long,
    @SerialName("sunset")
    val sunset: Long
)