package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.components.DateSelectorBar
import com.example.ui.components.ShareHelper
import com.example.ui.theme.GoldBright
import com.example.ui.theme.KumkumRed
import com.example.ui.theme.TempleMaroon
import com.example.ui.theme.TulsiGreen
import com.example.ui.viewmodel.RasiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RasiDetailScreen(
    viewModel: RasiViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val language by viewModel.language.collectAsState()
    val rasi = viewModel.selectedRasi.collectAsState().value
    val selectedCalendar by viewModel.selectedCalendar.collectAsState()
    val horoscope = viewModel.currentHoroscope.collectAsState().value
    val isToday = viewModel.isTodaySelected()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(rasi.symbolEmoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (language == Language.TAMIL) rasi.nameTamil else rasi.nameEnglish,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (language == Language.TAMIL)
                                    "அதிபதி: ${rasi.lordTamil} • ${rasi.elementTamil}"
                                else
                                    "Lord: ${rasi.lordEnglish} • ${rasi.elementEnglish}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (horoscope != null) {
                                ShareHelper.shareHoroscope(
                                    context = context,
                                    rasiInfo = rasi,
                                    horoscope = horoscope,
                                    language = language,
                                    tamilDate = horoscope.dateDisplayTamil
                                )
                            }
                        },
                        modifier = Modifier.testTag("share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("rasi_detail_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Interactive Date Selector Bar (Today by default; pick past or future dates)
            item {
                DateSelectorBar(
                    selectedCalendar = selectedCalendar,
                    isToday = isToday,
                    language = language,
                    onPreviousDay = { viewModel.goToPreviousDay() },
                    onNextDay = { viewModel.goToNextDay() },
                    onTodayClick = { viewModel.goToToday() },
                    onDateSelected = { cal -> viewModel.selectDate(cal) }
                )
            }

            // Gemini AI Badge and Refresh
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = GoldBright.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldBright, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == Language.TAMIL) "Gemini AI ஜோதிடக் கணிப்பு" else "Powered by Gemini AI",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GoldBright
                            )
                        }
                    }

                    TextButton(
                        onClick = { viewModel.refreshWithGeminiAi() },
                        modifier = Modifier.testTag("refresh_ai_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = if (language == Language.TAMIL) "AI புதுப்பி" else "AI Refresh", fontSize = 12.sp)
                    }
                }
            }

            if (horoscope == null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = GoldBright)
                    }
                }
            } else {
                // Score card with percentage and metrics
                item {
                    OverallRatingCard(
                        score = horoscope.overallPercentage,
                        careerScore = horoscope.careerScore,
                        financeScore = horoscope.financeScore,
                        healthScore = horoscope.healthScore,
                        familyScore = horoscope.familyScore,
                        dateDisplay = if (language == Language.TAMIL) horoscope.dateDisplayTamil else horoscope.dateDisplayEnglish,
                        language = language
                    )
                }

                // General Overview Prediction
                item {
                    PredictionCard(
                        icon = Icons.Default.AutoAwesome,
                        title = if (language == Language.TAMIL) "பொதுப்பலன்" else "General Forecast",
                        text = if (language == Language.TAMIL) horoscope.generalTamil else horoscope.generalEnglish,
                        accentColor = GoldBright
                    )
                }

                // Career & Job
                item {
                    PredictionCard(
                        icon = Icons.Default.Work,
                        title = if (language == Language.TAMIL) "தொழில் & உத்தியோகம்" else "Career & Profession",
                        text = if (language == Language.TAMIL) horoscope.careerTamil else horoscope.careerEnglish,
                        accentColor = Color(0xFF2563EB)
                    )
                }

                // Finance & Wealth
                item {
                    PredictionCard(
                        icon = Icons.Default.Payments,
                        title = if (language == Language.TAMIL) "தனம் & வருமானம்" else "Finance & Wealth",
                        text = if (language == Language.TAMIL) horoscope.financeTamil else horoscope.financeEnglish,
                        accentColor = TulsiGreen
                    )
                }

                // Family & Relationships
                item {
                    PredictionCard(
                        icon = Icons.Default.Favorite,
                        title = if (language == Language.TAMIL) "குடும்பம் & இல்லறம்" else "Family & Harmony",
                        text = if (language == Language.TAMIL) horoscope.familyTamil else horoscope.familyEnglish,
                        accentColor = KumkumRed
                    )
                }

                // Health & Wellness
                item {
                    PredictionCard(
                        icon = Icons.Default.FitnessCenter,
                        title = if (language == Language.TAMIL) "உடல்நலம் & உற்சாகம்" else "Health & Vitality",
                        text = if (language == Language.TAMIL) horoscope.healthTamil else horoscope.healthEnglish,
                        accentColor = Color(0xFF0D9488)
                    )
                }

                // Lucky Traits Matrix Card
                item {
                    LuckyTraitsCard(
                        luckyNumber = horoscope.luckyNumber,
                        luckyColor = if (language == Language.TAMIL) horoscope.luckyColorTamil else horoscope.luckyColorEnglish,
                        luckyDirection = if (language == Language.TAMIL) horoscope.luckyDirectionTamil else horoscope.luckyDirectionEnglish,
                        gemstone = if (language == Language.TAMIL) rasi.gemStoneTamil else rasi.gemStoneEnglish,
                        nakshatras = if (language == Language.TAMIL) rasi.nakshatrasCoveredTamil else rasi.nakshatrasCoveredEnglish,
                        language = language
                    )
                }

                // Sacred Pariharam / Remedy
                item {
                    PariharamCard(
                        pariharamText = if (language == Language.TAMIL) horoscope.pariharamTamil else horoscope.pariharamEnglish,
                        language = language
                    )
                }

                // Share Call-To-Action Button
                item {
                    Button(
                        onClick = {
                            ShareHelper.shareHoroscope(
                                context = context,
                                rasiInfo = rasi,
                                horoscope = horoscope,
                                language = language,
                                tamilDate = horoscope.dateDisplayTamil
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("whatsapp_share_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TulsiGreen
                        )
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.TAMIL)
                                "வாட்ஸ்அப் மற்றும் சமூக வலைதளங்களில் பகிர்க"
                            else
                                "Share on WhatsApp & Social Media",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OverallRatingCard(
    score: Int,
    careerScore: Int,
    financeScore: Int,
    healthScore: Int,
    familyScore: Int,
    dateDisplay: String,
    language: Language
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == Language.TAMIL) "கிரக சாதக நிலை" else "Planetary Favorability",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dateDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = GoldBright
                    )
                }

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(GoldBright.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$score%",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = GoldBright
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Score meters
            ScoreMeterRow(label = if (language == Language.TAMIL) "தொழில் (Career)" else "Career", value = careerScore, color = Color(0xFF2563EB))
            Spacer(modifier = Modifier.height(6.dp))
            ScoreMeterRow(label = if (language == Language.TAMIL) "தனம் (Wealth)" else "Wealth", value = financeScore, color = TulsiGreen)
            Spacer(modifier = Modifier.height(6.dp))
            ScoreMeterRow(label = if (language == Language.TAMIL) "குடும்பம் (Family)" else "Family", value = familyScore, color = KumkumRed)
            Spacer(modifier = Modifier.height(6.dp))
            ScoreMeterRow(label = if (language == Language.TAMIL) "ஆரோக்கியம் (Health)" else "Health", value = healthScore, color = Color(0xFF0D9488))
        }
    }
}

