package com.gp.weather_dashboard.data.location

import com.gp.weather_dashboard.data.models.Location
import com.gp.weather_dashboard.data.models.LocationResult
import kotlinx.cinterop.*
import platform.CoreLocation.*
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IosLocationService : LocationService {
    
    private val locationManager = CLLocationManager()

    override suspend fun getCurrentLocation(): LocationResult {
        if (!isLocationPermissionGranted()) {
            return LocationResult.PermissionDenied
        }

        return suspendCoroutine { continuation ->
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                @OptIn(ExperimentalForeignApi::class)
                override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                    val location = didUpdateLocations.firstOrNull() as? CLLocation
                    if (location != null) {
                        val result = LocationResult.Success(
                            Location(
                                latitude = location.coordinate.useContents { latitude },
                                longitude = location.coordinate.useContents { longitude }
                            )
                        )
                        continuation.resume(result)
                        manager.stopUpdatingLocation()
                    }
                }

                override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                    continuation.resume(LocationResult.Error(didFailWithError.localizedDescription))
                    manager.stopUpdatingLocation()
                }
            }

            locationManager.delegate = delegate
            locationManager.requestLocation()
        }
    }

    override suspend fun requestLocationPermission(): Boolean {
        return suspendCoroutine { continuation ->
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
                    when (didChangeAuthorizationStatus) {
                        kCLAuthorizationStatusAuthorizedWhenInUse,
                        kCLAuthorizationStatusAuthorizedAlways -> {
                            continuation.resume(true)
                        }
                        kCLAuthorizationStatusDenied,
                        kCLAuthorizationStatusRestricted -> {
                            continuation.resume(false)
                        }
                        else -> {
                            // Still waiting for user response
                        }
                    }
                }
            }

            locationManager.delegate = delegate
            locationManager.requestWhenInUseAuthorization()
        }
    }

    override fun isLocationPermissionGranted(): Boolean {
        val status = CLLocationManager.authorizationStatus()
        return status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                status == kCLAuthorizationStatusAuthorizedAlways
    }
}