package com.gp.weather_dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gp.weather_dashboard.data.models.*
import com.gp.weather_dashboard.presentation.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = WeatherViewModel()
) {
    val weatherData = viewModel.getWeatherDisplayData()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor(weatherData.backgroundGradient.primaryColor)))
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Today",
            fontSize = 16.sp,
            color = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT)),
            fontWeight = FontWeight.Normal
        )
        
        Spacer(modifier = Modifier.height(40.dp))
        
        Box(
            modifier = Modifier
                .size(332.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFD700).copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = weatherData.temperature.toString(),
                    fontSize = 200.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT)),
                    lineHeight = 86.sp
                )
                Text(
                    text = weatherData.temperatureUnit,
                    fontSize = 140.sp,
                    fontWeight = FontWeight.Thin,
                    color = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT)),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier
                    .size(24.dp)
                    .offset(y = 60.dp),
                tint = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT))
            )
            
            Text(
                text = weatherData.cityCode,
                fontSize = 178.sp,
                fontWeight = FontWeight.Thin,
                color = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT)),
                modifier = Modifier.offset(y = 140.dp),
                lineHeight = 230.sp
            )
        }
        
        Spacer(modifier = Modifier.height(60.dp))
        
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(weatherData.hourlyForecast) { forecast ->
                HourlyForecastItem(forecast = forecast)
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous",
                modifier = Modifier.size(24.dp),
                tint = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT))
            )
            
            Text(
                text = weatherData.cityName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT))
            )
            
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next",
                modifier = Modifier.size(24.dp),
                tint = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT))
            )
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun HourlyForecastItem(forecast: HourlyForecast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = forecast.time,
            fontSize = 12.sp,
            color = Color(android.graphics.Color.parseColor(WeatherConstants.DARK_TEXT)),
            textAlign = TextAlign.Center
        )
        
        Box(
            modifier = Modifier
                .size(29.dp, 22.dp)
                .background(
                    color = when (forecast.weatherIcon) {
                        WeatherIcon.SUNNY -> Color(0xFFFFD700)
                        WeatherIcon.PARTLY_CLOUDY -> Color(0xFFFFE135)
                        WeatherIcon.CLOUDY -> Color(0xFFE0E0E0)
                        WeatherIcon.RAINY -> Color(0xFF87CEEB)
                    },
                    shape = RoundedCornerShape(4.dp)
                )
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(11.dp, 16.dp)
                    .background(
                        Color(android.graphics.Color.parseColor(WeatherConstants.PRECIPITATION_COLOR)),
                        RoundedCornerShape(2.dp)
                    )
            )
            
            Text(
                text = "${forecast.precipitationChance}%",
                fontSize = 12.sp,
                color = Color(android.graphics.Color.parseColor(WeatherConstants.PRECIPITATION_COLOR)),
                textAlign = TextAlign.Center
            )
        }
    }
}