# CLAUDE.md - Weather Dashboard Development Instructions

## Project Context
You are building a **Weather Dashboard** application using **Kotlin Multiplatform Mobile (KMM)** that will be deployed to both **Android** and **iOS**. This is a PoC demonstration project showcasing cross-platform shared business logic, API integration, data visualization, and platform-specific UI implementations.

## Key Development Priorities

### 1. Architecture First
- Set up proper KMM project structure with `commonMain`, `androidMain`, and `iosMain` source sets
- Implement clean architecture with Repository pattern for data management
- Use dependency injection (prefer Koin for KMM compatibility)
- Separate concerns: Network → Repository → ViewModel → UI

### 2. Shared Business Logic Focus
This is the core value proposition of the demo. Ensure these are implemented in `commonMain`:
- Weather data models and API response parsing
- Repository implementations for data fetching and caching
- Business logic for weather calculations and data transformations
- Location management interfaces
- Database entities and DAOs using Room KMP

### 3. API Integration Standards
- **Primary API**: OpenWeatherMap (free tier: https://openweathermap.org/api)
- **HTTP Client**: Use Ktor for cross-platform networking
- **JSON Parsing**: Kotlinx.serialization throughout
- **Error Handling**: Implement comprehensive Result wrapper pattern
- **Rate Limiting**: Respect API quotas (1000 calls/day on free tier)

### 4. Platform-Specific Implementation Guidelines

#### Android Implementation
- **UI Framework**: Jetpack Compose (modern, declarative)
- **Charts**: Use MPAndroidChart or Compose-compatible charting library
- **Location**: Google Play Services Location API
- **Theme**: Material Design 3 with proper theming
- **Target**: API 34, minimum API 24

#### iOS Implementation
- **UI Integration**: SwiftUI with shared ViewModels
- **Charts**: Swift Charts (iOS 16+) or compatible library
- **Location**: Core Location framework
- **Theme**: Native iOS design patterns
- **Target**: iOS 15+

## Critical Implementation Details

### Database Setup (Room KMP)
```kotlin
// In commonMain - Use these exact patterns
@Entity(tableName = "weather_cache")
data class WeatherCache(
    @PrimaryKey val locationId: String,
    val weatherData: String, // JSON serialized WeatherData
    val lastUpdated: Long,
    val expiresAt: Long
)

@Database(
    entities = [WeatherCache::class, FavoriteLocation::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
```

### API Integration Pattern
```kotlin
// Follow this pattern for all API calls
class WeatherRepositoryImpl(
    private val apiClient: WeatherApiClient,
    private val database: WeatherDatabase
) : WeatherRepository {
    
    override suspend fun getCurrentWeather(location: Location): Result<WeatherData> {
        return try {
            // Check cache first
            val cached = database.weatherDao().getCachedWeather(location.id)
            if (cached != null && !cached.isExpired()) {
                return Result.success(cached.toWeatherData())
            }
            
            // Fetch from API
            val response = apiClient.getCurrentWeather(location.latitude, location.longitude)
            val weatherData = response.toWeatherData()
            
            // Cache the result
            database.weatherDao().insertWeatherCache(weatherData.toCache())
            
            Result.success(weatherData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Data Visualization Requirements
- **Temperature Chart**: 24-hour and 7-day line charts
- **Precipitation**: Bar chart with probability percentages
- **Wind**: Compass-style direction indicator with speed
- **Atmospheric**: Multi-line chart for humidity/pressure
- **UV Index**: Color-coded display with safety levels

## Development Workflow

### Phase 1: Foundation (Start Here)
1. **Project Setup**
  - Create KMM project with proper gradle configuration
  - Add all required dependencies (Ktor, Room, Kotlinx.serialization)
  - Set up basic project structure and build configuration

2. **Shared Models**
  - Implement core data classes in `commonMain`
  - Add Room entities and DAOs
  - Create API response models with serialization

3. **API Integration**
  - Implement WeatherApiClient with Ktor
  - Add proper error handling and response parsing
  - Test API connectivity with actual OpenWeatherMap endpoints

### Phase 2: Core Functionality
1. **Repository Layer**
  - Implement WeatherRepository with caching logic
  - Add location search functionality
  - Handle offline scenarios gracefully

2. **Database Integration**
  - Set up Room database with proper migrations
  - Implement caching strategy (cache for 10 minutes)
  - Add favorite locations persistence

### Phase 3: Platform UIs
1. **Android Implementation**
  - Create Compose UI with proper Material Design 3
  - Implement location permission handling
  - Add chart visualizations using MPAndroidChart

2. **iOS Implementation**
  - Create SwiftUI views that consume shared ViewModels
  - Implement Core Location integration
  - Add chart visualizations using Swift Charts

### Phase 4: Polish & Testing
1. **Feature Completion**
  - Add pull-to-refresh functionality
  - Implement dark/light theme support
  - Add loading states and error handling UI

2. **Testing & Optimization**
  - Add unit tests for shared business logic
  - Performance optimization for chart rendering
  - Memory leak detection and battery optimization

## OpenWeatherMap API Setup

### Required Endpoints
```
// Current Weather
GET https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API_KEY}&units=metric

