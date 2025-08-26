package com.example.mlkitandoird.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mlkitandoird.ui.screens.HomeScreen
import com.example.mlkitandoird.ui.screens.TextRecognitionScreen
import com.example.mlkitandoird.ui.screens.ObjectDetectionScreen
import com.example.mlkitandoird.ui.screens.BarcodeScanningScreen
import com.example.mlkitandoird.ui.screens.FaceDetectionScreen
import com.example.mlkitandoird.ui.screens.ImageLabelingScreen
import com.example.mlkitandoird.ui.screens.WorkoutTrackerScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.TextRecognition.route) {
            TextRecognitionScreen(navController = navController)
        }
        
        composable(Screen.ObjectDetection.route) {
            ObjectDetectionScreen(navController = navController)
        }
        
        composable(Screen.BarcodeScanning.route) {
            BarcodeScanningScreen(navController = navController)
        }
        
        composable(Screen.FaceDetection.route) {
            FaceDetectionScreen(navController = navController)
        }
        
        composable(Screen.ImageLabeling.route) {
            ImageLabelingScreen(navController = navController)
        }
        
        composable(Screen.WorkoutTracker.route) {
            WorkoutTrackerScreen(navController = navController)
        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "ML Kit Vision")
    object TextRecognition : Screen("text_recognition", "Text Recognition")
    object ObjectDetection : Screen("object_detection", "Object Detection")
    object BarcodeScanning : Screen("barcode_scanning", "Barcode Scanning")
    object FaceDetection : Screen("face_detection", "Face Detection")
    object ImageLabeling : Screen("image_labeling", "Image Labeling")
    object WorkoutTracker : Screen("workout_tracker", "Workout Tracker")
}
