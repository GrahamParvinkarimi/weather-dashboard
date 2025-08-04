# Weather Dashboard

Built as a prototype for demonstrating the use of Claude Code and Figma MCP for Kotlin Multiplatform. A lean, single-screen weather application built with Kotlin Multiplatform targeting Android and iOS platforms.

## Features

- **Current Weather Display**: Shows real-time weather data based on user location
- **Location-Based**: Automatically fetches weather for current location with city abbreviation
- **Temperature in Fahrenheit**: Displays temperature in Fahrenheit with proper formatting
- **Cross-Platform**: Shared business logic with native UI for each platform
- **Error Handling**: Comprehensive error handling for network and location services

## Architecture

This project follows **MVVM (Model-View-ViewModel)** pattern with **Repository pattern** for data layer:

- **Shared Business Logic**: Weather data fetching, location services, and ViewModels
- **Platform-Specific UI**: Jetpack Compose for Android, SwiftUI for iOS
- **Dependency Injection**: Koin for shared module DI
- **Networking**: Ktor client for API communication
- **Location Services**: Platform-specific location access with shared business logic

## Project Structure

### `/shared` - Shared Business Logic
Contains the core business logic shared between platforms:

- **`/src/commonMain/kotlin`** - Common code for all platforms
  - `data/` - Data layer (models, network, repository)
    - `models/` - Weather data models (WeatherData, Location, etc.)
    - `network/` - Weather API client using Ktor
    - `repository/` - Weather repository implementation
    - `mappers/` - Data transformation utilities
  - `presentation/` - ViewModels and UI state management
    - `WeatherViewModel.kt` - Main weather screen ViewModel
    - `WeatherUiState.kt` - UI state definitions
  - `di/` - Dependency injection setup with Koin

- **`/src/androidMain/kotlin`** - Android-specific implementations
  - `data/location/` - Android location service
  - `data/network/` - Android HTTP client factory
  - `di/` - Android platform module
  - `presentation/` - Android ViewModel extensions

- **`/src/iosMain/kotlin`** - iOS-specific implementations
  - `data/location/` - iOS location service using CoreLocation
  - `data/network/` - iOS HTTP client factory
  - `di/` - iOS platform module and dependency injection helper
  - `presentation/` - iOS ViewModel bridging

### `/composeApp` - Android UI Layer
Contains Android-specific UI implementation using Jetpack Compose:

- **`/src/commonMain/kotlin`** - Compose UI components
- **`/src/androidMain/kotlin`** - Android-specific UI and Activity

### `/iosApp` - iOS Application
Contains iOS-specific SwiftUI implementation:

- **`ContentView.swift`** - Main SwiftUI view
- **`Views/WeatherView.swift`** - Weather display SwiftUI view
- **`ViewModels/ObservableWeatherViewModel.swift`** - SwiftUI-compatible ViewModel wrapper
- **`iOSApp.swift`** - iOS app entry point

## Technical Stack

- **Kotlin Multiplatform Mobile** - Cross-platform development
- **Jetpack Compose** - Android UI framework
- **SwiftUI** - iOS UI framework
- **Ktor Client** - HTTP networking
- **Koin** - Dependency injection
- **kotlinx.serialization** - JSON serialization
- **kotlinx.coroutines** - Asynchronous programming
- **OpenWeatherMap API** - Weather data source

## API Integration

The app integrates with **OpenWeatherMap API** for current weather data. See [API_SETUP.md](./API_SETUP.md) for setup instructions.

## Getting Started

### Prerequisites
- Android Studio with Kotlin Multiplatform plugin
- Xcode (for iOS development)
- OpenWeatherMap API key

### Setup
1. Clone the repository
2. Add your OpenWeatherMap API key (see API_SETUP.md)
3. Open project in Android Studio
4. For iOS: Open iosApp/iosApp.xcodeproj in Xcode

### Building
- **Android**: Run from Android Studio or use `./gradlew assembleDebug`
- **iOS**: Build and run from Xcode

## Learn More

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
