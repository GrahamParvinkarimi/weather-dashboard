package com.gp.weather_dashboard

import com.gp.weather_dashboard.di.sharedModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object SharedSDK {
    fun initialize(appDeclaration: KoinAppDeclaration = {}) {
        startKoin {
            appDeclaration()
            modules(sharedModule)
        }
    }
}