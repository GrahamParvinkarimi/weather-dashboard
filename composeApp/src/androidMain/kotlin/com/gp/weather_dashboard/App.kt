package com.gp.weather_dashboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.gp.weather_dashboard.ui.WeatherApp

@Composable
@Preview
fun App() {
    MaterialTheme {
        WeatherApp()
    }
}