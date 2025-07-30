package com.gp.weather_dashboard.di

import org.koin.core.context.startKoin

fun initKoinIos() {
    startKoin {
        modules(sharedModule, platformModule)
    }
}