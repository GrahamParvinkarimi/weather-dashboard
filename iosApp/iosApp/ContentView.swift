import SwiftUI
import Shared

struct ContentView: View {
    @StateObject private var viewModel = WeatherViewModelBridge()
    
    var body: some View {
        WeatherView(viewModel: viewModel)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
