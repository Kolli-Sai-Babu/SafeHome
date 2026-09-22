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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.safespeak.data.local.UserProfile
import com.iqoo.safespeak.ui.theme.TextPrimary
import com.iqoo.safespeak.ui.theme.TextSecondary
import com.iqoo.safespeak.ui.theme.iQOOCardBorder
import com.iqoo.safespeak.ui.theme.iQOONeonYellow

@Composable
fun ProfileScreen(
    currentProfile: UserProfile,
    onSaveProfile: (UserProfile) -> Unit,
    onBack: () -> Unit
) {
    var fullName by remember(currentProfile) { mutableStateOf(currentProfile.fullName) }
    var contactName by remember(currentProfile) { mutableStateOf(currentProfile.contactName) }
    var contactPhone by remember(currentProfile) { mutableStateOf(currentProfile.contactPhone) }
    var bloodGroup by remember(currentProfile) { mutableStateOf(currentProfile.bloodGroup) }
    var medicalInfo by remember(currentProfile) { mutableStateOf(currentProfile.medicalInfo) }
    var customMessage by remember(currentProfile) { mutableStateOf(currentProfile.customMessage) }

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
                Text(text = "Emergency Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "Stored 100% locally on your device", fontSize = 11.sp, color = iQOONeonYellow)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form Fields
        ProfileField(label = "Full Name", value = fullName, onValueChange = { fullName = it })
        ProfileField(label = "Trusted Contact Name", value = contactName, onValueChange = { contactName = it })
        ProfileField(label = "Trusted Contact Phone", value = contactPhone, onValueChange = { contactPhone = it })
        ProfileField(label = "Blood Group (e.g. O+, A+, B-)", value = bloodGroup, onValueChange = { bloodGroup = it })
        ProfileField(label = "Medical Notes & Allergies", value = medicalInfo, onValueChange = { medicalInfo = it }, minLines = 2)
        ProfileField(label = "Custom Emergency Message", value = customMessage, onValueChange = { customMessage = it }, minLines = 3)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val updated = currentProfile.copy(
                    fullName = fullName,
                    contactName = contactName,
                    contactPhone = contactPhone,
                    bloodGroup = bloodGroup,
                    medicalInfo = medicalInfo,
                    customMessage = customMessage
                )
                onSaveProfile(updated)
                onBack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = iQOONeonYellow,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(8.dp))
            Text("SAVE LOCAL PROFILE", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 1
) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, iQOOCardBorder, RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = iQOONeonYellow,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color(0xFF161824),
                unfocusedContainerColor = Color(0xFF161824),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            minLines = minLines
        )
    }
}
