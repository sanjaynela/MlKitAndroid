package com.example.mlkitandoird.data.model

import android.graphics.Rect

sealed class MLKitResult {
    data class TextRecognitionResult(
        val text: String,
        val confidence: Float,
        val boundingBox: Rect? = null
    ) : MLKitResult()
    
    data class ObjectDetectionResult(
        val objectName: String,
        val confidence: Float,
        val boundingBox: Rect,
        val trackingId: Int? = null
    ) : MLKitResult()
    
    data class BarcodeResult(
        val rawValue: String,
        val format: Int,
        val boundingBox: Rect? = null
    ) : MLKitResult()
    
    data class FaceDetectionResult(
        val boundingBox: Rect,
        val landmarks: List<FaceLandmark> = emptyList(),
        val trackingId: Int? = null
    ) : MLKitResult()
    
    data class ImageLabelingResult(
        val label: String,
        val confidence: Float
    ) : MLKitResult()
    
    data class FaceLandmark(
        val type: Int,
        val position: android.graphics.PointF
    )
}
