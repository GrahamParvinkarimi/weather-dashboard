import Foundation
import Shared
import Combine

@ObservableObject
class WeatherViewModelBridge: ObservableObject {
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
        // Initialize Koin
        KoinInitializerKt.initKoinIos()
        
        // Get ViewModel from Koin
        self.viewModel = KoinHelper.shared.getWeatherViewModel()
        
        // Observe state changes
        observeStateChanges()
    }
    
    private func observeStateChanges() {
        // Create a timer to periodically check for state changes
        Timer.publish(every: 0.1, on: .main, in: .common)
            .autoconnect()
            .sink { [weak self] _ in
                self?.updateState()
            }
            .store(in: &cancellables)
    }
    
    private func updateState() {
        let currentState = viewModel.uiState.value
        
        DispatchQueue.main.async { [weak self] in
            self?.uiState = currentState
        }
    }
    
    func loadWeather() {
        viewModel.handleEvent(event: WeatherUiEvent.LoadWeather())
    }
    
    func refreshWeather() {
        viewModel.handleEvent(event: WeatherUiEvent.RefreshWeather())
    }
    
    func requestLocationPermission() {
        viewModel.handleEvent(event: WeatherUiEvent.RequestLocationPermission())
    }
    
    func clearError() {
        viewModel.handleEvent(event: WeatherUiEvent.ClearError())
    }
}

class KoinHelper {
    static let shared = KoinHelper()
    
    private init() {}
    
    func getWeatherViewModel() -> WeatherViewModel {
        return KoinKt.koin().get()
    }
}