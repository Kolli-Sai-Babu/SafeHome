package com.iqoo.safespeak.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyDao {

    // Emergency Events
    @Query("SELECT * FROM emergency_events ORDER BY timeMillis DESC")
    fun getAllEvents(): Flow<List<EmergencyEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EmergencyEvent): Long

    @Query("UPDATE emergency_events SET status = :status WHERE id = :id")
    suspend fun updateEventStatus(id: Long, status: String)

    @Query("DELETE FROM emergency_events")
    suspend fun clearAllEvents()

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileDirect(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)
}
