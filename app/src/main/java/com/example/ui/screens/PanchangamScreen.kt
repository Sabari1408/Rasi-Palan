package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.components.ShareHelper
import com.example.ui.theme.GoldBright
import com.example.ui.theme.KumkumRed
import com.example.ui.theme.TempleMaroon
import com.example.ui.theme.TulsiGreen
import com.example.ui.viewmodel.RasiViewModel

@Composable
fun PanchangamScreen(
    viewModel: RasiViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val panchangam by viewModel.panchangam.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("panchangam_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Date Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🪔 " + panchangam.tamilDate + " 🪔",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = panchangam.tamilYear + " • " + panchangam.dateString,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = panchangam.paksham,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Auspicious Timings Card (Nalla Neram & Gowri Nalla Neram)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, TulsiGreen.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = TulsiGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.TAMIL) "சுப நேரங்கள் (Auspicious Timings)" else "Auspicious Timings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TulsiGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TimingItemRow(
                        label = if (language == Language.TAMIL) "காலை நல்ல நேரம்:" else "Morning Nalla Neram:",
                        time = panchangam.nallaNeramMorning,
                        badgeColor = TulsiGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TimingItemRow(
                        label = if (language == Language.TAMIL) "மாலை நல்ல நேரம்:" else "Evening Nalla Neram:",
                        time = panchangam.nallaNeramEvening,
                        badgeColor = TulsiGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TimingItemRow(
                        label = if (language == Language.TAMIL) "கௌரி நல்ல நேரம்:" else "Gowri Nalla Neram:",
                        time = panchangam.gowriNallaNeram,
                        badgeColor = GoldBright
                    )
                }
            }
        }

        // Inauspicious Timings (Rahu Kalam, Yamagandam, Kuligai)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, KumkumRed.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = KumkumRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.TAMIL)
                                "தவிர்க்க வேண்டிய நேரங்கள் (Inauspicious Windows)"
                            else
                                "Inauspicious Timings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = KumkumRed
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TimingItemRow(
                        label = if (language == Language.TAMIL) "இராகு காலம்:" else "Rahu Kalam:",
                        time = panchangam.rahuKaalam,
                        badgeColor = KumkumRed
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TimingItemRow(
                        label = if (language == Language.TAMIL) "எமகண்டம்:" else "Yamagandam:",
                        time = panchangam.yamagandam,
                        badgeColor = KumkumRed
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TimingItemRow(
                        label = if (language == Language.TAMIL) "குளிகை:" else "Kuligai:",
                        time = panchangam.kuligai,
                        badgeColor = Color(0xFFD97706)
                    )
                }
            }
        }

        // Planetary Details: Thithi, Nakshatram, Yogam, Karanam
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
                    Text(
                        text = "⭐ " + if (language == Language.TAMIL) "திதி & நவகிரக பஞ்சாங்கம்" else "Thithi & Nakshatra" + " ⭐",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PanchangAttributeRow(
                        label = if (language == Language.TAMIL) "திதி (Thithi)" else "Thithi",
                        value = panchangam.thithi
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PanchangAttributeRow(
                        label = if (language == Language.TAMIL) "நட்சத்திரம் (Star)" else "Nakshatra",
                        value = panchangam.nakshatram
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PanchangAttributeRow(
                        label = if (language == Language.TAMIL) "யோகம் (Yogam)" else "Yogam",
                        value = panchangam.yogam
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PanchangAttributeRow(
                        label = if (language == Language.TAMIL) "கரணம் (Karanam)" else "Karanam",
                        value = panchangam.karanam
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    PanchangAttributeRow(
                        label = if (language == Language.TAMIL) "சூரிய உதயம் & அஸ்தமனம்" else "Sunrise & Sunset",
                        value = "${panchangam.suryodayam} / ${panchangam.suryasthamanam}"
                    )
                }
            }
        }

        // Chandirashtamam Warning Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GoldBright.copy(alpha = 0.12f)
                ),
                border = BorderStroke(1.5.dp, GoldBright)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = GoldBright,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.TAMIL) "சந்திராஷ்டமம் (கவனமாக இருக்க வேண்டிய ராசி)" else "Chandirashtamam Caution",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == Language.TAMIL)
                            "இன்று: ${panchangam.chandirashtamamTamil} ராசிக்காரர்கள் புதிய முயற்சிகளை தவிர்க்கவும், பயணங்களில் கவனம் செலுத்தவும்."
                        else
                            "Today: ${panchangam.chandirashtamamEnglish} natives are advised to avoid starting new contracts and drive carefully.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Action Buttons Row: Share & Set Reminder
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        ShareHelper.sharePanchangam(
                            context = context,
                            tamilDate = panchangam.tamilDate,
                            nallaNeram = panchangam.nallaNeramMorning,
                            rahuKalam = panchangam.rahuKaalam,
                            yamagandam = panchangam.yamagandam,
                            chandirashtamam = panchangam.chandirashtamamTamil
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("share_panchangam_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (language == Language.TAMIL) "பகிர்" else "Share")
                }

                FilledTonalButton(
                    onClick = {
                        viewModel.triggerAuspiciousTimingAlert()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("alert_nallaneram_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (language == Language.TAMIL) "சுப நினைவூட்டல்" else "Alert")
                }
            }
        }
    }
}

@Composable
fun TimingItemRow(
    label: String,
    time: String,
    badgeColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Surface(
            color = badgeColor.copy(alpha = 0.15f),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = badgeColor,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

@Composable
fun PanchangAttributeRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
