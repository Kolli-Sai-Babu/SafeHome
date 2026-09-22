package com.iqoo.safespeak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.safespeak.data.local.UserProfile
import com.iqoo.safespeak.ui.theme.TextPrimary
import com.iqoo.safespeak.ui.theme.TextSecondary
import com.iqoo.safespeak.ui.theme.iQOOCardBg
import com.iqoo.safespeak.ui.theme.iQOOCardBorder
import com.iqoo.safespeak.ui.theme.iQOOCyan
import com.iqoo.safespeak.ui.theme.iQOONeonYellow
import com.iqoo.safespeak.ui.theme.iQOOSuccessGreen

@Composable
fun SettingsPrivacyScreen(
    userProfile: UserProfile,
    onSaveProfile: (UserProfile) -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0E15))
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Settings & Privacy", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "Offline First Architecture", fontSize = 11.sp, color = iQOONeonYellow)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Privacy Guarantee Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, iQOOSuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2416)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = iQOOSuccessGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "100% PRIVACY FIRST GUARANTEE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = iQOOSuccessGreen
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "“SafeSpeak processes emergency voice commands locally on the device whenever possible.”",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Zero microphone audio sent to cloud servers.\n• Zero dependence on OpenAI, Gemini, or Groq APIs.\n• Local SQLite Room DB storage for contacts & logs.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Safety Feature Toggles
        Text(
            text = "SAFETY PREFERENCES",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Discreet Mode
        SettingToggleCard(
            title = "Discreet / Silent Mode",
            subtitle = "Provides silent haptic feedback and no loud emergency alarms in stealth situations.",
            icon = Icons.Default.VolumeOff,
            checked = userProfile.discreetMode,
            onCheckedChange = { checked ->
                onSaveProfile(userProfile.copy(discreetMode = checked))
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Voice Feedback (TTS)
        SettingToggleCard(
            title = "Offline Voice Feedback (TTS)",
            subtitle = "Speaks emergency confirmation status locally via Android Text-To-Speech.",
            icon = Icons.Default.VolumeUp,
            checked = userProfile.voiceFeedbackEnabled,
            onCheckedChange = { checked ->
                onSaveProfile(userProfile.copy(voiceFeedbackEnabled = checked))
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // System Technical Information
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, iQOOCardBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = iQOOCardBg),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = iQOOCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "HACKATHON BUILD SPECS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = iQOOCyan)
                }
                Spacer(modifier = Modifier.height(8.dp))
                TechRow("Target Device:", "iQOO Smartphone (Android 8.0 - 15)")
                TechRow("AI Intent Classifier:", "On-Device Local NLP Vector Engine")
                TechRow("Speech Recognition:", "Offline Android SpeechRecognizer")
                TechRow("GPS Hardware Provider:", "Fused Location & Satellite Direct")
                TechRow("Local Database:", "Android Room SQLite (Encrypted)")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SettingToggleCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, iQOOCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = iQOOCardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iQOONeonYellow, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = subtitle, fontSize = 11.sp, color = TextSecondary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = iQOONeonYellow,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = Color(0xFF242738)
                )
            )
        }
    }
}

@Composable
fun TechRow(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(text = key, fontSize = 11.sp, color = TextSecondary, modifier = Modifier.width(130.dp))
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}