@Composable
fun ScoreMeterRow(
    label: String,
    value: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.width(110.dp)
        )
        LinearProgressIndicator(
            progress = { value / 100f },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$value%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(36.dp)
        )
    }
}

@Composable
fun PredictionCard(
    icon: ImageVector,
    title: String,
    text: String,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun LuckyTraitsCard(
    luckyNumber: Int,
    luckyColor: String,
    luckyDirection: String,
    gemstone: String,
    nakshatras: String,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, GoldBright.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = "✨ " + if (language == Language.TAMIL) "அதிர்ஷ்ட அம்சங்கள்" else "Lucky Elements" + " ✨",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = GoldBright
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Lucky Number
                TraitItem(
                    title = if (language == Language.TAMIL) "அதிர்ஷ்ட எண்" else "Lucky No",
                    value = luckyNumber.toString(),
                    modifier = Modifier.weight(1f)
                )

                // Lucky Color
                TraitItem(
                    title = if (language == Language.TAMIL) "நிறம்" else "Color",
                    value = luckyColor,
                    modifier = Modifier.weight(1f)
                )

                // Lucky Direction
                TraitItem(
                    title = if (language == Language.TAMIL) "திசை" else "Direction",
                    value = luckyDirection,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Gemstone & Nakshatra
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = if (language == Language.TAMIL) "ரத்தினம்: $gemstone" else "Gemstone: $gemstone",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (language == Language.TAMIL) "நட்சத்திரங்கள்: $nakshatras" else "Stars: $nakshatras",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TraitItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PariharamCard(
    pariharamText: String,
    language: Language
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = TempleMaroon.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, TempleMaroon.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🪔", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == Language.TAMIL)
                        "இன்றைய பரிகாரம் & தெய்வ வழிபாடு"
                    else
                        "Remedial Prayer & Auspicious Deity",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TempleMaroon
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = pariharamText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )
        }
    }
}
