package com.gp.weather_dashboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gp.weather_dashboard.presentation.WeatherViewModel
import org.koin.compose.koinInject

@Composable
fun WeatherApp() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFF5E6A3), // Light yellow from Figma
            secondary = Color(0xFFFFE082),
            background = Color(0xFFF5E6A3),
            surface = Color(0xFFF5E6A3)
        )
    ) {
        WeatherScreen()
    }
}

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Check for location permission and request if needed
        if (!uiState.isLocationPermissionGranted) {
            // This would typically be handled with permission request launcher
            // For now, we'll just try to load weather
            viewModel.handleEvent(com.gp.weather_dashboard.presentation.WeatherUiEvent.LoadWeather)
        }
    }

    WeatherContent(
        uiState = uiState,
        onRefresh = { 
            viewModel.handleEvent(com.gp.weather_dashboard.presentation.WeatherUiEvent.RefreshWeather) 
        },
        onRequestPermission = { 
            viewModel.handleEvent(com.gp.weather_dashboard.presentation.WeatherUiEvent.RequestLocationPermission) 
        },
        onClearError = { 
            viewModel.handleEvent(com.gp.weather_dashboard.presentation.WeatherUiEvent.ClearError) 
        }
    )
}