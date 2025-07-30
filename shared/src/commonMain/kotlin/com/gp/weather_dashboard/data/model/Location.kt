package com.gp.weather_dashboard.data.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock

@Serializable
data class Location(
    val id: String,
    val name: String,
    val country: String,
    val state: String? = null,
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class FavoriteLocation(
    val locationId: String,
    val addedAt: Long = Clock.System.now().toEpochMilliseconds()
)