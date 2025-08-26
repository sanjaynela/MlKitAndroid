package com.example.mlkitandoird.ui.viewmodel

import android.graphics.Bitmap
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
    
    private val _uiState = MutableStateFlow(MLKitUiState())
    val uiState: StateFlow<MLKitUiState> = _uiState.asStateFlow()
    
    fun recognizeText(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = mlKitRepository.recognizeText(bitmap)
                _uiState.value = _uiState.value.copy(
                    textResults = results,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Text recognition failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun detectObjects(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = mlKitRepository.detectObjects(bitmap)
                _uiState.value = _uiState.value.copy(
                    objectResults = results,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Object detection failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun scanBarcodes(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = mlKitRepository.scanBarcodes(bitmap)
                _uiState.value = _uiState.value.copy(
                    barcodeResults = results,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Barcode scanning failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun detectFaces(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = mlKitRepository.detectFaces(bitmap)
                _uiState.value = _uiState.value.copy(
                    faceResults = results,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Face detection failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun labelImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = mlKitRepository.labelImage(bitmap)
                _uiState.value = _uiState.value.copy(
                    labelResults = results,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Image labeling failed",
                    isLoading = false
                )
            }
        }
    }
    
    fun clearResults() {
        _uiState.value = MLKitUiState()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
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
