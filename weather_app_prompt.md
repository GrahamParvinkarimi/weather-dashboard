# Weather App Implementation Prompt for Claude Code

```
Create a lean, single-screen weather application using Kotlin Multiplatform with the following specifications:

## Core Requirements
- **Platform Targets**: Android and iOS using Kotlin Multiplatform Mobile (KMM)
- **UI Framework**: Jetpack Compose for Android, SwiftUI for iOS (platform-specific UIs)
- **Architecture**: MVVM pattern with Repository pattern for data layer
- **Dependency Injection**: Use Koin for DI container (shared), native DI for iOS UI layer
- **Networking**: Ktor client for HTTP requests
- **Location Services**: Platform-specific location access with shared business logic

## Technical Specifications
- **Minimum Versions**: Android API 24+, iOS 14+
- **Build System**: Gradle with Kotlin DSL
- **Coroutines**: Use for asynchronous operations and state management
- **Serialization**: kotlinx.serialization for JSON parsing
- **State Management**: Jetpack Compose state for Android, SwiftUI @StateObject/@ObservableObject for iOS

## API Integration
- **Weather Service**: OpenWeatherMap API (https://openweathermap.org/api)
- **API Key**: Implement secure API key storage (BuildConfig for Android, Info.plist for iOS)
- **Endpoints**: Use Current Weather Data API
- **Temperature Unit**: Display in Fahrenheit
- **Location**: Fetch user's current location and display city abbreviation instead of "JKT"

## UI Implementation
- **Design Reference**: Use the current Figma selection as design reference for the weather display screen
- **Platform-Specific UI**: Jetpack Compose for Android following Material 3, SwiftUI for iOS following Human Interface Guidelines
- **Responsive Design**: Ensure proper layout for different screen sizes and orientations on both platforms
- **Loading States**: Implement platform-appropriate loading indicators
- **Error Handling**: Display user-friendly error messages using platform-specific UI patterns
- **Design Systems**: Material 3 for Android, latest iOS native design patterns for SwiftUI

## Project Structure
```
shared/
  ├── src/
  │   ├── commonMain/kotlin/
  │   │   ├── data/
  │   │   │   ├── repository/
  │   │   │   ├── network/
  │   │   │   └── model/
  │   │   ├── domain/
  │   │   │   ├── usecase/
  │   │   │   └── repository/
  │   │   ├── presentation/
  │   │   │   └── viewmodel/
  │   │   └── di/
  │   ├── androidMain/kotlin/
  │   └── iosMain/kotlin/
androidApp/
  ├── src/main/kotlin/
  │   └── ui/ (Jetpack Compose)
iosApp/
  ├── iosApp/
  │   ├── ContentView.swift (SwiftUI)
  │   ├── WeatherView.swift
  │   └── ViewModels/
```

## Key Features to Implement
1. **Location Permission Handling**: Request and handle location permissions on both platforms
2. **Weather Data Fetching**: Get current weather based on user location
3. **Temperature Display**: Show temperature in Fahrenheit with proper formatting
4. **Location Display**: Show city abbreviation derived from current location
5. **Refresh Functionality**: Pull-to-refresh or refresh button
6. **Offline Handling**: Graceful degradation when network is unavailable
7. **Loading States**: Proper loading indicators during API calls

## Dependencies to Include
- Kotlin Multiplatform Mobile
- Jetpack Compose for Android UI
- SwiftUI for iOS UI (native iOS development)
- Ktor client with platform engines
- kotlinx.serialization
- kotlinx.coroutines
- Koin for dependency injection (shared layer)
- Platform-specific location services
- iOS: Combine framework for reactive programming integration

## Quality Requirements
- **Error Handling**: Comprehensive error handling for network, location, and parsing errors
- **Performance**: Efficient API calls with proper caching if needed
- **User Experience**: Smooth animations and transitions
- **Code Quality**: Clean, maintainable code with proper separation of concerns
- **Testing**: Unit tests for business logic and view models

## Platform-Specific Implementation
- **Android**: Use Jetpack Compose with Android-specific location services and Material 3 design
- **iOS**: Use SwiftUI with CoreLocation integration and iOS Human Interface Guidelines
- **Shared Logic**: All business logic, data models, networking, and use cases in shared KMM module
- **Platform UIs**: Native UI implementations that consume shared ViewModels
- **Permissions**: Handle location permissions appropriately for each platform
- **iOS Integration**: Create Swift wrappers/bridges for shared Kotlin ViewModels to work with SwiftUI ObservableObject pattern

Please implement this weather application step by step, ensuring each component is properly tested and integrated before moving to the next. Start with the project setup and basic architecture, then implement the shared data layer, followed by the shared presentation logic (ViewModels), and finally the platform-specific UI implementations (Jetpack Compose for Android, SwiftUI for iOS). Ensure proper integration between the shared Kotlin ViewModels and SwiftUI through appropriate bridging mechanisms.
```