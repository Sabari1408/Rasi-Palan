package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.ThemeMode
import com.example.ui.theme.GoldBright
import com.example.ui.viewmodel.RasiViewModel

@Composable
fun SettingsScreen(
    viewModel: RasiViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val isOfflineMode by viewModel.isOfflineMode.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Language Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                    SettingHeader(
                        icon = Icons.Default.Language,
                        title = if (language == Language.TAMIL) "மொழி அமைப்பு (Language)" else "Language Preference"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LanguageChoiceButton(
                            text = "தமிழ் (Tamil)",
                            isSelected = language == Language.TAMIL,
                            onClick = { viewModel.setLanguage(Language.TAMIL) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("lang_tamil_button")
                        )

                        LanguageChoiceButton(
                            text = "English",
                            isSelected = language == Language.ENGLISH,
                            onClick = { viewModel.setLanguage(Language.ENGLISH) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("lang_english_button")
                        )
                    }
                }
            }
        }

        // Night Mode / Dark Mode Theme Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                    SettingHeader(
                        icon = Icons.Default.DarkMode,
                        title = if (language == Language.TAMIL)
                            "டார்க் மோட் / இரவு வாசிப்பு (Theme Mode)"
                        else
                            "Theme & Night Reading Mode"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeChoiceButton(
                            text = if (language == Language.TAMIL) "தானியங்கி" else "System",
                            isSelected = themeMode == ThemeMode.SYSTEM,
                            onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_system_button")
                        )

                        ThemeChoiceButton(
                            text = if (language == Language.TAMIL) "இரவு டார்க்" else "Dark",
                            isSelected = themeMode == ThemeMode.DARK,
                            onClick = { viewModel.setThemeMode(ThemeMode.DARK) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_dark_button")
                        )

                        ThemeChoiceButton(
                            text = if (language == Language.TAMIL) "பகல் ஒளிர்வு" else "Light",
                            isSelected = themeMode == ThemeMode.LIGHT,
                            onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_light_button")
                        )
                    }
                }
            }
        }

        // Sacred 'Om' Notification Sound Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, GoldBright.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    SettingHeader(
                        icon = Icons.Default.MusicNote,
                        title = if (language == Language.TAMIL)
                            "அறிவிப்பு 'ஓம்' ஒலி (Sacred 'Om' Sound)"
                        else
                            "Notification Sound ('Om' Sound)"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == Language.TAMIL)
                            "தினசரி காலை ராசி பலன் மற்றும் சுப நேர நினைவூட்டல் அறிவிப்புகள் மங்கலகரமான 'ஓம்' ஒலியுடன் ஒலிக்கும் வண்ணம் அமைக்கப்பட்டுள்ளது."
                        else
                            "Daily horoscope and auspicious timing alerts are configured with the sacred 'Om' sound for a peaceful morning start.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.playOmSoundOnly() },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("play_om_sound_button"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == Language.TAMIL) "ஓம் ஒலி கேட்க" else "Play Sound",
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = { viewModel.triggerTestNotification() },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("test_alert_om_button"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == Language.TAMIL) "சோதனை செய்" else "Test Alert",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Offline Mode & Cache Controls
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                    SettingHeader(
                        icon = Icons.Default.OfflinePin,
                        title = if (language == Language.TAMIL)
                            "ஆஃப்லைன் வசதி & சேமிப்பகம் (Room DB)"
                        else
                            "Offline Cache & Storage"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == Language.TAMIL)
                            "இணையம் இல்லாமலும் இன்றைய மற்றும் முந்தைய ராசி பலன்கள் உங்கள் சாதனத்தில் பாதுகாப்பாக சேமிக்கப்பட்டுள்ளன."
                        else
                            "Horoscopes and panchangam are cached locally in Room database for 100% offline access.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == Language.TAMIL) "ஆஃப்லைன் சோதனை முறை" else "Simulate Offline Mode",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Switch(
                            checked = isOfflineMode,
                            onCheckedChange = { viewModel.toggleOfflineMode() },
                            modifier = Modifier.testTag("offline_mode_switch")
                        )
                    }
                }
            }
        }

        // Device Performance & About App Info Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    SettingHeader(
                        icon = Icons.Default.Info,
                        title = if (language == Language.TAMIL) "செயலி தகவல் (App Info)" else "About App"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "தமிழ் ராசி பலன் (Tamil Rasi Palan) v1.0",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == Language.TAMIL)
                            "குறைந்த நினைவக திறன் கொண்ட மொபைல்களிலும் (Low-end devices) வேகமாக இயங்கும் வண்ணம் இலகுவாக (Lightweight) வடிவமைக்கப்பட்டுள்ளது. Google Ads விளம்பர ஆதரவுடன் செயல்படுகிறது."
                        else
                            "Lightweight architecture optimized for low-end devices and older Android versions. Ad-supported via Google Ads.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SettingHeader(
    icon: ImageVector,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LanguageChoiceButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) BorderStroke(1.5.dp, GoldBright) else null
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ThemeChoiceButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) BorderStroke(1.5.dp, GoldBright) else null
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            )
        }
    }
}
