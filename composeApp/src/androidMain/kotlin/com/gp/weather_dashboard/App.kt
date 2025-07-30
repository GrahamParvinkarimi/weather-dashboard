package com.gp.weather_dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

data class HourlyWeather(
    val time: String,
    val temperature: Int,
    val precipitationChance: Int,
    val isRainy: Boolean = false
)

@Composable
@Preview
fun App() {
    WeatherDashboard()
}

@Composable
fun WeatherDashboard() {
    val sunnyGradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFFF8BE28), // Golden yellow center
            Color(0xFFF0ECC6)  // Light cream background
        ),
        radius = 800f
    )
    
    val hourlyData = listOf(
        HourlyWeather("9 AM", 28, 6),
        HourlyWeather("10 AM", 29, 7),
        HourlyWeather("11 AM", 30, 8, true),
        HourlyWeather("12 PM", 31, 12),
        HourlyWeather("1 PM", 32, 20, true),
        HourlyWeather("2 PM", 31, 20, true),
        HourlyWeather("3 PM", 30, 20, true),
        HourlyWeather("4 PM", 29, 20, true),
        HourlyWeather("5 PM", 28, 20, true),
        HourlyWeather("6 PM", 27, 20, true),
        HourlyWeather("7 PM", 26, 20, true)
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(sunnyGradient)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status bar space
            Spacer(modifier = Modifier.height(44.dp))
            
            // Today text
            Text(
                text = "Today",
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 24.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Main temperature display
            Box(
                modifier = Modifier
                    .size(332.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFF8BE28).copy(alpha = 0.8f),
                                Color(0xFFF8BE28).copy(alpha = 0.4f),
                                Color.Transparent
                            ),
                            radius = 300f
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "28",
                            fontSize = 200.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            lineHeight = 86.sp
                        )
                        Text(
                            text = "°",
                            fontSize = 140.sp,
                            fontWeight = FontWeight.Thin,
                            color = Color.Black,
                            modifier = Modifier.offset(y = (-20).dp)
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.offset(y = (-40).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    
                    Text(
                        text = "JKT",
                        fontSize = 178.sp,
                        fontWeight = FontWeight.Thin,
                        color = Color.Black,
                        lineHeight = 230.sp,
                        modifier = Modifier.offset(y = (-80).dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Hourly forecast
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(hourlyData) { hour ->
                    HourlyWeatherItem(hour)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous",
                        tint = Color.Black
                    )
                }
                
                Text(
                    text = "Jakarta",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Light
                )
                
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = Color.Black
                    )
                }
            }
            
            // Home indicator
            Box(
                modifier = Modifier
                    .width(135.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0xFF181818))
                    .padding(bottom = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HourlyWeatherItem(weather: HourlyWeather) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = weather.time,
            fontSize = 12.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        
        // Weather icon placeholder (you would use actual weather icons here)
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(
                    if (weather.isRainy) Color(0xFFE0E0E0) else Color(0xFFF8BE28),
                    CircleShape
                )
        )
        
        // Precipitation chance
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(11.dp, 16.dp)
                    .background(Color(0xFFF8BE28))
            )
            Text(
                text = "${weather.precipitationChance}%",
                fontSize = 12.sp,
                color = Color(0xFFF8BE28),
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}