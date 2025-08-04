package com.gp.weather_dashboard.di

import com.gp.weather_dashboard.presentation.WeatherViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DiHelper : KoinComponent {
    fun getWeatherViewModel(): WeatherViewModel {
        val viewModel: WeatherViewModel by inject()
        return viewModel
    }
}