package com.gp.weather_dashboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.gp.weather_dashboard.ui.WeatherScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        WeatherScreen()
    }
}