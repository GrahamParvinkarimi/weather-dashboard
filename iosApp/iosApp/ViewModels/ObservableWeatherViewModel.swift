import Foundation
import Shared
import Combine

class ObservableWeatherViewModel: ObservableObject {
    private let viewModel: WeatherViewModel
    private var cancellables = Set<AnyCancellable>()
    
    @Published var uiState: WeatherUiState = WeatherUiState(
        isLoading: false,
        weatherData: nil,
        hourlyForecasts: [],
        errorMessage: nil,
        isLocationPermissionGranted: false,
        isRefreshing: false
    )
    
    init() {
        // Create ViewModel using DI helper
        self.viewModel = DiHelper().getWeatherViewModel()
        
        // Start observing state changes
        startObservingState()
    }
    
    private func startObservingState() {
        // Simple polling approach with proper state comparison
        Timer.publish(every: 0.1, on: .main, in: .common)
            .autoconnect()
            .sink { [weak self] _ in
                self?.checkForStateUpdates()
            }
            .store(in: &cancellables)
    }
    
    private func checkForStateUpdates() {
        let currentState = viewModel.uiState.value as! WeatherUiState
        
        // Only update if state actually changed to prevent unnecessary re-renders
        if !statesAreEqual(currentState, uiState) {
            DispatchQueue.main.async { [weak self] in
                self?.uiState = currentState
            }
        }
    }
    
    private func statesAreEqual(_ state1: WeatherUiState, _ state2: WeatherUiState) -> Bool {
        return state1.isLoading == state2.isLoading &&
               state1.isRefreshing == state2.isRefreshing &&
               state1.isLocationPermissionGranted == state2.isLocationPermissionGranted &&
               state1.errorMessage == state2.errorMessage &&
               state1.weatherData?.temperature == state2.weatherData?.temperature &&
               state1.hourlyForecasts.count == state2.hourlyForecasts.count
    }
    
    deinit {
        // Properly cleanup resources
        cancellables.removeAll()
        viewModel.clear()
    }
    
    func loadWeather() {
        viewModel.handleEvent(event: WeatherUiEvent.LoadWeather.shared)
    }
    
    func refreshWeather() {
        viewModel.handleEvent(event: WeatherUiEvent.RefreshWeather.shared)
    }
    
    func requestLocationPermission() {
        viewModel.handleEvent(event: WeatherUiEvent.RequestLocationPermission.shared)
    }
    
    func clearError() {
        viewModel.handleEvent(event: WeatherUiEvent.ClearError.shared)
    }
}