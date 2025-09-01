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
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScanningScreen(
    navController: NavController,
    viewModel: MLKitViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    var showCamera by remember { mutableStateOf(false) }
    var capturedImageFile by remember { mutableStateOf<File?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Barcode Scanning") },
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
                onClick = { showCamera = true }
            ) {
                Icon(Icons.Filled.Camera, contentDescription = "Capture")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Debug section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Debug Controls",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.testStateUpdate() }
                        ) {
                            Text("Test State Update")
                        }
                        Button(
                            onClick = { viewModel.logCurrentState() }
                        ) {
                            Text("Log State")
                        }
                        Button(
                            onClick = { viewModel.clearResults() }
                        ) {
                            Text("Clear Results")
                        }
                        Button(
                            onClick = { 
                                // Test ML Kit with a simple test
                                Log.d("BarcodeScreen", "Testing ML Kit processing...")
                                viewModel.scanBarcodes(android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888))
                            }
                        ) {
                            Text("Test ML Kit")
                        }
                        Button(
                            onClick = { 
                                // Test ML Kit library initialization
                                Log.d("BarcodeScreen", "Testing ML Kit library initialization...")
                                try {
                                    val scanner = com.google.mlkit.vision.barcode.BarcodeScanning.getClient()
                                    Log.d("BarcodeScreen", "ML Kit library initialized successfully")
                                } catch (e: Exception) {
                                    Log.e("BarcodeScreen", "ML Kit library initialization failed", e)
                                }
                            }
                        ) {
                            Text("Test ML Kit Init")
                        }
                    }
                }
            }
            
            if (showCamera) {
                CameraPermissionHandler(
                    onPermissionGranted = {
                        CameraPreview(
                            onImageCaptured = { file ->
                                capturedImageFile = file
                                showCamera = false
                                
                                Log.d("BarcodeScreen", "Image captured: ${file.absolutePath}")
                                Log.d("BarcodeScreen", "File size: ${file.length()} bytes")
                                
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                if (bitmap != null) {
                                    Log.d("BarcodeScreen", "Bitmap decoded successfully: ${bitmap.width}x${bitmap.height}")
                                    viewModel.scanBarcodes(bitmap)
                                } else {
                                    Log.e("BarcodeScreen", "Failed to decode bitmap")
                                }
                            },
                            onError = { exception ->
                                Log.e("BarcodeScreen", "Camera error", exception)
                                showCamera = false
                            },
                            onClose = { showCamera = false }
                        )
                    }
                )
            } else {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.error != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Error,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.error!!,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                } else if (uiState.barcodeResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Scanned Barcodes",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                        
                        items(uiState.barcodeResults) { result ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = result.rawValue,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Format: ${getBarcodeFormatName(result.format)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.QrCodeScanner,
                                contentDescription = "Barcode Scanning",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No barcodes scanned yet",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap the camera button to scan barcodes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getBarcodeFormatName(format: Int): String {
    return when (format) {
        0x0001 -> "CODE_128"
        0x0002 -> "CODE_39"
        0x0004 -> "CODE_93"
        0x0008 -> "CODABAR"
        0x0010 -> "DATA_MATRIX"
        0x0020 -> "EAN_13"
        0x0040 -> "EAN_8"
        0x0080 -> "ITF"
        0x0100 -> "QR_CODE"
        0x0200 -> "UPC_A"
        0x0400 -> "UPC_E"
        0x0800 -> "PDF417"
        0x1000 -> "AZTEC"
        else -> "UNKNOWN"
    }
}