// 5-Day Forecast  
GET https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API_KEY}&units=metric

// Geocoding (Location Search)
GET https://api.openweathermap.org/geo/1.0/direct?q={city}&limit=5&appid={API_KEY}
```

### API Key Management
- Store API key in `local.properties` for development
- Use BuildConfig for release builds
- Never commit API keys to version control

## Testing Strategy

### Shared Logic Tests (commonTest)
```kotlin
class WeatherRepositoryTest {
    @Test
    fun `test weather data caching works correctly`() = runTest {
        // Test caching behavior
    }
    
    @Test
    fun `test API error handling`() = runTest {
        // Test network failure scenarios
    }
}
```

### Platform-Specific Tests
- Android: Compose UI tests and instrumented tests
- iOS: XCTest for UI components and integration

## Common Pitfalls to Avoid

### 1. KMM-Specific Issues
- Don't use Android-specific classes in commonMain
- Avoid iOS-specific Swift code in shared modules
- Handle timezone differences properly using kotlinx-datetime

### 2. API Integration
- Always implement proper rate limiting
- Handle API quota exceeded gracefully
- Don't make API calls on every location change

### 3. Performance
- Limit chart data points (max 48 hours for hourly data)
- Use proper image caching for weather icons
- Implement proper database cleanup for old cached data

### 4. UI/UX
- Always show loading states during API calls
- Handle location permission denied scenarios
- Provide meaningful error messages to users

## Success Criteria

### Functional Requirements ✅
- [ ] App loads and displays current weather for user's location
- [ ] Location search works and saves favorite locations
- [ ] Charts display temperature, precipitation, and wind data
- [ ] App works offline with cached data
- [ ] Both Android and iOS versions function identically

### Technical Requirements ✅
- [ ] Shared business logic works across platforms
- [ ] API integration handles errors gracefully
- [ ] Room database caching works correctly
- [ ] Charts render smoothly with good performance
- [ ] App follows platform design guidelines

### Demo Requirements ✅
- [ ] App can be built and deployed to both platforms
- [ ] Demonstrates clear value of KMM approach
- [ ] Shows sophisticated data visualization capabilities
- [ ] Handles real-world scenarios (no internet, GPS disabled)

## Final Notes

- **Focus on demonstrating KMM value**: The shared business logic should be substantial and clearly beneficial
- **Prioritize working software**: Better to have fewer features that work perfectly than many half-working features
- **Document architectural decisions**: This helps with the PoC presentation
- **Keep it deployable**: Ensure the app can actually be installed and run on real devices

Remember: This is a PoC to demonstrate Kotlin Multiplatform capabilities. The shared business logic, clean architecture, and cross-platform functionality are more important than pixel-perfect UI polish.