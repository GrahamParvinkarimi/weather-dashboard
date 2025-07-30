package com.gp.weather_dashboard.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gp.weather_dashboard.data.models.HourlyForecast
import com.gp.weather_dashboard.presentation.WeatherUiState
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherContent(
    uiState: WeatherUiState,
    onRefresh: () -> Unit,
    onRequestPermission: () -> Unit,
    onClearError: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5E6A3))
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF8B4513)
            )
        } else if (uiState.errorMessage != null && !uiState.isLocationPermissionGranted) {
            PermissionRequestCard(
                onRequestPermission = onRequestPermission,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (uiState.errorMessage != null) {
            val errorMsg = uiState.errorMessage ?: ""
            ErrorCard(
                message = errorMsg,
                onDismiss = onClearError,
                onRetry = onRefresh,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (uiState.weatherData != null) {
            val weatherData = uiState.weatherData
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                
                // Today text
                Text(
                    text = "Today",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Main weather display
                MainWeatherDisplay(
                    temperature = weatherData?.temperature?.roundToInt() ?: 0,
                    cityCode = weatherData?.cityName ?: "",
                    modifier = Modifier.weight(1f)
                )
                
                // Hourly forecast
                if (uiState.hourlyForecasts.isNotEmpty()) {
                    HourlyForecastSection(
                        forecasts = uiState.hourlyForecasts,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
                
                // City navigation
                CityNavigation(
                    cityName = weatherData?.cityName ?: "",
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
        
        // Refresh button
        FloatingActionButton(
            onClick = onRefresh,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            containerColor = Color.White.copy(alpha = 0.8f)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun MainWeatherDisplay(
    temperature: Int,
    cityCode: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large circular background
        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.Center
        ) {
            // Yellow circle background
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color(0xFFFFD54F),
                    radius = size.minDimension / 2,
                    center = center
                )
                
                // Small circle on top right
                drawCircle(
                    color = Color(0xFFFFE082),
                    radius = 24.dp.toPx(),
                    center = Offset(
                        x = center.x + 100.dp.toPx(),
                        y = center.y - 100.dp.toPx()
                    )
                )
            }
            
            // Temperature text
            Text(
                text = "$temperature°",
                fontSize = 96.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Location pin icon and city
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(modifier = Modifier.size(16.dp)) {
                drawCircle(
                    color = Color.Black,
                    radius = 8.dp.toPx(),
                    center = center
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = cityCode,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                color = Color.Black,
                letterSpacing = 8.sp
            )
        }
    }
}

@Composable
fun HourlyForecastSection(
    forecasts: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Weather chart line
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            val path = Path()
            val points = forecasts.take(7).mapIndexed { index, forecast ->
                val x = (size.width / 6f) * index
                val y = size.height / 2f // Simplified line for now
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                Offset(x, y)
            }
            
            drawPath(path, Color.Black, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
            
            // Draw points
            points.forEach { point ->
                drawCircle(
                    color = Color.Black,
                    radius = 4.dp.toPx(),
                    center = point
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Time and weather icons
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(forecasts.take(7)) { forecast ->
                HourlyForecastItem(forecast = forecast)
            }
        }
    }
}

@Composable
fun HourlyForecastItem(forecast: HourlyForecast) {
    val date = Date(forecast.dateTime * 1000) // Convert to milliseconds
    val formatter = SimpleDateFormat("h a", Locale.getDefault())
    val hour = formatter.format(date)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(50.dp)
    ) {
        Text(
            text = hour,
            fontSize = 12.sp,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Weather icon placeholder
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = when {
                        forecast.description.contains("rain") -> Color(0xFF64B5F6)
                        forecast.description.contains("cloud") -> Color(0xFF90A4AE)
                        else -> Color(0xFFFFD54F)
                    },
                    shape = CircleShape
                )
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "${(forecast.probabilityOfPrecipitation * 100).roundToInt()}%",
            fontSize = 10.sp,
            color = Color.Black
        )
    }
}

@Composable
fun CityNavigation(
    cityName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Refresh, // Using refresh as arrow placeholder
            contentDescription = "Previous",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        
        Text(
            text = cityName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        
        Icon(
            imageVector = Icons.Default.Refresh, // Using refresh as arrow placeholder
            contentDescription = "Next",
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun PermissionRequestCard(
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Location Permission Required",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "This app needs location permission to show weather for your current location.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(onClick = onRequestPermission) {
                Text("Grant Permission")
            }
        }
    }
}

@Composable
fun ErrorCard(
    message: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row {
                TextButton(onClick = onDismiss) {
                    Text("Dismiss")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}