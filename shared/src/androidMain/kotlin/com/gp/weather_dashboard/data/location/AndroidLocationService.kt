package com.gp.weather_dashboard.data.location

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.gp.weather_dashboard.data.models.Location
import com.gp.weather_dashboard.data.models.LocationResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class AndroidLocationService(
    private val context: Context
) : LocationService {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override suspend fun getCurrentLocation(): LocationResult {
        if (!isLocationPermissionGranted()) {
            return LocationResult.PermissionDenied
        }

        if (!isLocationEnabled()) {
            return LocationResult.ServiceDisabled
        }

        return try {
            // First try to get last known location (much faster and more reliable)
            val lastKnownLocation = getLastKnownLocation()
            if (lastKnownLocation != null) {
                return LocationResult.Success(
                    Location(
                        latitude = lastKnownLocation.latitude,
                        longitude = lastKnownLocation.longitude
                    )
                )
            }

            // If no last known location, try to get fresh location with timeout
            withTimeoutOrNull(10000) {
                suspendCancellableCoroutine<LocationResult> { continuation ->
                    var resumed = false
                    val listener = android.location.LocationListener { location ->
                        if (!resumed) {
                            resumed = true
                            continuation.resume(LocationResult.Success(
                                Location(
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                            ))
                        }
                    }

                    try {
                        // Try both providers
                        val providers = listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)
                        var requestMade = false
                        
                        for (provider in providers) {
                            if (locationManager.isProviderEnabled(provider)) {
                                locationManager.requestSingleUpdate(provider, listener, null)
                                requestMade = true
                                break
                            }
                        }
                        
                        if (!requestMade) {
                            continuation.resume(LocationResult.ServiceDisabled)
                        }
                    } catch (e: SecurityException) {
                        if (!resumed) {
                            resumed = true
                            continuation.resume(LocationResult.PermissionDenied)
                        }
                    } catch (e: Exception) {
                        if (!resumed) {
                            resumed = true
                            continuation.resume(LocationResult.Error(e.message ?: "Unknown location error"))
                        }
                    }

                    continuation.invokeOnCancellation {
                        try {
                            locationManager.removeUpdates(listener)
                        } catch (_: Exception) {
                            // Ignore cleanup errors
                        }
                    }
                }
            } ?: LocationResult.Error("Unable to get location - try enabling location services")
        } catch (e: Exception) {
            LocationResult.Error(e.message ?: "Unknown location error")
        }
    }

    override suspend fun requestLocationPermission(): Boolean {
        // This should be handled by the UI layer
        return isLocationPermissionGranted()
    }

    override fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getLastKnownLocation(): android.location.Location? {
        return try {
            val providers = listOf(LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER)
            for (provider in providers) {
                if (locationManager.isProviderEnabled(provider)) {
                    locationManager.getLastKnownLocation(provider)?.let { return it }
                }
            }
            null
        } catch (_: SecurityException) {
            null
        }
    }
}