package com.gp.weather_dashboard.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerialName("cod")
    val cod: String,
    @SerialName("message")
    val message: Int,
    @SerialName("cnt")
    val count: Int,
    @SerialName("list")
    val list: List<ForecastItem>,
    @SerialName("city")
    val city: City
)

@Serializable
data class ForecastItem(
    @SerialName("dt")
    val dateTime: Long,
    @SerialName("main")
    val main: Main,
    @SerialName("weather")
    val weather: List<Weather>,
    @SerialName("clouds")
    val clouds: Clouds,
    @SerialName("wind")
    val wind: Wind,
    @SerialName("visibility")
    val visibility: Int,
    @SerialName("pop")
    val probabilityOfPrecipitation: Double,
    @SerialName("rain")
    val rain: Rain? = null,
    @SerialName("sys")
    val sys: ForecastSys,
    @SerialName("dt_txt")
    val dateTimeText: String
)

@Serializable
data class Rain(
    @SerialName("3h")
    val threeHour: Double? = null
)

@Serializable
data class ForecastSys(
    @SerialName("pod")
    val partOfDay: String
)

@Serializable
data class City(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("coord")
    val coordinates: Coordinates,
    @SerialName("country")
    val country: String,
    @SerialName("population")
    val population: Int,
    @SerialName("timezone")
    val timezone: Int,
    @SerialName("sunrise")
    val sunrise: Long,
    @SerialName("sunset")
    val sunset: Long
)