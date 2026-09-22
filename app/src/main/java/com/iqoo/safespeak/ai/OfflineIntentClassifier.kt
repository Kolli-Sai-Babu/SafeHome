package com.iqoo.safespeak.ai

import java.util.Locale
import kotlin.math.max

/**
 * On-Device Offline AI Emergency Intent Classifier.
 * Zero network dependencies. Processes speech utterances locally on device.
 */
object OfflineIntentClassifier {

    private const val EMERGENCY_THRESHOLD = 0.55f

    // Weighted local emergency intent vocabulary & phrase vectors
    private val EMERGENCY_PHRASES = mapOf(
        "safespeak i need help" to 1.00f,
        "safespeak help me" to 0.98f,
        "safespeak emergency" to 0.98f,
        "activate safespeak" to 0.98f,
        "i need help" to 0.95f,
        "i am in danger" to 0.96f,
        "i'm in danger" to 0.96f,
        "please help me" to 0.95f,
        "help me" to 0.90f,
        "save me" to 0.92f,
        "emergency help" to 0.95f,
        "someone is following me" to 0.94f,
        "call emergency" to 0.92f,
        "i'm hurt" to 0.90f,
        "sos emergency" to 0.98f,
        "distress alert" to 0.92f,
        "danger help" to 0.94f
    )

    private val HIGH_WEIGHT_KEYWORDS = setOf(
        "safespeak", "emergency", "danger", "sos", "distress", "attack", "threat"
    )

    private val MEDIUM_WEIGHT_KEYWORDS = setOf(
        "help", "save", "hurt", "following", "scared", "trapped", "accident", "police"
    )

    /**
     * Classifies spoken utterance offline.
     */
    fun classifyUtterance(rawText: String): IntentClassificationResult {
        val startTime = System.currentTimeMillis()
        val normalized = rawText.lowercase(Locale.ROOT).trim()

        if (normalized.isEmpty()) {
            return IntentClassificationResult(
                isEmergencyIntent = false,
                confidenceScore = 0.0f,
                matchedIntentName = "NO_INPUT",
                matchedKeywords = emptyList(),
                rawUtterance = rawText,
                processingTimeMs = System.currentTimeMillis() - startTime
            )
        }

        var maxScore = 0.0f
        var bestMatchedPhrase = "NONE"
        val matchedKeywords = mutableListOf<String>()

        // 1. Direct & Fuzzy Phrase Matching
        for ((phrase, baseScore) in EMERGENCY_PHRASES) {
            if (normalized.contains(phrase)) {
                if (baseScore > maxScore) {
                    maxScore = baseScore
                    bestMatchedPhrase = phrase
                }
            } else {
                // Fuzzy similarity (Levenshtein distance calculation)
                val similarity = calculateFuzzySimilarity(normalized, phrase)
                if (similarity >= 0.70f) {
                    val adjustedScore = similarity * baseScore
                    if (adjustedScore > maxScore) {
                        maxScore = adjustedScore
                        bestMatchedPhrase = phrase
                    }
                }
            }
        }

        // 2. Keyword Vector Weighting
        val tokens = normalized.split(Regex("\\s+"))
        var keywordScore = 0.0f
        for (token in tokens) {
            val cleanToken = token.replace(Regex("[^a-z0-9]"), "")
            if (HIGH_WEIGHT_KEYWORDS.contains(cleanToken)) {
                keywordScore += 0.45f
                matchedKeywords.add(cleanToken)
            } else if (MEDIUM_WEIGHT_KEYWORDS.contains(cleanToken)) {
                keywordScore += 0.25f
                matchedKeywords.add(cleanToken)
            }
        }

        // Combine phrase score and keyword vector score
        val finalConfidence = minOf(1.0f, max(maxScore, minOf(0.99f, maxScore * 0.5f + keywordScore * 0.5f)))
        val isEmergency = finalConfidence >= EMERGENCY_THRESHOLD

        val intentName = if (isEmergency) {
            if (matchedKeywords.contains("safespeak")) "EMERGENCY_TRIGGER_DIRECT" else "EMERGENCY_INTENT_DETECTED"
        } else {
            "NON_EMERGENCY"
        }

        return IntentClassificationResult(
            isEmergencyIntent = isEmergency,
            confidenceScore = (finalConfidence * 100).toInt() / 100.0f,
            matchedIntentName = intentName,
            matchedKeywords = matchedKeywords.distinct(),
            rawUtterance = rawText,
            processingTimeMs = maxOf(1L, System.currentTimeMillis() - startTime)
        )
    }

    private fun calculateFuzzySimilarity(s1: String, s2: String): Float {
        val maxLen = max(s1.length, s2.length)
        if (maxLen == 0) return 1.0f
        val distance = levenshteinDistance(s1, s2)
        return 1.0f - (distance.toFloat() / maxLen.toFloat())
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j
        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[s1.length][s2.length]
    }
}
