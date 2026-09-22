package com.iqoo.safespeak.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_events")
data class EmergencyEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestampFormatted: String,
    val timeMillis: Long = System.currentTimeMillis(),
    val triggerType: String,
    val recognizedText: String,
    val confidenceScore: Float,
    val locationAvailable: Boolean,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val accuracyMeters: Float? = null,
    val status: String, // "ACTIVATED", "CONFIRMED", "CANCELLED_FALSE_ALARM"
    val isDemo: Boolean = true
)
