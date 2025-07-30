# Weather App API Setup

## OpenWeatherMap API Key Setup

To use this weather application, you need to obtain an API key from OpenWeatherMap and configure it in the app.

### Steps:

1. **Get API Key:**
   - Visit [OpenWeatherMap](https://openweathermap.org/api)
   - Sign up for a free account
   - Navigate to "My API Keys" 
   - Copy your API key

2. **Configure API Key:**
   - Open `shared/src/commonMain/kotlin/com/gp/weather_dashboard/data/network/WeatherApi.kt`
   - Replace `YOUR_API_KEY_HERE` with your actual API key:
   ```kotlin
   private const val API_KEY = "your_actual_api_key_here"
   ```

3. **Alternative Configuration (Recommended for production):**
   - Add API key to `local.properties` file:
   ```
   WEATHER_API_KEY=your_actual_api_key_here
   ```
   
   - Update build configuration to read from properties:
   ```kotlin
   // In shared/build.gradle.kts
   android {
       buildConfigField("String", "WEATHER_API_KEY", "\"${project.findProperty("WEATHER_API_KEY") ?: ""}\"")
   }
   ```

### API Endpoints Used:
- Current Weather: `https://api.openweathermap.org/data/2.5/weather`
- 5-Day Forecast: `https://api.openweathermap.org/data/2.5/forecast`

### Rate Limits:
- Free tier: 60 calls/minute, 1000 calls/day
- The app is designed to minimize API calls by caching data and only refreshing when needed.

### Temperature Units:
- API returns temperature in Kelvin by default
- App converts to Fahrenheit for display as specified in requirements
- Conversion is handled in `WeatherMappers.kt`