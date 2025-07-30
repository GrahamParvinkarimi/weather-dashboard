package com.gp.weather_dashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gp.weather_dashboard.di.sharedModule
import com.gp.weather_dashboard.di.platformModule
import com.gp.weather_dashboard.ui.WeatherApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize Koin
        startKoin {
            androidContext(this@MainActivity)
            modules(sharedModule, platformModule)
        }

        setContent {
            WeatherApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    WeatherApp()
}