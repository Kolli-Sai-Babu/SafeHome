package com.iqoo.safespeak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.safespeak.ai.IntentClassificationResult
import com.iqoo.safespeak.data.local.UserProfile
import com.iqoo.safespeak.services.OfflineLocationData
import com.iqoo.safespeak.ui.components.DemoModeBar
import com.iqoo.safespeak.ui.theme.TextPrimary
import com.iqoo.safespeak.ui.theme.TextSecondary
import com.iqoo.safespeak.ui.theme.iQOOCardBg
import com.iqoo.safespeak.ui.theme.iQOOCardBorder
import com.iqoo.safespeak.ui.theme.iQOOCyan
import com.iqoo.safespeak.ui.theme.iQOOEmergencyRed
import com.iqoo.safespeak.ui.theme.iQOONeonYellow
import com.iqoo.safespeak.ui.theme.iQOOSuccessGreen
import com.iqoo.safespeak.viewmodel.ScreenState

@Composable
fun MainDashboardScreen(
    userProfile: UserProfile,
    locationData: OfflineLocationData,
    isListening: Boolean,
    lastClassification: IntentClassificationResult?,
    statusMessage: String?,
    onNavigate: (ScreenState) -> Unit,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onSimulate: (phrase: String, triggerName: String) -> Unit,
    onManualEmergencyTrigger: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0E15))
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // App Header & iQOO Hackathon Branding
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SafeSpeak",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = "iQOO Smart Living • Offline AI Emergency Assistant",
                    fontSize = 11.sp,
                    color = iQOONeonYellow
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            // Main Status Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF102819))
                    .border(1.dp, iQOOSuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(iQOOSuccessGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "OFFLINE READY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = iQOOSuccessGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // System Hardware Badges (AI: On-Device, Network: Offline, Location: Hardware GPS)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BadgeChip(label = "AI: ON-DEVICE", color = iQOONeonYellow, modifier = Modifier.weight(1f))
            BadgeChip(label = "NETWORK: OFFLINE", color = iQOOCyan, modifier = Modifier.weight(1f))
            BadgeChip(
                label = if (locationData.isAvailable) "GPS: AVAILABLE" else "GPS: HARDWARE READY",
                color = if (locationData.isAvailable) iQOOSuccessGreen else TextSecondary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, iQOOCardBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = iQOOCardBg),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = iQOOSuccessGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Your emergency assistant is ready.",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Voice intent engine is standby locally.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                if (statusMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Status: $statusMessage",
                        fontSize = 12.sp,
                        color = iQOONeonYellow,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (lastClassification != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E2133))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Last Voice AI Match: ${lastClassification.matchedUtteranceSummary()}",
                            fontSize = 11.sp,
                            color = iQOOCyan
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hackathon Judge Demo Mode Bar
        DemoModeBar(onSimulate = onSimulate)

        Spacer(modifier = Modifier.height(16.dp))

        // Large Voice Activation Button Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (isListening) iQOONeonYellow else iQOOCardBorder,
                    shape = RoundedCornerShape(20.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = iQOOCardBg),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VOICE TRIGGER ACTIVATION",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = iQOONeonYellow,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Say: “SafeSpeak, I need help.”",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (isListening) onStopListening() else onStartListening()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isListening) iQOOEmergencyRed else iQOONeonYellow,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Test Voice",
                        tint = if (isListening) Color.White else Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isListening) "STOP LISTENING (ACTIVE)" else "TEST VOICE TRIGGER",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (isListening) Color.White else Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onManualEmergencyTrigger,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = iQOOEmergencyRed.copy(alpha = 0.2f),
                        contentColor = iQOOEmergencyRed
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(1.dp, iQOOEmergencyRed, RoundedCornerShape(30.dp))
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = iQOOEmergencyRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "MANUAL EMERGENCY OVERRIDE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Navigation Grid
        Text(
            text = "QUICK MANAGEMENT",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            QuickCard(
                title = "Emergency Profile",
                subtitle = userProfile.fullName,
                icon = Icons.Default.Person,
                color = iQOOCyan,
                onClick = { onNavigate(ScreenState.PROFILE_EDIT) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            QuickCard(
                title = "Trusted Contact",
                subtitle = userProfile.contactName,
                icon = Icons.Default.ContactPhone,
                color = iQOOSuccessGreen,
                onClick = { onNavigate(ScreenState.PROFILE_EDIT) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            QuickCard(
                title = "Emergency History",
                subtitle = "Local Event Logs",
                icon = Icons.Default.History,
                color = iQOONeonYellow,
                onClick = { onNavigate(ScreenState.HISTORY_LOG) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            QuickCard(
                title = "Settings & Privacy",
                subtitle = "Offline Guarantee",
                icon = Icons.Default.Settings,
                color = TextSecondary,
                onClick = { onNavigate(ScreenState.SETTINGS_PRIVACY) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun BadgeChip(label: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun QuickCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, iQOOCardBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = iQOOCardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(26.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = subtitle, fontSize = 11.sp, color = TextSecondary, maxLines = 1)
        }
    }
}

private fun IntentClassificationResult.matchedUtteranceSummary(): String {
    return "“${rawUtterance}” → ${(confidenceScore * 100).toInt()}% match ($matchedIntentName)"
}
