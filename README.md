# ML Kit Vision Android App

A comprehensive Android application demonstrating Google's ML Kit Vision APIs with Jetpack Compose and Hilt dependency injection. This app showcases various computer vision capabilities including text recognition, object detection, barcode scanning, face detection, image labeling, and workout tracking.

## Features

### 🔍 Text Recognition (OCR)
- Extract text from images and documents
- Real-time text detection using camera
- Support for multiple languages
- Confidence scoring for recognized text

### 🎯 Object Detection
- Detect and classify objects in images
- Multiple object detection with bounding boxes
- Object tracking with unique IDs
- Real-time object recognition

### 📱 Barcode Scanning
- Scan QR codes and various barcode formats
- Support for multiple barcode types (CODE_128, EAN_13, QR_CODE, etc.)
- Real-time barcode detection
- Format identification

### 👤 Face Detection
- Detect faces in images
- Facial landmark detection (eyes, nose, mouth, etc.)
- Face tracking with unique IDs
- Bounding box information

### 🏷️ Image Labeling
- Classify objects in images
- Confidence scoring for classifications
- Multiple label detection
- Real-time image analysis

### 💪 Workout Tracker
- Exercise tracking with pose detection
- Rep counting for various exercises
- Workout session management
- Exercise selection (Push-ups, Squats, Lunges, etc.)

## Technology Stack

- **UI Framework**: Jetpack Compose
- **Dependency Injection**: Hilt
- **Navigation**: Navigation Compose
- **ML Kit APIs**: Text Recognition, Object Detection, Barcode Scanning, Face Detection, Image Labeling
- **Camera**: CameraX
- **Permissions**: Accompanist Permissions
- **Image Loading**: Coil
- **Architecture**: MVVM with Repository pattern

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (API level 24)
- Kotlin 1.9.0+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/sanjaynela/MLKitAndroid.git
cd MLKitAndroid
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Build and run the application

### Permissions

The app requires the following permissions:
- Camera access for image capture
- Storage access for saving images (optional)

## Project Structure

```
app/src/main/java/com/example/mlkitandoird/
├── data/
│   ├── model/
│   │   └── MLKitResult.kt
│   └── repository/
│       └── MLKitRepository.kt
├── di/
│   └── AppModule.kt
├── ui/
│   ├── components/
│   │   ├── CameraComponent.kt
│   │   └── PermissionComponent.kt
│   ├── navigation/
│   │   └── NavGraph.kt
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── TextRecognitionScreen.kt
│   │   ├── ObjectDetectionScreen.kt
│   │   ├── BarcodeScanningScreen.kt
│   │   ├── FaceDetectionScreen.kt
│   │   ├── ImageLabelingScreen.kt
│   │   └── WorkoutTrackerScreen.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── viewmodel/
│       └── MLKitViewModel.kt
├── MainActivity.kt
└── MLKitApplication.kt
```

## Usage

### Getting Started
1. Launch the app
2. Grant camera permissions when prompted
3. Select a feature from the home screen
4. Use the camera to capture images for analysis

### Text Recognition
- Tap the camera button to capture an image
- The app will extract and display recognized text
- View confidence scores for each text block

### Object Detection
- Capture an image containing objects
- View detected objects with their classifications
- See confidence scores and tracking information

### Barcode Scanning
- Point the camera at a barcode or QR code
- View the scanned content and format information
- Supports various barcode types

### Face Detection
- Capture an image with faces
- View detected faces with landmark information
- See bounding box coordinates

### Image Labeling
- Capture an image for classification
- View detected labels with confidence scores
- Multiple labels can be detected per image

### Workout Tracker
- Select an exercise type
- Start a workout session
- Use camera for pose detection and rep counting
- Track workout progress and statistics

## ML Kit Integration

The app demonstrates the following ML Kit APIs:

### Text Recognition
```kotlin
val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
recognizer.process(image)
    .addOnSuccessListener { visionText ->
        // Process recognized text
    }
```

### Object Detection
```kotlin
val options = ObjectDetectorOptions.Builder()
    .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
    .enableMultipleObjects()
    .enableClassification()
    .build()
val objectDetector = ObjectDetection.getClient(options)
```

### Barcode Scanning
```kotlin
val scanner = BarcodeScanning.getClient()
scanner.process(image)
    .addOnSuccessListener { barcodes ->
        // Process scanned barcodes
    }
```

### Face Detection
```kotlin
val options = FaceDetectorOptions.Builder()
    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
    .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
    .build()
val faceDetector = FaceDetection.getClient(options)
```

### Image Labeling
```kotlin
val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
labeler.process(image)
    .addOnSuccessListener { labels ->
        // Process image labels
    }
```

