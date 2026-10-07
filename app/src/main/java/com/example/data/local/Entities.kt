package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val savedRasiId: Int = 0, // default Mesham
    val savedNakshatraId: Int = 0, // Ashwini
    val savedPada: Int = 1,
    val language: String = "TAMIL",
    val themeMode: String = "SYSTEM",
    val notificationsEnabled: Boolean = true,
    val alertHour: Int = 7,
    val alertMinute: Int = 0,
    val lastViewedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "cached_horoscopes")
data class CachedHoroscopeEntity(
    @PrimaryKey val cacheKey: String, // e.g. "MESHAM_2026-10-07"
    val rasiIndex: Int,
    val dateKey: String,
    val dateDisplayTamil: String,
    val dateDisplayEnglish: String,
    val overallPercentage: Int,
    val careerScore: Int,
    val financeScore: Int,
    val healthScore: Int,
    val familyScore: Int,
    val generalTamil: String,
    val generalEnglish: String,
    val careerTamil: String,
    val careerEnglish: String,
    val financeTamil: String,
    val financeEnglish: String,
    val healthTamil: String,
    val healthEnglish: String,
    val familyTamil: String,
    val familyEnglish: String,
    val luckyNumber: Int,
    val luckyColorTamil: String,
    val luckyColorEnglish: String,
    val luckyDirectionTamil: String,
    val luckyDirectionEnglish: String,
    val pariharamTamil: String,
    val pariharamEnglish: String,
    val cachedTimestamp: Long = System.currentTimeMillis()
)
