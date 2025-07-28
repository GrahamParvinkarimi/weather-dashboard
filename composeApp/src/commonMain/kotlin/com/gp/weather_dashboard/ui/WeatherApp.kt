package com.gp.weather_dashboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gp.weather_dashboard.data.model.Location
import com.gp.weather_dashboard.data.model.WeatherData
import com.gp.weather_dashboard.domain.repository.WeatherRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(
    weatherRepository: WeatherRepository = koinInject()
) {
    var weatherData by remember { mutableStateOf<WeatherData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Location>>(emptyList()) }
    
    val scope = rememberCoroutineScope()
    
    // Sample location for demonstration
    val sampleLocation = Location(
        id = "40.7128_-74.0060",
        name = "New York",
        country = "US",
        state = "NY",
        latitude = 40.7128,
        longitude = -74.0060
    )
    
    LaunchedEffect(Unit) {
        loadWeather(sampleLocation, weatherRepository) { data, error ->
            weatherData = data
            errorMessage = error
            isLoading = false
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Weather Dashboard",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kotlin Multiplatform Demo",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Search Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Search Locations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Enter city name") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                val result = weatherRepository.searchLocations(searchQuery)
                                result.fold(
                                    onSuccess = { locations ->
                                        searchResults = locations
                                        errorMessage = null
                                    },
                                    onFailure = { error ->
                                        errorMessage = "Search failed: ${error.message}"
                                        searchResults = emptyList()
                                    }
                                )
                                isLoading = false
                            }
                        },
                        enabled = searchQuery.isNotBlank() && !isLoading
                    ) {
                        Text("Search")
                    }
                }
                
                if (searchResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(searchResults) { location ->
                            LocationItem(
                                location = location,
                                onClick = {
                                    isLoading = true
                                    loadWeather(location, weatherRepository) { data, error ->
                                        weatherData = data
                                        errorMessage = error
                                        isLoading = false
                                    }
                                    searchResults = emptyList()
                                    searchQuery = ""
                                }
                            )
                        }
                    }
                }
            }
        }
        
        // Weather Display
        if (isLoading) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        
        errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        weatherData?.let { data ->
            WeatherCard(weatherData = data)
        }
    }
}

@Composable
fun LocationItem(
    location: Location,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${location.state?.let { "$it, " } ?: ""}${location.country}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun WeatherCard(weatherData: WeatherData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Location
            Text(
                text = weatherData.location.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            // Current weather
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${weatherData.current.temperature.toInt()}°C",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = weatherData.current.weatherCondition.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Feels like ${weatherData.current.feelsLike.toInt()}°C",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Humidity: ${weatherData.current.humidity}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            // Additional details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetail(
                    label = "Wind",
                    value = "${weatherData.current.windSpeed} m/s"
                )
                WeatherDetail(
                    label = "Pressure",
                    value = "${weatherData.current.pressure.toInt()} hPa"
                )
                WeatherDetail(
                    label = "Visibility",
                    value = "${(weatherData.current.visibility / 1000).toInt()} km"
                )
            }
        }
    }
}

@Composable
fun WeatherDetail(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun loadWeather(
    location: Location,
    repository: WeatherRepository,
    onResult: (WeatherData?, String?) -> Unit
) {
    kotlinx.coroutines.GlobalScope.launch {
        val result = repository.getCurrentWeather(location)
        result.fold(
            onSuccess = { data ->
                onResult(data, null)
            },
            onFailure = { error ->
                onResult(null, "Failed to load weather: ${error.message}")
            }
        )
    }
}