package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Language
import com.example.ui.components.DateSelectorBar
import com.example.ui.components.PanchangamSnapshotCard
import com.example.ui.components.RasiGridCard
import com.example.ui.components.UserSavedRasiHeroCard
import com.example.ui.theme.GoldBright
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.RasiViewModel

@Composable
fun DashboardScreen(
    viewModel: RasiViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val panchangam by viewModel.panchangam.collectAsState()
    val selectedCalendar by viewModel.selectedCalendar.collectAsState()
    val isOfflineMode by viewModel.isOfflineMode.collectAsState()

    val savedRasiInfo = viewModel.getSavedRasiInfo()
    val savedNakshatraInfo = viewModel.getSavedNakshatraInfo()
    val allRasis = com.example.data.repository.AstrologyDataProvider.allRasis
    val isToday = viewModel.isTodaySelected()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_grid"),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Offline Status Warning Pill
        if (isOfflineMode) {
            item(span = { GridItemSpan(2) }) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("offline_indicator_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.WifiOff,
                            contentDescription = "Offline",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.TAMIL)
                                "ஆஃப்லைன் பயன்முறை: கேச் செய்யப்பட்ட ராசி பலன் செயல்படுகிறது"
                            else
                                "Offline Mode: Displaying locally cached horoscopes",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        // Hero Devotional Banner Artwork
        item(span = { GridItemSpan(2) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("header_banner_card"),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.rasi_banner_header_1791397145432),
                        contentDescription = "Astrology Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.35f),
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🕉️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == Language.TAMIL) "சுப தினம் • நலம் தரும் ஜோதிடம்" else "Auspicious Daily Astrology",
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldBright,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = if (language == Language.TAMIL)
                                "தினசரி ராசி பலன் & பஞ்சாங்கம்"
                            else
                                "Daily Horoscope & Panchangam",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = panchangam.tamilDate + " | " + panchangam.dateString,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        // Interactive Date Selector Bar (Today by default; pick past or future dates)
        item(span = { GridItemSpan(2) }) {
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

        // User's Saved Rasi Hero Card
        item(span = { GridItemSpan(2) }) {
            UserSavedRasiHeroCard(
                rasi = savedRasiInfo,
                nakshatra = savedNakshatraInfo,
                pada = userProfile.savedPada,
                language = language,
                onViewHoroscope = {
                    viewModel.navigateTo(AppScreen.RasiDetail(savedRasiInfo.id))
                },
                onChangeProfile = {
                    viewModel.navigateTo(AppScreen.Profile)
                }
            )
        }

        // Panchangam Snapshot Card
        item(span = { GridItemSpan(2) }) {
            PanchangamSnapshotCard(
                panchangam = panchangam,
                language = language,
                onViewFullPanchangam = {
                    viewModel.navigateTo(AppScreen.Panchangam)
                }
            )
        }

        // Section Title: 12 Rasis
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⭐", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.TAMIL) "12 ராசிகள் (அனைத்து பலன்கள்)" else "12 Zodiac Signs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = if (language == Language.TAMIL) "தொட்டுப் பார்க்கவும்" else "Tap to view",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // All 12 Rasi Grid Cards
        items(allRasis, key = { it.id.name }) { rasi ->
            RasiGridCard(
                rasi = rasi,
                language = language,
                isUserRasi = rasi.id.index == userProfile.savedRasiId,
                onClick = {
                    viewModel.navigateTo(AppScreen.RasiDetail(rasi.id))
                }
            )
        }

        // Auspicious quote card
        item(span = { GridItemSpan(2) }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🪔 " + if (language == Language.TAMIL) "இன்றைய சுப சிந்தனை" else "Auspicious Daily Thought" + " 🪔",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == Language.TAMIL)
                            panchangam.auspiciousThoughtTamil
                        else
                            panchangam.auspiciousThoughtEnglish,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
