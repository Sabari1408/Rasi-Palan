package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.RasiId
import com.example.data.repository.AstrologyDataProvider
import com.example.ui.theme.GoldBright
import com.example.ui.theme.TulsiGreen
import com.example.ui.viewmodel.RasiViewModel

@Composable
fun ProfileScreen(
    viewModel: RasiViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var selectedRasiIndex by remember(userProfile) { mutableStateOf(userProfile.savedRasiId) }
    var selectedNakshatraId by remember(userProfile) { mutableStateOf(userProfile.savedNakshatraId) }
    var selectedPada by remember(userProfile) { mutableStateOf(userProfile.savedPada) }
    var notificationsEnabled by remember(userProfile) { mutableStateOf(userProfile.notificationsEnabled) }

    var isRasiPickerOpen by remember { mutableStateOf(false) }
    var isNakshatraPickerOpen by remember { mutableStateOf(false) }

    val allRasis = AstrologyDataProvider.allRasis
    val allNakshatras = AstrologyDataProvider.allNakshatras

    val currentSelectedRasi = allRasis.firstOrNull { it.id.index == selectedRasiIndex } ?: allRasis.first()
    val currentSelectedNakshatra = allNakshatras.firstOrNull { it.id == selectedNakshatraId } ?: allNakshatras.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(GoldBright.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentSelectedRasi.symbolEmoji,
                            fontSize = 28.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = if (language == Language.TAMIL) "உங்கள் சுயவிவரம்" else "Personal Astrological Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "${if (language == Language.TAMIL) currentSelectedRasi.nameTamil else currentSelectedRasi.nameEnglish} • ${if (language == Language.TAMIL) currentSelectedNakshatra.nameTamil else currentSelectedNakshatra.nameEnglish} ($selectedPada)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (language == Language.TAMIL)
                                "தினசரி விரைவு குறிப்பு மற்றும் தனிப்பயன் எச்சரிக்கைகளுக்கு"
                            else
                                "For daily quick reference and personalized alerts",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // 1. Rasi Selection Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (language == Language.TAMIL) "1. உங்கள் ராசி" else "1. Your Zodiac Sign (Rasi)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${currentSelectedRasi.symbolEmoji} ${if (language == Language.TAMIL) currentSelectedRasi.nameTamil else currentSelectedRasi.nameEnglish} (${currentSelectedRasi.nameEnglish})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(
                            onClick = { isRasiPickerOpen = !isRasiPickerOpen },
                            modifier = Modifier.testTag("toggle_rasi_picker_button")
                        ) {
                            Text(
                                text = if (isRasiPickerOpen)
                                    (if (language == Language.TAMIL) "மூடுக" else "Close")
                                else
                                    (if (language == Language.TAMIL) "மாற்றுக" else "Change")
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isRasiPickerOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // In-place Expandable Grid for all 12 Rasis (Zero popup window issues!)
                    AnimatedVisibility(
                        visible = isRasiPickerOpen,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(
                                text = if (language == Language.TAMIL) "ராசியைத் தொடவும்:" else "Select your sign:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // 12 Rasis displayed in rows of 3
                            val rows = allRasis.chunked(3)
                            rows.forEach { rowRasis ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    rowRasis.forEach { rasi ->
                                        val isSelected = rasi.id.index == selectedRasiIndex
                                        Surface(
                                            onClick = {
                                                selectedRasiIndex = rasi.id.index
                                                isRasiPickerOpen = false
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("pick_rasi_${rasi.id.name}"),
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            border = if (isSelected) BorderStroke(1.5.dp, GoldBright) else null
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(text = rasi.symbolEmoji, fontSize = 18.sp)
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = if (language == Language.TAMIL) rasi.nameTamil else rasi.nameEnglish,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                                    fontSize = 11.sp,
                                                    maxLines = 1
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Nakshatra (Star) Selection Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (language == Language.TAMIL) "2. உங்கள் நட்சத்திரம் (27 நட்சத்திரங்கள்)" else "2. Birth Star (Nakshatra)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${if (language == Language.TAMIL) currentSelectedNakshatra.nameTamil else currentSelectedNakshatra.nameEnglish} (அதிபதி: ${currentSelectedNakshatra.lordTamil})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(
                            onClick = { isNakshatraPickerOpen = !isNakshatraPickerOpen },
                            modifier = Modifier.testTag("toggle_nakshatra_picker_button")
                        ) {
                            Text(
                                text = if (isNakshatraPickerOpen)
                                    (if (language == Language.TAMIL) "மூடுக" else "Close")
                                else
                                    (if (language == Language.TAMIL) "மாற்றுக" else "Change")
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isNakshatraPickerOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // In-place Expandable List for 27 Nakshatras
                    AnimatedVisibility(
                        visible = isNakshatraPickerOpen,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(
                                text = if (language == Language.TAMIL) "நட்சத்திரத்தைத் தொடவும்:" else "Select birth star:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val starRows = allNakshatras.chunked(2)
                            starRows.forEach { rowStars ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    rowStars.forEach { star ->
                                        val isSelected = star.id == selectedNakshatraId
                                        Surface(
                                            onClick = {
                                                selectedNakshatraId = star.id
                                                isNakshatraPickerOpen = false
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("pick_nakshatra_${star.id}"),
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            border = if (isSelected) BorderStroke(1.5.dp, GoldBright) else null
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = if (language == Language.TAMIL) star.nameTamil else star.nameEnglish,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Text(
                                                    text = star.lordTamil,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Pada Selection (1, 2, 3, 4)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = if (language == Language.TAMIL) "3. நட்சத்திர பாதம்" else "3. Birth Star Pada (1 - 4)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (1..4).forEach { pada ->
                            val isSelected = pada == selectedPada
                            Surface(
                                onClick = { selectedPada = pada },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pada_button_$pada"),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) BorderStroke(1.5.dp, GoldBright) else null
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (language == Language.TAMIL) "$pada-ம் பாதம்" else "Pada $pada",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Notification Preferences Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (language == Language.TAMIL)
                                    "தினசரி காலை ராசி பலன் எச்சரிக்கை"
                                else
                                    "Daily Morning Horoscope Alert",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (language == Language.TAMIL)
                                    "காலை 07:00 மணிக்கு உங்கள் ராசிக்கான பலன்கள்"
                                else
                                    "Personalized alert at 07:00 AM daily",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            modifier = Modifier.testTag("notification_toggle_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { viewModel.triggerTestNotification() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("test_push_alert_button"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == Language.TAMIL)
                                "உடனடி அறிவிப்பை சோதிக்க (Test Push Alert)"
                            else
                                "Test Push Notification Now",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 5. Save Profile Button
        item {
            Button(
                onClick = {
                    viewModel.saveProfile(
                        rasiId = RasiId.fromIndex(selectedRasiIndex),
                        nakshatraId = selectedNakshatraId,
                        pada = selectedPada,
                        notificationsEnabled = notificationsEnabled
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_profile_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == Language.TAMIL) "சேமிக்க (Save Details)" else "Save Astrological Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
