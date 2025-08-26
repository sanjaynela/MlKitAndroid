package com.example.mlkitandoird.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mlkitandoird.ui.navigation.Screen
import com.example.mlkitandoird.ui.viewmodel.MLKitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MLKitViewModel = hiltViewModel()
) {
    val features = listOf(
        Feature(
            title = "Text Recognition",
            description = "Extract text from images and documents",
            icon = Icons.Filled.TextFields,
            route = Screen.TextRecognition.route
        ),
        Feature(
            title = "Object Detection",
            description = "Detect and classify objects in images",
            icon = Icons.Filled.Search,
            route = Screen.ObjectDetection.route
        ),
        Feature(
            title = "Barcode Scanning",
            description = "Scan QR codes and barcodes",
            icon = Icons.Filled.QrCodeScanner,
            route = Screen.BarcodeScanning.route
        ),
        Feature(
            title = "Face Detection",
            description = "Detect faces and facial landmarks",
            icon = Icons.Filled.Face,
            route = Screen.FaceDetection.route
        ),
        Feature(
            title = "Image Labeling",
            description = "Classify objects in images",
            icon = Icons.Filled.Label,
            route = Screen.ImageLabeling.route
        ),
        Feature(
            title = "Workout Tracker",
            description = "Track workouts with pose detection",
            icon = Icons.Filled.FitnessCenter,
            route = Screen.WorkoutTracker.route
        )
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ML Kit Vision") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(features) { feature ->
                FeatureCard(
                    feature = feature,
                    onClick = { navController.navigate(feature.route) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeatureCard(
    feature: Feature,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = feature.title,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = feature.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private data class Feature(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String
)
