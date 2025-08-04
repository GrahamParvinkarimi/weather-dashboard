package com.gp.weather_dashboard.di

import com.gp.weather_dashboard.data.location.AndroidLocationService
import com.gp.weather_dashboard.data.location.LocationService
import com.gp.weather_dashboard.presentation.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single<LocationService> { AndroidLocationService(androidContext()) }
    single { WeatherViewModel(get(), get()) }
}