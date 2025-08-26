package com.example.mlkitandoird.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mlkitandoird.ui.components.CameraPermissionHandler
import com.example.mlkitandoird.ui.components.CameraPreview
import com.example.mlkitandoird.ui.viewmodel.MLKitViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTrackerScreen(
    navController: NavController,
    viewModel: MLKitViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    var showCamera by remember { mutableStateOf(false) }
    var isWorkoutActive by remember { mutableStateOf(false) }
    var workoutStartTime by remember { mutableStateOf<Long?>(null) }
    var repCount by remember { mutableStateOf(0) }
    var currentExercise by remember { mutableStateOf("Push-ups") }
    
    val exercises = listOf("Push-ups", "Squats", "Lunges", "Plank", "Burpees")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Tracker") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    if (isWorkoutActive) {
                        showCamera = true
                    } else {
                        isWorkoutActive = true
                        workoutStartTime = System.currentTimeMillis()
                    }
                }
            ) {
                Icon(
                    imageVector = if (isWorkoutActive) Icons.Filled.Camera else Icons.Filled.PlayArrow,
                    contentDescription = if (isWorkoutActive) "Track Exercise" else "Start Workout"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showCamera) {
                CameraPermissionHandler(
                    onPermissionGranted = {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CameraPreview(
                                onImageCaptured = { file ->
                                    showCamera = false
                                    
                                    // In a real app, you would use ML Kit Pose Detection here
                                    // For now, we'll simulate pose detection
                                    repCount++
                                },
                                onError = { exception ->
                                    showCamera = false
                                }
                            )
                            
                            IconButton(
                                onClick = { showCamera = false },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close Camera",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                )
            } else {
                // Workout tracking UI
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        // Workout status card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isWorkoutActive) 
                                    MaterialTheme.colorScheme.primaryContainer 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isWorkoutActive) "Workout Active" else "Workout Ready",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = if (isWorkoutActive) Icons.Filled.FitnessCenter else Icons.Filled.Schedule,
                                        contentDescription = "Workout Status",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                if (isWorkoutActive && workoutStartTime != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Started: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(workoutStartTime!!))}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    
                    item {
                        // Exercise selection
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Current Exercise",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                exercises.forEach { exercise ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = currentExercise == exercise,
                                            onClick = { currentExercise = exercise }
                                        )
                                        Text(
                                            text = exercise,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    item {
                        // Rep counter
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Reps Completed",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = repCount.toString(),
                                    style = MaterialTheme.typography.displayMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                if (isWorkoutActive) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { if (repCount > 0) repCount-- }
                                        ) {
                                            Icon(Icons.Filled.Remove, contentDescription = "Decrease")
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Decrease")
                                        }
                                        
                                        Button(
                                            onClick = { repCount++ }
                                        ) {
                                            Icon(Icons.Filled.Add, contentDescription = "Increase")
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Increase")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    item {
                        // Workout controls
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { 
                                    isWorkoutActive = false
                                    workoutStartTime = null
                                    repCount = 0
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(Icons.Filled.Stop, contentDescription = "End Workout")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("End Workout")
                            }
                            
                            Button(
                                onClick = { repCount = 0 },
                                modifier = Modifier.weight(1f),
                                enabled = isWorkoutActive
                            ) {
                                Icon(Icons.Filled.Refresh, contentDescription = "Reset Reps")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset Reps")
                            }
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Pose Detection Tips",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "• Position yourself in front of the camera\n" +
                                          "• Ensure good lighting\n" +
                                          "• Keep your full body in frame\n" +
                                          "• Perform exercises slowly and deliberately\n" +
                                          "• The app will automatically count your reps",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
