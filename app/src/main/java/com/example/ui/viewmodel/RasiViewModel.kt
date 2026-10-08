package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.RasiPalanDatabase
import com.example.data.local.UserProfileEntity
import com.example.data.model.*
import com.example.data.repository.AstrologyDataProvider
import com.example.data.repository.GeminiRasiRepository
import com.example.data.repository.RasiRepository
import com.example.ui.components.AlertScheduler
import com.example.ui.components.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

sealed class AppScreen {
    object Dashboard : AppScreen()
    data class RasiDetail(val rasiId: RasiId) : AppScreen()
    object Panchangam : AppScreen()
    object Profile : AppScreen()
    object Settings : AppScreen()
}

class RasiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RasiRepository

    init {
        val db = RasiPalanDatabase.getInstance(application)
        val geminiRepo = GeminiRasiRepository(db.rasiDao(), application)
        repository = RasiRepository(db.rasiDao(), geminiRepo)
        NotificationHelper.createNotificationChannel(application)
    }

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Dashboard)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfileEntity(
            savedRasiId = 0,
            savedNakshatraId = 0,
            savedPada = 1,
            language = "TAMIL",
            themeMode = "SYSTEM",
            notificationsEnabled = true
        )
    )
    val userProfile: StateFlow<UserProfileEntity> = _userProfile.asStateFlow()

    private val _language = MutableStateFlow(Language.TAMIL)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _selectedRasi = MutableStateFlow(AstrologyDataProvider.allRasis.first())
    val selectedRasi: StateFlow<RasiInfo> = _selectedRasi.asStateFlow()

    // Date selection - defaults to Today!
    private val _selectedCalendar = MutableStateFlow(Calendar.getInstance())
    val selectedCalendar: StateFlow<Calendar> = _selectedCalendar.asStateFlow()

    private val _currentHoroscope = MutableStateFlow<HoroscopeItem?>(null)
    val currentHoroscope: StateFlow<HoroscopeItem?> = _currentHoroscope.asStateFlow()

    private val _panchangam = MutableStateFlow(repository.getPanchangam(Calendar.getInstance()))
    val panchangam: StateFlow<PanchangamData> = _panchangam.asStateFlow()

    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        // Observe profile from Room DB
        viewModelScope.launch {
            repository.getUserProfile().collect { profile ->
                if (profile != null) {
                    _userProfile.value = profile
                    _language.value = if (profile.language == "ENGLISH") Language.ENGLISH else Language.TAMIL
                    _themeMode.value = when (profile.themeMode) {
                        "DARK" -> ThemeMode.DARK
                        "LIGHT" -> ThemeMode.LIGHT
                        else -> ThemeMode.SYSTEM
                    }
                    AlertScheduler.sync(application, profile.notificationsEnabled, profile.alertHour, profile.alertMinute)
                } else {
                    // No saved profile yet: the UI default is "alerts on at 07:00".
                    AlertScheduler.sync(application, true, 7, 0)
                }
            }
        }

        // Load today's horoscope (Gemini first, local fallback). No pre-seeding of the cache,
        // otherwise the generic local text would overwrite/block the Gemini result.
        loadHoroscope(_selectedRasi.value.id, _selectedCalendar.value)
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
        if (screen is AppScreen.RasiDetail) {
            selectRasi(screen.rasiId)
        }
    }

    fun selectRasi(rasiId: RasiId) {
        val rasi = repository.getRasiById(rasiId)
        _selectedRasi.value = rasi
        loadHoroscope(rasiId, _selectedCalendar.value)
    }

    fun selectDate(calendar: Calendar) {
        _selectedCalendar.value = calendar
        _panchangam.value = repository.getPanchangam(calendar)
        loadHoroscope(_selectedRasi.value.id, calendar)
    }

    fun goToToday() {
        selectDate(Calendar.getInstance())
    }

    fun goToPreviousDay() {
        val newCal = (_selectedCalendar.value.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        selectDate(newCal)
    }

    fun goToNextDay() {
        val newCal = (_selectedCalendar.value.clone() as Calendar).apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }
        selectDate(newCal)
    }

    fun isTodaySelected(): Boolean {
        val today = Calendar.getInstance()
        val current = _selectedCalendar.value
        return today.get(Calendar.YEAR) == current.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)
    }

    private var horoscopeJob: Job? = null

    private fun loadHoroscope(rasiId: RasiId, calendar: Calendar, forceRefresh: Boolean = false) {
        // Cancel any in-flight load so a slow earlier response can't overwrite a newer selection.
        horoscopeJob?.cancel()
        horoscopeJob = viewModelScope.launch {
            repository.getHoroscope(rasiId, calendar, forceRefresh, allowAi = !_isOfflineMode.value)
                .collect { item -> _currentHoroscope.value = item }
        }
    }

    fun refreshWithGeminiAi() {
        _toastMessage.value = if (_language.value == Language.TAMIL)
            "Gemini AI மூலம் ராசி பலன் கணிக்கப்படுகிறது..."
        else
            "Fetching Gemini AI horoscope..."
        loadHoroscope(_selectedRasi.value.id, _selectedCalendar.value, forceRefresh = true)
    }

    fun saveProfile(rasiId: RasiId, nakshatraId: Int, pada: Int, notificationsEnabled: Boolean) {
        viewModelScope.launch {
            val updated = _userProfile.value.copy(
                savedRasiId = rasiId.index,
                savedNakshatraId = nakshatraId,
                savedPada = pada,
                notificationsEnabled = notificationsEnabled,
                language = _language.value.name,
                themeMode = _themeMode.value.name
            )
            repository.saveUserProfile(updated)
            _userProfile.value = updated
            _toastMessage.value = if (_language.value == Language.TAMIL)
                "ராசி & நட்சத்திர விவரங்கள் சேமிக்கப்பட்டன!"
            else
                "Rasi & Nakshatra profile saved successfully!"
        }
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
        viewModelScope.launch {
            val updated = _userProfile.value.copy(language = lang.name)
            repository.saveUserProfile(updated)
            _userProfile.value = updated
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        viewModelScope.launch {
            val updated = _userProfile.value.copy(themeMode = mode.name)
            repository.saveUserProfile(updated)
            _userProfile.value = updated
        }
    }

    fun toggleOfflineMode() {
        _isOfflineMode.value = !_isOfflineMode.value
        loadHoroscope(_selectedRasi.value.id, _selectedCalendar.value)
        _toastMessage.value = if (_isOfflineMode.value)
            "ஆஃப்லைன் பயன்முறை ஆன் செய்யப்பட்டது (கேச் தரவு)"
        else
            "ஆன்லைன் பயன்முறை இயக்கப்பட்டது"
    }

    fun triggerTestNotification() {
        val userRasi = repository.getRasiById(RasiId.fromIndex(_userProfile.value.savedRasiId))
        val current = _currentHoroscope.value ?: AstrologyDataProvider.generateHoroscopeForRasi(userRasi.id, Calendar.getInstance())
        NotificationHelper.sendPersonalizedDailyNotification(
            getApplication(),
            userRasi.nameTamil,
            userRasi.nameEnglish,
            current.luckyNumber,
            current.luckyColorTamil,
            current.generalTamil.take(120) + "..."
        )
        _toastMessage.value = "🕉️ ஓம் ஒலியுடன் அறிவிப்பு அனுப்பப்பட்டது! (Sent with Om Sound)"
    }

    fun triggerAuspiciousTimingAlert() {
        val panchang = _panchangam.value
        NotificationHelper.sendAuspiciousTimingNotification(
            getApplication(),
            panchang.nallaNeramMorning,
            panchang.thithi
        )
        _toastMessage.value = "🕉️ ஓம் ஒலியுடன் சுப நேர அறிவிப்பு அனுப்பப்பட்டது!"
    }

    fun playOmSoundOnly() {
        NotificationHelper.playSacredOmSound(getApplication())
        _toastMessage.value = "🕉️ ஓம் சப்தம் (Sacred Om Sound)"
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun getSavedRasiInfo(): RasiInfo {
        return repository.getRasiById(RasiId.fromIndex(_userProfile.value.savedRasiId))
    }

    fun getSavedNakshatraInfo(): NakshatraInfo {
        val list = repository.getAllNakshatras()
        return list.firstOrNull { it.id == _userProfile.value.savedNakshatraId } ?: list.first()
    }
}
