package com.iqoo.safespeak.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val fullName: String = "Alex Vance",
    val contactName: String = "Sarah Vance (Sister)",
    val contactPhone: String = "+1 (555) 019-2834",
    val bloodGroup: String = "O+",
    val medicalInfo: String = "Asthma, Allergic to Penicillin",
    val customMessage: String = "EMERGENCY: I need immediate assistance! My real-time location attached.",
    val discreetMode: Boolean = false,
    val voiceFeedbackEnabled: Boolean = true
)
