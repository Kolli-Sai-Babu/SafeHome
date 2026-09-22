package com.iqoo.safespeak.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iqoo.safespeak.ai.IntentClassificationResult
import com.iqoo.safespeak.ai.OfflineIntentClassifier
import com.iqoo.safespeak.ai.VoiceTriggerListener
import com.iqoo.safespeak.data.local.EmergencyEvent
import com.iqoo.safespeak.data.local.UserProfile
import com.iqoo.safespeak.data.repository.SafeSpeakRepository
import com.iqoo.safespeak.services.HapticFeedbackManager
import com.iqoo.safespeak.services.OfflineLocationData
import com.iqoo.safespeak.services.OfflineLocationProvider
import com.iqoo.safespeak.services.VoiceFeedbackManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ScreenState {
    DASHBOARD,
    SAFE_MODE,
    PROFILE_EDIT,
    HISTORY_LOG,
    SETTINGS_PRIVACY
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as com.iqoo.safespeak.SafeSpeakApplication
    private val repository: SafeSpeakRepository = app.repository
    private val locationProvider = OfflineLocationProvider(application)
    private val haptics = HapticFeedbackManager(application)
    private val voiceFeedback = VoiceFeedbackManager(application)

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .map { it ?: UserProfile() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    val emergencyEvents: StateFlow<List<EmergencyEvent>> = repository.allEvents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentScreen = MutableStateFlow(ScreenState.DASHBOARD)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _lastClassification = MutableStateFlow<IntentClassificationResult?>(null)
    val lastClassification: StateFlow<IntentClassificationResult?> = _lastClassification.asStateFlow()

    private val _activeEmergency = MutableStateFlow<EmergencyEvent?>(null)
    val activeEmergency: StateFlow<EmergencyEvent?> = _activeEmergency.asStateFlow()

    private val _locationData = MutableStateFlow(OfflineLocationData(isAvailable = false))
    val locationData: StateFlow<OfflineLocationData> = _locationData.asStateFlow()

    private val _countdownSeconds = MutableStateFlow(10)
    val countdownSeconds: StateFlow<Int> = _countdownSeconds.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private var countdownJob: Job? = null
    private var voiceListener: VoiceTriggerListener? = null

    init {
        // Seed default profile if DB empty
        viewModelScope.launch {
            repository.saveProfile(UserProfile())
            refreshLocation()
        }
    }

    fun navigateTo(screen: ScreenState) {
        _currentScreen.value = screen
    }

    fun refreshLocation() {
        locationProvider.requestEmergencyLocation { loc ->
            _locationData.value = loc
        }
    }

    fun startListening() {
        if (_isListening.value) return
        _isListening.value = true
        _statusMessage.value = "Listening for emergency trigger phrase..."

        voiceListener = VoiceTriggerListener(
            context = getApplication(),
            onResult = { classification ->
                _isListening.value = false
                _lastClassification.value = classification
                if (classification.isEmergencyIntent) {
                    triggerEmergency(
                        triggerType = "Voice Trigger (Offline AI)",
                        text = classification.rawUtterance,
                        confidence = classification.confidenceScore,
                        isDemo = false
                    )
                } else {
                    _statusMessage.value = "Voice intent analyzed: Non-emergency utterance"
                }
            },
            onError = { errStr ->
                _isListening.value = false
                _statusMessage.value = errStr
            }
        )
        voiceListener?.startListening()
    }

    fun stopListening() {
        voiceListener?.stopListening()
        _isListening.value = false
        _statusMessage.value = "Offline voice listener paused"
    }

    /**
     * Simulates or triggers emergency intent (Used by Demo Mode bar & Test Voice Trigger button)
     */
    fun simulateUtterance(phrase: String, triggerLabel: String = "Demo Simulation") {
        val classification = OfflineIntentClassifier.classifyUtterance(phrase)
        _lastClassification.value = classification

        if (classification.isEmergencyIntent) {
            triggerEmergency(
                triggerType = triggerLabel,
                text = phrase,
                confidence = classification.confidenceScore,
                isDemo = true
            )
        } else {
            _statusMessage.value = "Utterance analyzed ($phrase): Below emergency threshold"
        }
    }

    fun triggerEmergency(
        triggerType: String,
        text: String,
        confidence: Float = 0.96f,
        isDemo: Boolean = true
    ) {
        refreshLocation()
        val currentLoc = _locationData.value
        val profile = userProfile.value

        val timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm:ss a", Locale.getDefault()).format(Date())

        val event = EmergencyEvent(
            timestampFormatted = timestamp,
            triggerType = triggerType,
            recognizedText = text,
            confidenceScore = confidence,
            locationAvailable = currentLoc.isAvailable,
            latitude = currentLoc.latitude,
            longitude = currentLoc.longitude,
            accuracyMeters = currentLoc.accuracyMeters,
            status = "ACTIVATED",
            isDemo = isDemo
        )

        _activeEmergency.value = event
        _currentScreen.value = ScreenState.SAFE_MODE

        // Provide Haptic & Voice Feedback
        if (profile.discreetMode) {
            haptics.triggerDiscreetPulse()
        } else {
            haptics.triggerEmergencyHaptic()
            if (profile.voiceFeedbackEnabled) {
                voiceFeedback.speak("Safe Mode Activated. Emergency Intent Detected. 10 second false alarm timer starting.", isDiscreetMode = false)
            }
        }

        // Save event to Room DB
        viewModelScope.launch {
            val id = repository.logEmergencyEvent(event)
            _activeEmergency.value = event.copy(id = id)
        }

        startCountdownTimer()
    }

    private fun startCountdownTimer() {
        countdownJob?.cancel()
        _countdownSeconds.value = 10
        countdownJob = viewModelScope.launch {
            for (i in 10 downTo 1) {
                _countdownSeconds.value = i
                delay(1000)
            }
            _countdownSeconds.value = 0
            // Timer expired -> Lock status as CONFIRMED
            confirmEmergencyAlert(autoTriggered = true)
        }
    }

    fun confirmEmergencyAlert(autoTriggered: Boolean = false) {
        countdownJob?.cancel()
        val current = _activeEmergency.value ?: return
        val updated = current.copy(status = "CONFIRMED")
        _activeEmergency.value = updated

        val profile = userProfile.value
        if (!profile.discreetMode && profile.voiceFeedbackEnabled) {
            voiceFeedback.speak(
                if (autoTriggered) "Emergency mode confirmed automatically." else "Emergency alert confirmed by user.",
                isDiscreetMode = profile.discreetMode
            )
        }

        viewModelScope.launch {
            repository.updateEventStatus(current.id, "CONFIRMED")
        }
        _statusMessage.value = "Emergency status confirmed locally"
    }

    fun cancelEmergencyAlert() {
        countdownJob?.cancel()
        haptics.triggerCancelHaptic()

        val current = _activeEmergency.value
        if (current != null) {
            val updated = current.copy(status = "CANCELLED_FALSE_ALARM")
            _activeEmergency.value = updated
            viewModelScope.launch {
                repository.updateEventStatus(current.id, "CANCELLED_FALSE_ALARM")
            }
        }

        val profile = userProfile.value
        if (!profile.discreetMode && profile.voiceFeedbackEnabled) {
            voiceFeedback.speak("Safe Mode Cancelled. False alarm recorded.", isDiscreetMode = profile.discreetMode)
        }

        _statusMessage.value = "Emergency cancelled (False alarm)"
        _currentScreen.value = ScreenState.DASHBOARD
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
            _statusMessage.value = "Emergency profile updated successfully"
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _statusMessage.value = "Emergency event log cleared"
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        voiceListener?.stopListening()
        voiceFeedback.shutdown()
    }
}

