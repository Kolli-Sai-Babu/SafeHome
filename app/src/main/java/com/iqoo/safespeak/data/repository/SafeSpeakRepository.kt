package com.iqoo.safespeak.data.repository

import com.iqoo.safespeak.data.local.EmergencyDao
import com.iqoo.safespeak.data.local.EmergencyEvent
import com.iqoo.safespeak.data.local.UserProfile
import kotlinx.coroutines.flow.Flow

class SafeSpeakRepository(private val dao: EmergencyDao) {

    val allEvents: Flow<List<EmergencyEvent>> = dao.getAllEvents()
    val userProfile: Flow<UserProfile?> = dao.getUserProfile()

    suspend fun logEmergencyEvent(event: EmergencyEvent): Long {
        return dao.insertEvent(event)
    }

    suspend fun updateEventStatus(id: Long, status: String) {
        dao.updateEventStatus(id, status)
    }

    suspend fun clearHistory() {
        dao.clearAllEvents()
    }

    suspend fun saveProfile(profile: UserProfile) {
        dao.saveUserProfile(profile)
    }

    suspend fun getProfileDirect(): UserProfile {
        return dao.getUserProfileDirect() ?: UserProfile()
    }
}
