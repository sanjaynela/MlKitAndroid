package com.example.mlkitandoird.data.repository

import android.graphics.Bitmap
import com.example.mlkitandoird.data.model.MLKitResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import javax.inject.Inject
import javax.inject.Singleton

interface MLKitRepository {
    suspend fun recognizeText(bitmap: Bitmap): List<MLKitResult.TextRecognitionResult>
    suspend fun detectObjects(bitmap: Bitmap): List<MLKitResult.ObjectDetectionResult>
    suspend fun scanBarcodes(bitmap: Bitmap): List<MLKitResult.BarcodeResult>
    suspend fun detectFaces(bitmap: Bitmap): List<MLKitResult.FaceDetectionResult>
    suspend fun labelImage(bitmap: Bitmap): List<MLKitResult.ImageLabelingResult>
}

@Singleton
class MLKitRepositoryImpl @Inject constructor() : MLKitRepository {
    
    override suspend fun recognizeText(bitmap: Bitmap): List<MLKitResult.TextRecognitionResult> {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val results = visionText.textBlocks.map { block ->
                        MLKitResult.TextRecognitionResult(
                            text = block.text,
                            confidence = 1.0f, // ML Kit doesn't provide confidence for text recognition
                            boundingBox = block.boundingBox
                        )
                    }
                    continuation.resume(results)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
    
    override suspend fun detectObjects(bitmap: Bitmap): List<MLKitResult.ObjectDetectionResult> {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val options = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .build()
            
            val objectDetector = ObjectDetection.getClient(options)
            
            objectDetector.process(image)
                .addOnSuccessListener { detectedObjects ->
                    val results = detectedObjects.map { obj ->
                        MLKitResult.ObjectDetectionResult(
                            objectName = obj.labels.firstOrNull()?.text ?: "Unknown",
                            confidence = obj.labels.firstOrNull()?.confidence ?: 0f,
                            boundingBox = obj.boundingBox,
                            trackingId = obj.trackingId
                        )
                    }
                    continuation.resume(results)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
    
    override suspend fun scanBarcodes(bitmap: Bitmap): List<MLKitResult.BarcodeResult> {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val scanner = BarcodeScanning.getClient()
            
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val results = barcodes.map { barcode ->
                        MLKitResult.BarcodeResult(
                            rawValue = barcode.rawValue ?: "",
                            format = barcode.format,
                            boundingBox = barcode.boundingBox
                        )
                    }
                    continuation.resume(results)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
    
    override suspend fun detectFaces(bitmap: Bitmap): List<MLKitResult.FaceDetectionResult> {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
            
            val faceDetector = FaceDetection.getClient(options)
            
            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    val results = faces.map { face ->
                        val landmarks = face.allLandmarks.map { landmark ->
                            MLKitResult.FaceLandmark(
                                type = landmark.landmarkType,
                                position = landmark.position
                            )
                        }
                        
                        MLKitResult.FaceDetectionResult(
                            boundingBox = face.boundingBox,
                            landmarks = landmarks,
                            trackingId = face.trackingId
                        )
                    }
                    continuation.resume(results)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
    
    override suspend fun labelImage(bitmap: Bitmap): List<MLKitResult.ImageLabelingResult> {
        return suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    val results = labels.map { label ->
                        MLKitResult.ImageLabelingResult(
                            label = label.text,
                            confidence = label.confidence
                        )
                    }
                    continuation.resume(results)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
}
