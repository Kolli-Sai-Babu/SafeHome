package com.iqoo.safespeak.ai

data class IntentClassificationResult(
    val isEmergencyIntent: Boolean,
    val confidenceScore: Float,
    val matchedIntentName: String,
    val matchedKeywords: List<String>,
    val rawUtterance: String,
    val processingTimeMs: Long,
    val modelSignature: String = "SafeSpeak On-Device Intent Engine v1.0 (Offline)"
)
