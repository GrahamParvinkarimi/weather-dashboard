import SwiftUI
import Shared

struct WeatherView: View {
    @ObservedObject var viewModel: ObservableWeatherViewModel
    
    var body: some View {
        ZStack {
            // Background color matching Figma
            Color(red: 0.96, green: 0.9, blue: 0.64)
                .ignoresSafeArea()
            
            if viewModel.uiState.isLoading {
                ProgressView()
                    .scaleEffect(1.5)
                    .progressViewStyle(CircularProgressViewStyle(tint: Color(red: 0.54, green: 0.27, blue: 0.07)))
            } else if let errorMessage = viewModel.uiState.errorMessage, !viewModel.uiState.isLocationPermissionGranted {
                PermissionRequestView {
                    viewModel.requestLocationPermission()
                }
            } else if let errorMessage = viewModel.uiState.errorMessage {
                ErrorView(message: errorMessage) {
                    viewModel.clearError()
                } onRetry: {
                    viewModel.refreshWeather()
                }
            } else if let weatherData = viewModel.uiState.weatherData {
                VStack(spacing: 0) {
                    Spacer(minLength: 80)
                    
                    // Today text
                    Text("Today")
                        .font(.title2)
                        .fontWeight(.medium)
                        .foregroundColor(.black)
                    
                    Spacer(minLength: 40)
                    
                    // Main weather display
                    MainWeatherDisplayView(
                        temperature: Int(weatherData.temperature.rounded()),
                        cityCode: weatherData.cityName
                    )
                    
                    Spacer()
                    
                    // Hourly forecast
                    if !viewModel.uiState.hourlyForecasts.isEmpty {
                        HourlyForecastView(forecasts: viewModel.uiState.hourlyForecasts)
                            .padding(.vertical, 24)
                    }
                    
                    // City navigation
                    CityNavigationView(cityName: weatherData.cityName)
                        .padding(.bottom, 40)
                }
                .padding(.horizontal, 24)
            }
            
            // Refresh button
            VStack {
                HStack {
                    Spacer()
                    Button(action: {
                        viewModel.refreshWeather()
                    }) {
                        Image(systemName: "arrow.clockwise")
                            .font(.title2)
                            .foregroundColor(.black)
                            .frame(width: 44, height: 44)
                            .background(Color.white.opacity(0.8))
                            .clipShape(Circle())
                    }
                    .padding(.trailing, 16)
                    .padding(.top, 16)
                }
                Spacer()
            }
        }
        .onAppear {
            if !viewModel.uiState.isLocationPermissionGranted {
                viewModel.loadWeather()
            }
        }
    }
}

struct MainWeatherDisplayView: View {
    let temperature: Int
    let cityCode: String
    
    var body: some View {
        VStack(spacing: 24) {
            // Large circular background with temperature
            ZStack {
                // Main yellow circle
                Circle()
                    .fill(Color(red: 1.0, green: 0.84, blue: 0.31))
                    .frame(width: 280, height: 280)
                
                // Small accent circle
                Circle()
                    .fill(Color(red: 1.0, green: 0.88, blue: 0.51))
                    .frame(width: 48, height: 48)
                    .offset(x: 100, y: -100)
                
                // Temperature text
                Text("\(temperature)°")
                    .font(.system(size: 96, weight: .bold))
                    .foregroundColor(.black)
            }
            
            // Location with pin icon
            HStack(spacing: 8) {
                Circle()
                    .fill(Color.black)
                    .frame(width: 16, height: 16)
                
                Text(cityCode)
                    .font(.system(size: 48, weight: .light))
                    .foregroundColor(.black)
                    .tracking(8)
            }
        }
    }
}

struct HourlyForecastView: View {
    let forecasts: [HourlyForecast]
    
