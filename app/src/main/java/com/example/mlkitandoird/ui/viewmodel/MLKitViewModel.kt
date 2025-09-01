package com.example.mlkitandoird.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mlkitandoird.data.model.MLKitResult
import com.example.mlkitandoird.data.repository.MLKitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MLKitViewModel @Inject constructor(
    private val mlKitRepository: MLKitRepository
) : ViewModel() {
    
    companion object {
        private const val TAG = "MLKitViewModel"
    }
    
    private val _uiState = MutableStateFlow(MLKitUiState())
    val uiState: StateFlow<MLKitUiState> = _uiState.asStateFlow()
    
    fun recognizeText(bitmap: Bitmap) {
        Log.d(TAG, "Starting text recognition...")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                Log.d(TAG, "Calling repository for text recognition...")
                val results = mlKitRepository.recognizeText(bitmap)
                Log.d(TAG, "Text recognition completed with ${results.size} results")
                _uiState.value = _uiState.value.copy(
                    textResults = results,
                    isLoading = false
                )
                Log.d(TAG, "UI state updated for text recognition")
            } catch (e: Exception) {
                Log.e(TAG, "Text recognition failed", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Text recognition failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun detectObjects(bitmap: Bitmap) {
        Log.d(TAG, "Starting object detection...")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                Log.d(TAG, "Calling repository for object detection...")
                val results = mlKitRepository.detectObjects(bitmap)
                Log.d(TAG, "Object detection completed with ${results.size} results")
                _uiState.value = _uiState.value.copy(
                    objectResults = results,
                    isLoading = false
                )
                Log.d(TAG, "UI state updated for object detection")
            } catch (e: Exception) {
                Log.e(TAG, "Object detection failed", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Object detection failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun scanBarcodes(bitmap: Bitmap) {
        Log.d(TAG, "Starting barcode scanning...")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                Log.d(TAG, "Calling repository for barcode scanning...")
                val results = mlKitRepository.scanBarcodes(bitmap)
                Log.d(TAG, "Barcode scanning completed with ${results.size} results")
                _uiState.value = _uiState.value.copy(
                    barcodeResults = results,
                    isLoading = false
                )
                Log.d(TAG, "UI state updated for barcode scanning")
            } catch (e: Exception) {
                Log.e(TAG, "Barcode scanning failed", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Barcode scanning failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun detectFaces(bitmap: Bitmap) {
        Log.d(TAG, "Starting face detection...")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                Log.d(TAG, "Calling repository for face detection...")
                val results = mlKitRepository.detectFaces(bitmap)
                Log.d(TAG, "Face detection completed with ${results.size} results")
                _uiState.value = _uiState.value.copy(
                    faceResults = results,
                    isLoading = false
                )
                Log.d(TAG, "UI state updated for face detection")
            } catch (e: Exception) {
                Log.e(TAG, "Face detection failed", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Face detection failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun labelImage(bitmap: Bitmap) {
        Log.d(TAG, "Starting image labeling...")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                Log.d(TAG, "Calling repository for image labeling...")
                val results = mlKitRepository.labelImage(bitmap)
                Log.d(TAG, "Image labeling completed with ${results.size} results")
                _uiState.value = _uiState.value.copy(
                    labelResults = results,
                    isLoading = false
                )
                Log.d(TAG, "UI state updated for image labeling")
            } catch (e: Exception) {
                Log.e(TAG, "Image labeling failed", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Image labeling failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun clearResults() {
        Log.d(TAG, "Clearing all results")
        _uiState.value = MLKitUiState()
    }
    
    fun clearError() {
        Log.d(TAG, "Clearing error")
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    // Debug function to test state updates
    fun testStateUpdate() {
        Log.d(TAG, "Testing state update...")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                error = "Test error message",
                isLoading = false
            )
            Log.d(TAG, "Test state update completed")
        }
    }
    
    // Debug function to check current state
    fun logCurrentState() {
        Log.d(TAG, "Current UI state: $uiState")
    }
}

data class MLKitUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val textResults: List<MLKitResult.TextRecognitionResult> = emptyList(),
    val objectResults: List<MLKitResult.ObjectDetectionResult> = emptyList(),
    val barcodeResults: List<MLKitResult.BarcodeResult> = emptyList(),
    val faceResults: List<MLKitResult.FaceDetectionResult> = emptyList(),
    val labelResults: List<MLKitResult.ImageLabelingResult> = emptyList()
)
