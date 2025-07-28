package com.gp.weather_dashboard.di

import com.gp.weather_dashboard.data.api.WeatherApiClient
import com.gp.weather_dashboard.data.api.WeatherApiClientImpl
import com.gp.weather_dashboard.data.network.HttpClientFactory
import com.gp.weather_dashboard.data.repository.InMemoryWeatherRepository
import com.gp.weather_dashboard.domain.repository.WeatherRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import com.gp.weather_dashboard.util.ApiKeyProvider

val sharedModule = module {
    
    // Network
    single { HttpClientFactory.create() }
    
    // API Client
    single<WeatherApiClient> { 
        WeatherApiClientImpl(
            httpClient = get(),
            apiKey = ApiKeyProvider.getOpenWeatherApiKey()
        )
    }
    
    // Repository
    singleOf(::InMemoryWeatherRepository) bind WeatherRepository::class
}