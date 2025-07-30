import SwiftUI
import Shared

struct WeatherView: View {
    private let viewModel = WeatherViewModel()
    @State private var weatherData: WeatherDisplayData?
    
    var body: some View {
        GeometryReader { geometry in
            ZStack {
                // Background
                Color(hex: weatherData?.backgroundGradient.primaryColor ?? "#f0ecc6")
                    .ignoresSafeArea()
                
                VStack(spacing: 0) {
                    // Header
                    Text("Today")
                        .font(.system(size: 16, weight: .regular))
                        .foregroundColor(Color(hex: "#000000"))
                        .padding(.top, 24)
                    
                    Spacer().frame(height: 40)
                    
                    // Main temperature display
                    ZStack {
                        Circle()
                            .fill(Color.yellow.opacity(0.3))
                            .frame(width: 332, height: 332)
                        
                        VStack(spacing: 0) {
                            HStack(alignment: .top, spacing: 0) {
                                Text("\(weatherData?.temperature ?? 28)")
                                    .font(.system(size: 200, weight: .light))
                                    .foregroundColor(Color(hex: "#000000"))
                                    .lineLimit(1)
                                
                                Text(weatherData?.temperatureUnit ?? "°")
                                    .font(.system(size: 140, weight: .thin))
                                    .foregroundColor(Color(hex: "#000000"))
                                    .offset(y: 8)
                            }
                            
                            Image(systemName: "location.fill")
                                .font(.system(size: 24))
                                .foregroundColor(Color(hex: "#000000"))
                                .offset(y: 60)
                            
                            Text(weatherData?.cityCode ?? "JKT")
                                .font(.system(size: 178, weight: .thin))
                                .foregroundColor(Color(hex: "#000000"))
                                .offset(y: 140)
                        }
                    }
                    
                    Spacer().frame(height: 60)
                    
                    // Hourly forecast
                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 20) {
                            ForEach(Array((weatherData?.hourlyForecast ?? []).enumerated()), id: \.offset) { index, forecast in
                                HourlyForecastView(forecast: forecast)
                            }
                        }
                        .padding(.horizontal, 24)
                    }
                    
                    Spacer()
                    
                    // Bottom navigation
                    HStack {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 24))
                            .foregroundColor(Color(hex: "#000000"))
                        
                        Spacer()
                        
                        Text(weatherData?.cityName ?? "Jakarta")
                            .font(.system(size: 14, weight: .light))
                            .foregroundColor(Color(hex: "#000000"))
                        
                        Spacer()
                        
                        Image(systemName: "chevron.right")
                            .font(.system(size: 24))
                            .foregroundColor(Color(hex: "#000000"))
                    }
                    .padding(.horizontal, 24)
                    .padding(.bottom, 40)
                }
            }
        }
        .onAppear {
            weatherData = viewModel.getWeatherDisplayData()
        }
    }
}

struct HourlyForecastView: View {
    let forecast: HourlyForecast
    
    var body: some View {
        VStack(spacing: 8) {
            Text(forecast.time)
                .font(.system(size: 12, weight: .regular))
                .foregroundColor(Color(hex: "#000000"))
            
            Rectangle()
                .fill(weatherIconColor(forecast.weatherIcon))
                .frame(width: 29, height: 22)
                .cornerRadius(4)
            
            HStack(spacing: 2) {
                Rectangle()
                    .fill(Color(hex: "#f8be28"))
                    .frame(width: 11, height: 16)
                    .cornerRadius(2)
                
                Text("\(forecast.precipitationChance)%")
                    .font(.system(size: 12, weight: .regular))
                    .foregroundColor(Color(hex: "#f8be28"))
            }
        }
    }
    
    private func weatherIconColor(_ icon: WeatherIcon) -> Color {
        switch icon {
        case .sunny:
            return Color.yellow
        case .partlyCloudy:
            return Color.yellow.opacity(0.8)
        case .cloudy:
            return Color.gray.opacity(0.6)
        case .rainy:
            return Color.blue.opacity(0.4)
        }
    }
}

extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (1, 1, 1, 0)
        }

        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue:  Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}

struct WeatherView_Previews: PreviewProvider {
    static var previews: some View {
        WeatherView()
    }
}