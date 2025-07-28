package com.gp.weather_dashboard

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform