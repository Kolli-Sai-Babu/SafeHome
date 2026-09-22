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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.safespeak.data.local.EmergencyEvent
import com.iqoo.safespeak.ui.theme.TextPrimary
import com.iqoo.safespeak.ui.theme.TextSecondary
import com.iqoo.safespeak.ui.theme.iQOOCardBg
import com.iqoo.safespeak.ui.theme.iQOOCardBorder
import com.iqoo.safespeak.ui.theme.iQOOCyan
import com.iqoo.safespeak.ui.theme.iQOOEmergencyRed
import com.iqoo.safespeak.ui.theme.iQOONeonYellow
import com.iqoo.safespeak.ui.theme.iQOOSuccessGreen

@Composable
fun HistoryScreen(
    events: List<EmergencyEvent>,
    onClearAll: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0E15))
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Emergency Event Log", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "Persisted locally in Room DB", fontSize = 11.sp, color = iQOONeonYellow)
            }
            Spacer(modifier = Modifier.weight(1f))
            if (events.isNotEmpty()) {
                IconButton(onClick = onClearAll) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear History", tint = iQOOEmergencyRed)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (events.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.History, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "No emergency events logged yet.", fontSize = 14.sp, color = TextSecondary)
                    Text(text = "Test voice triggers on dashboard to log events.", fontSize = 12.sp, color = iQOONeonYellow)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(events) { event ->
                    EventCard(event = event)
                }
            }
        }
    }
}

@Composable
fun EventCard(event: EmergencyEvent) {
    val statusColor = when (event.status) {
        "CONFIRMED" -> iQOOSuccessGreen
        "CANCELLED_FALSE_ALARM" -> iQOONeonYellow
        else -> iQOOEmergencyRed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, iQOOCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = iQOOCardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (event.status == "CONFIRMED") Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = event.triggerType,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = event.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${event.timestampFormatted}", fontSize = 12.sp, color = TextSecondary)
            Text(text = "Utterance: “${event.recognizedText}”", fontSize = 12.sp, color = iQOOCyan)
            Text(text = "AI Confidence: ${((event.confidenceScore) * 100).toInt()}% Match", fontSize = 11.sp, color = TextSecondary)

            if (event.locationAvailable && event.latitude != null) {
                Text(
                    text = "GPS Location: ${String.format("%.4f", event.latitude)}, ${String.format("%.4f", event.longitude)} (±${event.accuracyMeters ?: 10f}m)",
                    fontSize = 11.sp,
                    color = iQOOSuccessGreen
                )
            } else {
                Text(text = "Location: GPS unavailable at activation time", fontSize = 11.sp, color = iQOOEmergencyRed)
            }
        }
    }
}
