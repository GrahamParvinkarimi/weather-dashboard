package com.gp.weather_dashboard.presentation

import com.gp.weather_dashboard.data.location.LocationService
import com.gp.weather_dashboard.data.mappers.celsiusToFahrenheit
import com.gp.weather_dashboard.data.mappers.toCityAbbreviation
import com.gp.weather_dashboard.data.models.ForecastResult
import com.gp.weather_dashboard.data.models.LocationResult
import com.gp.weather_dashboard.data.models.WeatherResult
import com.gp.weather_dashboard.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationService: LocationService
): ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        checkLocationPermissionAndLoadWeather()
    }

    fun handleEvent(event: WeatherUiEvent) {
        when (event) {
            is WeatherUiEvent.LoadWeather -> loadWeather()
            is WeatherUiEvent.RefreshWeather -> refreshWeather()
            is WeatherUiEvent.RequestLocationPermission -> requestLocationPermission()
            is WeatherUiEvent.ClearError -> clearError()
        }
    }

    private fun checkLocationPermissionAndLoadWeather() {
        _uiState.value = _uiState.value.copy(
            isLocationPermissionGranted = locationService.isLocationPermissionGranted()
        )
        
        if (_uiState.value.isLocationPermissionGranted) {
            loadWeather()
        }
    }

    private fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            when (val locationResult = locationService.getCurrentLocation()) {
                is LocationResult.Success -> {
                    loadWeatherData(locationResult.location.latitude, locationResult.location.longitude)
                }
                is LocationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = locationResult.message
                    )
                }
                is LocationResult.PermissionDenied -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Location permission is required to get weather data",
                        isLocationPermissionGranted = false
                    )
                }
                is LocationResult.ServiceDisabled -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Please enable location services"
                    )
                }
            }
        }
    }

    private suspend fun loadWeatherData(latitude: Double, longitude: Double) {
        // Load current weather
        when (val weatherResult = weatherRepository.getCurrentWeather(latitude, longitude)) {
            is WeatherResult.Success -> {
                val weatherData = weatherResult.data.copy(
                    temperature = weatherResult.data.temperature.celsiusToFahrenheit(),
                    feelsLike = weatherResult.data.feelsLike.celsiusToFahrenheit(),
                    cityName = weatherResult.data.cityName.toCityAbbreviation()
                )
                
                _uiState.value = _uiState.value.copy(
                    weatherData = weatherData,
                    isLoading = false
                )
                
                // Load hourly forecast
                loadHourlyForecast(latitude, longitude)
            }
            is WeatherResult.Error -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = weatherResult.message
                )
            }
        }
    }

    private suspend fun loadHourlyForecast(latitude: Double, longitude: Double) {
        when (val forecastResult = weatherRepository.getHourlyForecast(latitude, longitude)) {
            is ForecastResult.Success -> {
                val forecasts = forecastResult.forecasts.map { forecast ->
                    forecast.copy(temperature = forecast.temperature.celsiusToFahrenheit())
                }
                
                _uiState.value = _uiState.value.copy(
                    hourlyForecasts = forecasts
                )
            }
            is ForecastResult.Error -> {
                // Don't show error for forecast failure, current weather is more important
            }
        }
    }

    private fun refreshWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            
            when (val locationResult = locationService.getCurrentLocation()) {
                is LocationResult.Success -> {
                    loadWeatherData(locationResult.location.latitude, locationResult.location.longitude)
                }
                is LocationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = locationResult.message
                    )
                }
                is LocationResult.PermissionDenied -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = "Location permission is required",
                        isLocationPermissionGranted = false
                    )
                }
                is LocationResult.ServiceDisabled -> {
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = "Please enable location services"
                    )
                }
            }
            
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    private fun requestLocationPermission() {
        viewModelScope.launch {
            val granted = locationService.requestLocationPermission()
            _uiState.value = _uiState.value.copy(isLocationPermissionGranted = granted)
            
            if (granted) {
                loadWeather()
            }
        }
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}