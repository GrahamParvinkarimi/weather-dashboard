package com.gp.weather_dashboard.data.location

import com.gp.weather_dashboard.data.models.LocationResult

interface LocationService {
    suspend fun getCurrentLocation(): LocationResult
    suspend fun requestLocationPermission(): Boolean
    fun isLocationPermissionGranted(): Boolean
}