package com.iqoo.safespeak

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.iqoo.safespeak.ui.screens.HistoryScreen
import com.iqoo.safespeak.ui.screens.MainDashboardScreen
import com.iqoo.safespeak.ui.screens.ProfileScreen
import com.iqoo.safespeak.ui.screens.SafeModeScreen
import com.iqoo.safespeak.ui.screens.SettingsPrivacyScreen
import com.iqoo.safespeak.ui.theme.SafeSpeakTheme
import com.iqoo.safespeak.ui.theme.iQOOBackground
import com.iqoo.safespeak.viewmodel.MainViewModel
import com.iqoo.safespeak.viewmodel.ScreenState

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val micGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false
        val locGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        if (locGranted) {
            viewModel.refreshLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()

        setContent {
            SafeSpeakTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = iQOOBackground
                ) {
                    val currentScreen by viewModel.currentScreen.collectAsState()
                    val userProfile by viewModel.userProfile.collectAsState()
                    val emergencyEvents by viewModel.emergencyEvents.collectAsState()
                    val isListening by viewModel.isListening.collectAsState()
                    val lastClassification by viewModel.lastClassification.collectAsState()
                    val activeEmergency by viewModel.activeEmergency.collectAsState()
                    val locationData by viewModel.locationData.collectAsState()
                    val countdownSeconds by viewModel.countdownSeconds.collectAsState()
                    val statusMessage by viewModel.statusMessage.collectAsState()

                    // Back button handling
                    BackHandler(enabled = currentScreen != ScreenState.DASHBOARD) {
                        if (currentScreen == ScreenState.SAFE_MODE) {
                            viewModel.cancelEmergencyAlert()
                        } else {
                            viewModel.navigateTo(ScreenState.DASHBOARD)
                        }
                    }

                    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                        when (screen) {
                            ScreenState.DASHBOARD -> MainDashboardScreen(
                                userProfile = userProfile,
                                locationData = locationData,
                                isListening = isListening,
                                lastClassification = lastClassification,
                                statusMessage = statusMessage,
                                onNavigate = { viewModel.navigateTo(it) },
                                onStartListening = { viewModel.startListening() },
                                onStopListening = { viewModel.stopListening() },
                                onSimulate = { phrase, triggerName ->
                                    viewModel.simulateUtterance(phrase, triggerName)
                                },
                                onManualEmergencyTrigger = {
                                    viewModel.triggerEmergency(
                                        triggerType = "Manual Override Button",
                                        text = "Manual Emergency Override",
                                        isDemo = true
                                    )
                                }
                            )

                            ScreenState.SAFE_MODE -> SafeModeScreen(
                                activeEvent = activeEmergency,
                                userProfile = userProfile,
                                locationData = locationData,
                                countdownSeconds = countdownSeconds,
                                onConfirm = { viewModel.confirmEmergencyAlert() },
                                onCancel = { viewModel.cancelEmergencyAlert() }
                            )

                            ScreenState.PROFILE_EDIT -> ProfileScreen(
                                currentProfile = userProfile,
                                onSaveProfile = { viewModel.saveProfile(it) },
                                onBack = { viewModel.navigateTo(ScreenState.DASHBOARD) }
                            )

                            ScreenState.HISTORY_LOG -> HistoryScreen(
                                events = emergencyEvents,
                                onClearAll = { viewModel.clearAllHistory() },
                                onBack = { viewModel.navigateTo(ScreenState.DASHBOARD) }
                            )

                            ScreenState.SETTINGS_PRIVACY -> SettingsPrivacyScreen(
                                userProfile = userProfile,
                                onSaveProfile = { viewModel.saveProfile(it) },
                                onBack = { viewModel.navigateTo(ScreenState.DASHBOARD) }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.VIBRATE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val missing = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missing.isNotEmpty()) {
            requestPermissionLauncher.launch(missing.toTypedArray())
        }
    }
}
