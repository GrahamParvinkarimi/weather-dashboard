package com.gp.weather_dashboard.data.models

data class Location(
    val latitude: Double,
    val longitude: Double,
    val city: String? = null,
    val country: String? = null
)

sealed class LocationResult {
    data class Success(val location: Location) : LocationResult()
    data class Error(val message: String) : LocationResult()
    object PermissionDenied : LocationResult()
    object ServiceDisabled : LocationResult()
}