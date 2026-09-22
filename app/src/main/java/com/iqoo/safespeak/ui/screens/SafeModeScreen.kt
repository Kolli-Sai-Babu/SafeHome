package com.iqoo.safespeak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.safespeak.data.local.EmergencyEvent
import com.iqoo.safespeak.data.local.UserProfile
import com.iqoo.safespeak.services.OfflineLocationData
import com.iqoo.safespeak.ui.theme.TextPrimary
import com.iqoo.safespeak.ui.theme.TextSecondary
import com.iqoo.safespeak.ui.theme.iQOOCardBg
import com.iqoo.safespeak.ui.theme.iQOOCardBorder
import com.iqoo.safespeak.ui.theme.iQOOCyan
import com.iqoo.safespeak.ui.theme.iQOOEmergencyRed
import com.iqoo.safespeak.ui.theme.iQOONeonYellow
import com.iqoo.safespeak.ui.theme.iQOOSuccessGreen

@Composable
fun SafeModeScreen(
    activeEvent: EmergencyEvent?,
    userProfile: UserProfile,
    locationData: OfflineLocationData,
    countdownSeconds: Int,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isConfirmed = activeEvent?.status == "CONFIRMED"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0506)) // Deep Emergency Dark background
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Large 🚨 Siren Indicator
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(iQOOEmergencyRed.copy(alpha = 0.2f))
                .border(2.dp, iQOOEmergencyRed, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🚨", fontSize = 36.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "SAFE MODE ACTIVATED",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = iQOOEmergencyRed,
            letterSpacing = 1.sp
        )

        Text(
            text = "Emergency intent detected offline on device.",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Countdown Timer & False Alarm Protection Banner
        if (!isConfirmed) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, iQOONeonYellow.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF261D0A)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(iQOONeonYellow.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$countdownSeconds",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = iQOONeonYellow
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "FALSE ACTIVATION PROTECTION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = iQOONeonYellow
                        )
                        Text(
                            text = "Tap CANCEL below within $countdownSeconds seconds if this alert was accidental.",
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, iQOOSuccessGreen, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2614)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = iQOOSuccessGreen, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "EMERGENCY ALERT CONFIRMED",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = iQOOSuccessGreen
                        )
                        Text(
                            text = "Local emergency package compiled and latched locally.",
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trigger Details Card
        DetailCard(
            title = "ACTIVATION REASON & TIME",
            icon = Icons.Default.Info,
            iconTint = iQOONeonYellow
        ) {
            DetailRow(label = "Time of Activation:", value = activeEvent?.timestampFormatted ?: "Just Now")
            DetailRow(label = "Trigger Type:", value = activeEvent?.triggerType ?: "Voice Trigger (Offline AI)")
            DetailRow(label = "Utterance Recognized:", value = "“${activeEvent?.recognizedText ?: "I need help"}”")
            DetailRow(label = "AI Confidence Score:", value = "${((activeEvent?.confidenceScore ?: 0.96f) * 100).toInt()}% Match")
            DetailRow(label = "Processing Engine:", value = "On-Device Offline NLP (Local CPU)")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Location Info Card (GPS Hardware)
        DetailCard(
            title = "LOCATION STATUS (OFFLINE GPS)",
            icon = Icons.Default.LocationOn,
            iconTint = iQOOCyan
        ) {
            val isAvail = locationData.isAvailable
            DetailRow(
                label = "Location Status:",
                value = if (isAvail) "AVAILABLE (Hardware Fix)" else "UNAVAILABLE (Retrying GPS)",
                valueColor = if (isAvail) iQOOSuccessGreen else iQOOEmergencyRed
            )
            if (isAvail && locationData.latitude != null) {
                DetailRow(label = "Latitude:", value = String.format("%.6f° N", locationData.latitude))
                DetailRow(label = "Longitude:", value = String.format("%.6f° E", locationData.longitude))
                DetailRow(label = "Accuracy Radius:", value = "±${locationData.accuracyMeters ?: 10.0f} meters")
                DetailRow(label = "Hardware Provider:", value = locationData.provider)
            } else {
                Text(
                    text = "GPS hardware fix in progress. Last known cached location loaded.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Saved Emergency Contact Card
        DetailCard(
            title = "SAVED EMERGENCY CONTACT",
            icon = Icons.Default.ContactPhone,
            iconTint = iQOOSuccessGreen
        ) {
            DetailRow(label = "Contact Name:", value = userProfile.contactName)
            DetailRow(label = "Phone Number:", value = userProfile.contactPhone)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Local Emergency Medical Profile
        DetailCard(
            title = "LOCAL EMERGENCY PROFILE",
            icon = Icons.Default.MedicalServices,
            iconTint = iQOOEmergencyRed
        ) {
            DetailRow(label = "Patient Name:", value = userProfile.fullName)
            DetailRow(label = "Blood Group:", value = userProfile.bloodGroup)
            DetailRow(label = "Medical Notes:", value = userProfile.medicalInfo)
            DetailRow(label = "Prepared Emergency Text:", value = userProfile.customMessage)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Confirm & Cancel Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C1619),
                    contentColor = iQOOEmergencyRed
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .border(1.dp, iQOOEmergencyRed, RoundedCornerShape(16.dp))
            ) {
                Icon(Icons.Default.Cancel, null, tint = iQOOEmergencyRed)
                Spacer(modifier = Modifier.width(6.dp))
                Text("CANCEL", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = iQOOEmergencyRed,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
            ) {
                Icon(Icons.Default.CheckCircle, null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isConfirmed) "CONFIRMED" else "CONFIRM", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DetailCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, iQOOCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = iQOOCardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = iconTint,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = TextPrimary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}
