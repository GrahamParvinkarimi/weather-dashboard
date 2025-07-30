import Foundation
import Shared

print("Testing KMM Weather Dashboard...")

// Test basic functionality
let greeting = Greeting()
print("Greeting from shared code: \(greeting.greet())")

// Test platform-specific code
let platform = Platform_iosKt.getPlatform()
print("Platform: \(platform.name)")

print("Shared framework is working correctly!")