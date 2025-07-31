package com.gp.weather_dashboard.di

import com.gp.weather_dashboard.data.network.WeatherApi
import com.gp.weather_dashboard.data.network.createHttpClient
import com.gp.weather_dashboard.data.repository.WeatherRepository
import com.gp.weather_dashboard.data.repository.WeatherRepositoryImpl
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule = module {
    single { createHttpClient() }
    single { WeatherApi(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    
}

expect val platformModule: Module

fun initKoin() {
    startKoin {
        modules(sharedModule, platformModule)
    }
}