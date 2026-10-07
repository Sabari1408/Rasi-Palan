package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.data.model.Language
import com.example.data.model.ThemeMode
import com.example.ui.components.GoogleAdBanner
import com.example.ui.screens.*
import com.example.ui.theme.GoldBright
import com.example.ui.theme.TamilRasiPalanTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.RasiViewModel
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: RasiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Google Mobile Ads SDK asynchronously in background
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                MobileAds.initialize(this@MainActivity) {}
            } catch (t: Throwable) {
                // Graceful fallback
            }
        }

        setContent {
            val themeMode by viewModel.themeMode.collectAsState()
            val isDark = when (themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            TamilRasiPalanTheme(darkTheme = isDark) {
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(viewModel: RasiViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val language by viewModel.language.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Request notification permission on Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentScreen !is AppScreen.RasiDetail) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = if (language == Language.TAMIL) "தமிழ் ராசி பலன்" else "Tamil Rasi Palan",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (language == Language.TAMIL)
                                    "தினசரி பலன்கள் & பஞ்சாங்கம்"
                                else
                                    "Daily Horoscope & Panchangam",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        // Language switcher button
                        FilledTonalButton(
                            onClick = {
                                viewModel.setLanguage(if (language == Language.TAMIL) Language.ENGLISH else Language.TAMIL)
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .testTag("top_bar_lang_toggle")
                        ) {
                            Text(
                                text = if (language == Language.TAMIL) "English" else "தமிழ்",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Test Notification Trigger with Om sound
                        IconButton(
                            onClick = { viewModel.triggerTestNotification() },
                            modifier = Modifier.testTag("top_bar_notification_icon")
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsNone,
                                contentDescription = "Alerts with Om Sound",
                                tint = GoldBright
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            Column {
                // Persistent Google AdMob Banner for revenue generation
                GoogleAdBanner()

                NavigationBar(
                    modifier = Modifier.testTag("bottom_nav_bar"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    // Dashboard tab
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.Dashboard,
                        onClick = { viewModel.navigateTo(AppScreen.Dashboard) },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                        label = { Text(if (language == Language.TAMIL) "ராசி பலன்" else "Horoscope", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_dashboard")
                    )

                    // Panchangam tab
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.Panchangam,
                        onClick = { viewModel.navigateTo(AppScreen.Panchangam) },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Panchangam") },
                        label = { Text(if (language == Language.TAMIL) "பஞ்சாங்கம்" else "Panchangam", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_panchangam")
                    )

                    // My Profile tab
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.Profile,
                        onClick = { viewModel.navigateTo(AppScreen.Profile) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "My Sign") },
                        label = { Text(if (language == Language.TAMIL) "என் ராசி" else "My Sign", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_profile")
                    )

                    // Settings tab
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.Settings,
                        onClick = { viewModel.navigateTo(AppScreen.Settings) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text(if (language == Language.TAMIL) "அமைப்புகள்" else "Settings", fontSize = 11.sp) },
                        modifier = Modifier.testTag("nav_settings")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is AppScreen.Dashboard -> {
                    DashboardScreen(viewModel = viewModel)
                }
                is AppScreen.RasiDetail -> {
                    RasiDetailScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateTo(AppScreen.Dashboard) }
                    )
                }
                is AppScreen.Panchangam -> {
                    PanchangamScreen(viewModel = viewModel)
                }
                is AppScreen.Profile -> {
                    ProfileScreen(viewModel = viewModel)
                }
                is AppScreen.Settings -> {
                    SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