    var body: some View {
        VStack(spacing: 16) {
            // Weather chart line (simplified)
            ZStack {
                Path { path in
                    let points = Array(0..<min(7, forecasts.count))
                    for (index, _) in points.enumerated() {
                        let x = CGFloat(index) * (UIScreen.main.bounds.width - 48) / 6
                        let y: CGFloat = 30 // Simplified horizontal line
                        if index == 0 {
                            path.move(to: CGPoint(x: x, y: y))
                        } else {
                            path.addLine(to: CGPoint(x: x, y: y))
                        }
                    }
                }
                .stroke(Color.black, lineWidth: 2)
                
                // Points on the line
                HStack {
                    ForEach(0..<min(7, forecasts.count), id: \.self) { index in
                        Circle()
                            .fill(Color.black)
                            .frame(width: 8, height: 8)
                        if index < min(6, forecasts.count - 1) {
                            Spacer()
                        }
                    }
                }
                .padding(.horizontal, 4)
            }
            .frame(height: 60)
            
            // Time and weather info
            HStack {
                ForEach(Array(forecasts.prefix(7).enumerated()), id: \.offset) { index, forecast in
                    HourlyForecastItemView(forecast: forecast)
                    if index < min(6, forecasts.count - 1) {
                        Spacer()
                    }
                }
            }
        }
    }
}

struct HourlyForecastItemView: View {
    let forecast: HourlyForecast
    
    var body: some View {
        VStack(spacing: 8) {
            Text(hourString(from: forecast.dateTime))
                .font(.system(size: 12))
                .foregroundColor(.black)
            
            // Weather icon placeholder
            Circle()
                .fill(weatherColor(for: forecast.description))
                .frame(width: 24, height: 24)
            
            Text("\(Int(forecast.probabilityOfPrecipitation * 100))%")
                .font(.system(size: 10))
                .foregroundColor(.black)
        }
        .frame(width: 50)
    }
    
    private func hourString(from timestamp: Int64) -> String {
        let date = Date(timeIntervalSince1970: TimeInterval(timestamp))
        let formatter = DateFormatter()
        formatter.dateFormat = "h a"
        return formatter.string(from: date)
    }
    
    private func weatherColor(for description: String) -> Color {
        if description.lowercased().contains("rain") {
            return Color(red: 0.39, green: 0.71, blue: 0.96)
        } else if description.lowercased().contains("cloud") {
            return Color(red: 0.56, green: 0.64, blue: 0.68)
        } else {
            return Color(red: 1.0, green: 0.84, blue: 0.31)
        }
    }
}

struct CityNavigationView: View {
    let cityName: String
    
    var body: some View {
        HStack {
            Image(systemName: "chevron.left")
                .font(.title2)
                .foregroundColor(.black)
            
            Spacer()
            
            Text(cityName)
                .font(.system(size: 18, weight: .medium))
                .foregroundColor(.black)
            
            Spacer()
            
            Image(systemName: "chevron.right")
                .font(.title2)
                .foregroundColor(.black)
        }
    }
}

struct PermissionRequestView: View {
    let onRequestPermission: () -> Void
    
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "location.circle")
                .font(.system(size: 48))
                .foregroundColor(.black)
            
            Text("Location Permission Required")
                .font(.title2)
                .fontWeight(.bold)
                .multilineTextAlignment(.center)
            
            Text("This app needs location permission to show weather for your current location.")
                .font(.body)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
            
            Button("Grant Permission") {
                onRequestPermission()
            }
            .padding()
            .background(Color.blue)
            .foregroundColor(.white)
            .cornerRadius(8)
        }
        .padding()
        .background(Color.white)
        .cornerRadius(12)
        .padding(.horizontal, 32)
    }
}

struct ErrorView: View {
    let message: String
    let onDismiss: () -> Void
    let onRetry: () -> Void
    
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "exclamationmark.triangle")
                .font(.system(size: 48))
                .foregroundColor(.red)
            
            Text("Error")
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(.red)
            
            Text(message)
                .font(.body)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
            
            HStack {
                Button("Dismiss") {
                    onDismiss()
                }
                .padding()
                .background(Color.gray.opacity(0.3))
                .foregroundColor(.black)
                .cornerRadius(8)
                
                Button("Retry") {
                    onRetry()
                }
                .padding()
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(8)
            }
        }
        .padding()
        .background(Color.white)
        .cornerRadius(12)
        .padding(.horizontal, 32)
    }
}

struct WeatherView_Previews: PreviewProvider {
    static var previews: some View {
        WeatherView(viewModel: ObservableWeatherViewModel())
    }
}