package com.iqoo.safespeak.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.safespeak.ui.theme.TextPrimary
import com.iqoo.safespeak.ui.theme.TextSecondary
import com.iqoo.safespeak.ui.theme.iQOOCardBg
import com.iqoo.safespeak.ui.theme.iQOOCardBorder
import com.iqoo.safespeak.ui.theme.iQOONeonYellow

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DemoModeBar(
    onSimulate: (phrase: String, triggerName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(iQOOCardBg)
            .border(1.dp, iQOOCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Science,
                contentDescription = "Demo Mode",
                tint = iQOONeonYellow
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "HACKATHON DEMO MODE",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = iQOONeonYellow
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "OFFLINE DEMO — NO MESSAGE SENT",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFF9500),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF332000))
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Tap a preset utterance below to test offline AI intent detection on-stage:",
            fontSize = 12.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            AssistChip(
                onClick = { onSimulate("SafeSpeak, I need help!", "Judge Demo Trigger") },
                label = { Text("“SafeSpeak, I need help!”", fontSize = 11.sp, color = TextPrimary) },
                leadingIcon = { Icon(Icons.Default.VolumeUp, null, tint = iQOONeonYellow) },
                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF222538))
            )
            Spacer(modifier = Modifier.width(6.dp))

            AssistChip(
                onClick = { onSimulate("I am in danger", "Judge Danger Trigger") },
                label = { Text("“I am in danger”", fontSize = 11.sp, color = TextPrimary) },
                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF222538))
            )
            Spacer(modifier = Modifier.width(6.dp))

            AssistChip(
                onClick = { onSimulate("Activate SafeSpeak", "Judge Direct Trigger") },
                label = { Text("“Activate SafeSpeak”", fontSize = 11.sp, color = TextPrimary) },
                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF222538))
            )
            Spacer(modifier = Modifier.width(6.dp))

            AssistChip(
                onClick = { onSimulate("What's the weather today?", "False Alarm Test") },
                label = { Text("“What's the weather?” (Benign)", fontSize = 11.sp, color = TextSecondary) },
                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF191B26))
            )
        }
    }
}
