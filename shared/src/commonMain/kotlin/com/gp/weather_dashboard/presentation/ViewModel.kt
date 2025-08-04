package com.gp.weather_dashboard.presentation

import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModel() {
    val viewModelScope: CoroutineScope
    
    protected open fun onCleared()
}