package com.gp.weather_dashboard.di

import com.gp.weather_dashboard.data.location.IosLocationService
import com.gp.weather_dashboard.data.location.LocationService
import com.gp.weather_dashboard.presentation.WeatherViewModel
import org.koin.dsl.module

actual val platformModule = module {
    single<LocationService> { IosLocationService() }
    single { WeatherViewModel(get(), get()) }
}