## Hilt Integration

The app uses Hilt for dependency injection:

### Application Class
```kotlin
@HiltAndroidApp
class MLKitApplication : Application()
```

### Repository Injection
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindMLKitRepository(
        mlKitRepositoryImpl: MLKitRepositoryImpl
    ): MLKitRepository
}
```

### ViewModel Injection
```kotlin
@HiltViewModel
class MLKitViewModel @Inject constructor(
    private val mlKitRepository: MLKitRepository
) : ViewModel()
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Google ML Kit team for the excellent vision APIs
- Android team for Jetpack Compose
- The Android community for various libraries and tools

## ⚠️ Important Compatibility Notes

### Compose-Kotlin Compatibility Issues

This project has been configured to avoid common Compose-Kotlin compatibility issues. When working with Jetpack Compose, ensure you use compatible versions:

#### Version Compatibility Table
| Kotlin Version | Compose Compiler Version |
|----------------|-------------------------|
| 1.9.22         | 1.5.10                 |
| 1.9.21         | 1.5.8                  |
| 1.9.20         | 1.5.7                  |
| 1.9.10         | 1.5.4                  |
| 1.9.0          | 1.5.3                  |

#### Current Configuration
```kotlin
// build.gradle.kts
android {
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10" // Matches Kotlin 1.9.22
    }
}
```

**Common Error**: `Incompatible Compose Compiler version`
**Solution**: Always check the [official compatibility table](https://developer.android.com/jetpack/androidx/releases/compose-kotlin) before updating versions.

### Hilt/JavaPoet Compatibility Issues

This project uses Hilt 2.50 to avoid JavaPoet compatibility issues that can occur with newer versions.

#### Known Issues
- **Error**: `'java.lang.String com.squareup.javapoet.ClassName.canonicalName()'`
- **Cause**: Version conflicts between Hilt and JavaPoet libraries
- **Solution**: Use Hilt 2.50 instead of 2.52+

#### Current Hilt Configuration
```kotlin
// Project-level build.gradle.kts
plugins {
    id("com.google.dagger.hilt.android") version "2.50" apply false
}

// App-level build.gradle.kts
dependencies {
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-compiler:2.50")
}
```

#### Troubleshooting Steps
1. **Clean project**: `./gradlew clean`
2. **Downgrade Hilt** to version 2.50
3. **Use direct dependencies** instead of version catalogs
4. **Invalidate IDE caches** and restart

### API Level Compatibility

The project handles API level compatibility issues for modern Java APIs:

#### LocalDateTime Compatibility
**Problem**: `LocalDateTime.now()` requires API level 26, but minSdk is 24
**Solution**: Use `System.currentTimeMillis()` with `SimpleDateFormat`

```kotlin
// Instead of LocalDateTime.now()
val timestamp = System.currentTimeMillis()
val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
```

#### Required Imports
```kotlin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
```

### Build Configuration Best Practices

#### Version Catalog vs Direct Dependencies
- **Development**: Use direct dependencies for easier debugging
- **Production**: Consider version catalogs for maintainability
- **Troubleshooting**: Switch to direct dependencies when issues occur

#### Plugin Declaration Pattern
```kotlin
// Project-level build.gradle.kts
plugins {
    id("com.android.application") version "8.12.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}
```

### Quick Fix Templates

#### Compose Compiler Fix
```kotlin
android {
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10" // Adjust based on Kotlin version
    }
}
```

#### Hilt Setup
```kotlin
plugins {
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-compiler:2.50")
}
```

#### API Level Compatibility
```kotlin
// Instead of LocalDateTime.now()
val timestamp = System.currentTimeMillis()
val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(timestamp))
```

### Common Pitfalls to Avoid
- ❌ Don't mix different Kotlin versions across modules
- ❌ Don't use incompatible Compose BOM versions
- ❌ Don't skip the `kapt` plugin when using Hilt
- ❌ Don't use Java 8+ APIs without desugaring for minSdk < 26
- ❌ Don't use Hilt 2.52+ without testing JavaPoet compatibility

### IDE Configuration
- Enable Gradle build cache
- Configure proper JDK version (11 or 17)
- Set up proper Android SDK versions
- Configure lint rules appropriately

## Related Articles

- [Build Smarter Android Apps with Vision: ML Kit the Easy Way](https://medium.com/@sanjaynelagadde1992/build-smarter-android-apps-with-vision-ml-kit-the-easy-way-15a8e61e3c76)
- [Simplifying Dependency Injection in Android Jetpack Compose with Hilt](https://medium.com/mobile-app-development-publication/simplifying-dependency-injection-in-android-jetpack-compose-with-hilt-1b42f25cf358)
