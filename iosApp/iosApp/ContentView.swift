import SwiftUI
import Shared

struct HourlyWeather {
    let time: String
    let temperature: Int
    let precipitationChance: Int
    let isRainy: Bool
}

struct ContentView: View {
    let hourlyData: [HourlyWeather] = [
        HourlyWeather(time: "9 AM", temperature: 28, precipitationChance: 6, isRainy: false),
        HourlyWeather(time: "10 AM", temperature: 29, precipitationChance: 7, isRainy: false),
        HourlyWeather(time: "11 AM", temperature: 30, precipitationChance: 8, isRainy: true),
        HourlyWeather(time: "12 PM", temperature: 31, precipitationChance: 12, isRainy: false),
        HourlyWeather(time: "1 PM", temperature: 32, precipitationChance: 20, isRainy: true),
        HourlyWeather(time: "2 PM", temperature: 31, precipitationChance: 20, isRainy: true),
        HourlyWeather(time: "3 PM", temperature: 30, precipitationChance: 20, isRainy: true),
        HourlyWeather(time: "4 PM", temperature: 29, precipitationChance: 20, isRainy: true),
        HourlyWeather(time: "5 PM", temperature: 28, precipitationChance: 20, isRainy: true),
        HourlyWeather(time: "6 PM", temperature: 27, precipitationChance: 20, isRainy: true),
        HourlyWeather(time: "7 PM", temperature: 26, precipitationChance: 20, isRainy: true)
    ]
    
    var body: some View {
        ZStack {
            RadialGradient(
                colors: [
                    Color(red: 0.97, green: 0.75, blue: 0.16), // #F8BE28
                    Color(red: 0.94, green: 0.93, blue: 0.78)  // #F0ECC6
                ],
                center: .center,
                startRadius: 100,
                endRadius: 800
            )
            .ignoresSafeArea()
            
            VStack(spacing: 0) {
                Spacer().frame(height: 44)
                
                // Today text
                Text("Today")
                    .font(.system(size: 16))
                    .foregroundColor(.black)
                    .padding(.top, 24)
                
                Spacer().frame(height: 16)
                
                // Main temperature display
                ZStack {
                    Circle()
                        .fill(
                            RadialGradient(
                                colors: [
                                    Color(red: 0.97, green: 0.75, blue: 0.16, opacity: 0.8),
                                    Color(red: 0.97, green: 0.75, blue: 0.16, opacity: 0.4),
                                    Color.clear
                                ],
                                center: .center,
                                startRadius: 50,
                                endRadius: 300
                            )
                        )
                        .frame(width: 332, height: 332)
                    
                    VStack(alignment: .center, spacing: 0) {
                        HStack(alignment: .top, spacing: 0) {
                            Text("28")
                                .font(.system(size: 200, weight: .light))
                                .foregroundColor(.black)
                                .lineLimit(1)
                            
                            Text("°")
                                .font(.system(size: 140, weight: .thin))
                                .foregroundColor(.black)
                                .offset(y: -20)
                        }
                        
                        HStack {
                            Image(systemName: "location.fill")
                                .font(.system(size: 12))
                                .foregroundColor(.black)
                        }
                        .offset(y: -40)
                        
                        Text("JKT")
                            .font(.system(size: 178, weight: .thin))
                            .foregroundColor(.black)
                            .offset(y: -80)
                    }
                }
                
                Spacer().frame(height: 24)
                
                // Hourly forecast
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 20) {
                        ForEach(0..<hourlyData.count, id: \.self) { index in
                            HourlyWeatherView(weather: hourlyData[index])
                        }
                    }
                    .padding(.horizontal, 24)
                }
                
                Spacer()
                
                // Bottom navigation
                HStack {
                    Button(action: {}) {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 20))
                            .foregroundColor(.black)
                    }
                    
                    Spacer()
                    
                    Text("Jakarta")
                        .font(.system(size: 14, weight: .light))
                        .foregroundColor(.black)
                    
                    Spacer()
                    
                    Button(action: {}) {
                        Image(systemName: "chevron.right")
                            .font(.system(size: 20))
                            .foregroundColor(.black)
                    }
                }
                .padding(.horizontal, 24)
                .padding(.vertical, 16)
                
                // Home indicator
                RoundedRectangle(cornerRadius: 100)
                    .fill(Color(red: 0.09, green: 0.09, blue: 0.09)) // #181818
                    .frame(width: 135, height: 5)
                    .padding(.bottom, 8)
                
                Spacer().frame(height: 8)
            }
        }
    }
}

struct HourlyWeatherView: View {
    let weather: HourlyWeather
    
    var body: some View {
        VStack(spacing: 8) {
            Text(weather.time)
                .font(.system(size: 12))
                .foregroundColor(.black)
            
            // Weather icon placeholder
            Circle()
                .fill(weather.isRainy ? Color.gray.opacity(0.6) : Color(red: 0.97, green: 0.75, blue: 0.16))
                .frame(width: 22, height: 22)
            
            // Precipitation chance
            HStack(alignment: .center, spacing: 2) {
                Rectangle()
                    .fill(Color(red: 0.97, green: 0.75, blue: 0.16))
                    .frame(width: 11, height: 16)
                
                Text("\(weather.precipitationChance)%")
                    .font(.system(size: 12))
                    .foregroundColor(Color(red: 0.97, green: 0.75, blue: 0.16))
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
