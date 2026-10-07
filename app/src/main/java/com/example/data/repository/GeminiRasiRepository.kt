package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.local.CachedHoroscopeEntity
import com.example.data.local.RasiDao
import com.example.data.model.HoroscopeItem
import com.example.data.model.RasiId
import com.example.data.model.RasiInfo
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class GeminiRasiRepository(
    private val rasiDao: RasiDao,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val tag = "GeminiRasiRepository"
    private val modelName = "gemini-2.5-flash"

    /**
     * Fetches daily Rasi Palan content for a specific date using Gemini via Firebase Vertex AI (Firebase AI SDK).
     * If cached locally in Room, emits cached data first.
     * If network/API call succeeds, updates the Room database cache and emits the fresh AI prediction.
     * If offline or error occurs, falls back to the deterministic astrological engine.
     */
    fun fetchRasiPalanForDate(
        rasiId: RasiId,
        calendar: Calendar,
        forceRefresh: Boolean = false
    ): Flow<HoroscopeItem> = flow {
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time)
        val cacheKey = "${rasiId.name}_$dateKey"

        // 1. Check local Room database cache
        val cached = rasiDao.getCachedHoroscope(cacheKey).firstOrNull()
        if (cached != null && !forceRefresh) {
            emit(cached.toDomainModel(rasiId))
            return@flow
        }

        // 2. Fetch via Gemini API using Firebase Vertex AI
        val aiHoroscope = try {
            generateHoroscopeWithGemini(rasiId, calendar, dateKey)
        } catch (e: Exception) {
            Log.w(tag, "Firebase Vertex AI unavailable or error: ${e.message}, falling back to local provider", e)
            null
        }

        val resultItem = if (aiHoroscope != null) {
            // Save Gemini-generated horoscope into Room Database cache
            rasiDao.insertHoroscopes(listOf(aiHoroscope.toEntity(cacheKey)))
            aiHoroscope
        } else {
            // Fallback to local astrological engine
            val fallback = AstrologyDataProvider.generateHoroscopeForRasi(rasiId, calendar)
            rasiDao.insertHoroscopes(listOf(fallback.toEntity(cacheKey)))
            fallback
        }

        emit(resultItem)
    }.flowOn(ioDispatcher)

    private suspend fun generateHoroscopeWithGemini(
        rasiId: RasiId,
        calendar: Calendar,
        dateKey: String
    ): HoroscopeItem? = withContext(ioDispatcher) {
        // Ensure FirebaseApp is initialized with options if not already initialized
        if (FirebaseApp.getApps(context).isEmpty()) {
            try {
                FirebaseApp.initializeApp(context)
            } catch (e: Exception) {
                Log.w(tag, "FirebaseApp.initializeApp failed or no google-services.json available: ${e.message}")
            }
        }
        if (FirebaseApp.getApps(context).isEmpty()) {
            // Cannot use Firebase Vertex AI without configured FirebaseApp, return null to use local astrological engine
            return@withContext null
        }

        val rasi = AstrologyDataProvider.allRasis.firstOrNull { it.id == rasiId }
            ?: AstrologyDataProvider.allRasis.first()
        val panchang = AstrologyDataProvider.getPanchangamForDate(calendar)

        val prompt = buildAstrologyPrompt(rasi, panchang, dateKey)

        val config = generationConfig {
            responseMimeType = "application/json"
            temperature = 0.7f
        }

        val generativeModel = Firebase.ai.generativeModel(
            modelName = modelName,
            generationConfig = config
        )

        val response = generativeModel.generateContent(prompt)
        val responseText = response.text ?: return@withContext null

        parseGeminiResponse(responseText, rasiId, dateKey, panchang.tamilDate, panchang.dateString, rasi)
    }

    private fun buildAstrologyPrompt(
        rasi: RasiInfo,
        panchang: com.example.data.model.PanchangamData,
        dateKey: String
    ): String {
        return """
            You are an expert Tamil Vedic Astrologer providing authentic daily Rasi Palan (இன்றைய ராசி பலன்) for Tamil Nadu devotees.
            Generate the daily horoscope for:
            - Rasi: ${rasi.nameTamil} (${rasi.nameEnglish}, Symbol: ${rasi.symbolEmoji})
            - Ruling Planet (அதிபதி): ${rasi.lordTamil} (${rasi.lordEnglish})
            - Element (பூதம்): ${rasi.elementTamil}
            - Date: $dateKey (${panchang.tamilDate}, ${panchang.dateString})
            - Thithi: ${panchang.thithi}
            - Star: ${panchang.nakshatram}

            Respond strictly in valid JSON matching this exact structure:
            {
              "overallPercentage": <integer 65 to 98>,
              "careerScore": <integer 60 to 98>,
              "financeScore": <integer 60 to 98>,
              "healthScore": <integer 60 to 98>,
              "familyScore": <integer 60 to 98>,
              "generalTamil": "<In-depth auspicious prediction in pure Tamil>",
              "generalEnglish": "<Clear English forecast>",
              "careerTamil": "<Detailed professional/job forecast in Tamil>",
              "careerEnglish": "<Professional forecast in English>",
              "financeTamil": "<Wealth & expenditure forecast in Tamil>",
              "financeEnglish": "<Finance forecast in English>",
              "healthTamil": "<Health & wellness advice in Tamil>",
              "healthEnglish": "<Health advice in English>",
              "familyTamil": "<Family, relationship & domestic forecast in Tamil>",
              "familyEnglish": "<Family forecast in English>",
              "luckyNumber": <integer 1 to 9>,
              "luckyColorTamil": "<Auspicious color in Tamil, e.g. சிகப்பு, மஞ்சள்>",
              "luckyColorEnglish": "<Auspicious color in English>",
              "luckyDirectionTamil": "<Auspicious direction in Tamil, e.g. கிழக்கு, வடகிழக்கு>",
              "luckyDirectionEnglish": "<Direction in English>",
              "pariharamTamil": "<Traditional devotional remedy/prayer to Tamil deity in Tamil>",
              "pariharamEnglish": "<Remedial prayer in English>"
            }
        """.trimIndent()
    }

    private fun parseGeminiResponse(
        jsonString: String,
        rasiId: RasiId,
        dateKey: String,
        dateDisplayTamil: String,
        dateDisplayEnglish: String,
        rasi: RasiInfo
    ): HoroscopeItem {
        val cleanJson = jsonString.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
        val json = JSONObject(cleanJson)

        return HoroscopeItem(
            rasiId = rasiId,
            dateKey = dateKey,
            dateDisplayTamil = dateDisplayTamil,
            dateDisplayEnglish = dateDisplayEnglish,
            overallPercentage = json.optInt("overallPercentage", 80),
            careerScore = json.optInt("careerScore", 82),
            financeScore = json.optInt("financeScore", 78),
            healthScore = json.optInt("healthScore", 85),
            familyScore = json.optInt("familyScore", 84),
            generalTamil = json.optString("generalTamil", "${rasi.nameTamil} ராசி அன்பர்களுக்கு நன்மையான நாள்."),
            generalEnglish = json.optString("generalEnglish", "Favorable planetary transits for ${rasi.nameEnglish} natives."),
            careerTamil = json.optString("careerTamil", "தொழில் மற்றும் பணியிடத்தில் நன்மைகள் கூடும்."),
            careerEnglish = json.optString("careerEnglish", "Positive developments in career and business."),
            financeTamil = json.optString("financeTamil", "பணவரவு திருப்திகரமாக இருக்கும்."),
            financeEnglish = json.optString("financeEnglish", "Steady and reassuring financial flow."),
            healthTamil = json.optString("healthTamil", "ஆரோக்கியத்தில் புத்துணர்ச்சி காணப்படும்."),
            healthEnglish = json.optString("healthEnglish", "Good physical and mental vitality."),
            familyTamil = json.optString("familyTamil", "குடும்பத்தில் அமைதியும் மகிழ்ச்சியும் நிலவும்."),
            familyEnglish = json.optString("familyEnglish", "Peace and mutual affection in family life."),
            luckyNumber = json.optInt("luckyNumber", rasi.defaultLuckyNumber),
            luckyColorTamil = json.optString("luckyColorTamil", rasi.defaultLuckyColorTamil),
            luckyColorEnglish = json.optString("luckyColorEnglish", rasi.defaultLuckyColorEnglish),
            luckyDirectionTamil = json.optString("luckyDirectionTamil", "வடகிழக்கு"),
            luckyDirectionEnglish = json.optString("luckyDirectionEnglish", "North-East"),
            pariharamTamil = json.optString("pariharamTamil", "இஷ்ட தெய்வத்தை வழிபட்டு நற்காரியங்களை தொடங்கவும்."),
            pariharamEnglish = json.optString("pariharamEnglish", "Offer morning prayers to family deity for blessings.")
        )
    }

    private fun HoroscopeItem.toEntity(cacheKey: String): CachedHoroscopeEntity {
        return CachedHoroscopeEntity(
            cacheKey = cacheKey,
            rasiIndex = rasiId.index,
            dateKey = dateKey,
            dateDisplayTamil = dateDisplayTamil,
            dateDisplayEnglish = dateDisplayEnglish,
            overallPercentage = overallPercentage,
            careerScore = careerScore,
            financeScore = financeScore,
            healthScore = healthScore,
            familyScore = familyScore,
            generalTamil = generalTamil,
            generalEnglish = generalEnglish,
            careerTamil = careerTamil,
            careerEnglish = careerEnglish,
            financeTamil = financeTamil,
            financeEnglish = financeEnglish,
            healthTamil = healthTamil,
            healthEnglish = healthEnglish,
            familyTamil = familyTamil,
            familyEnglish = familyEnglish,
            luckyNumber = luckyNumber,
            luckyColorTamil = luckyColorTamil,
            luckyColorEnglish = luckyColorEnglish,
            luckyDirectionTamil = luckyDirectionTamil,
            luckyDirectionEnglish = luckyDirectionEnglish,
            pariharamTamil = pariharamTamil,
            pariharamEnglish = pariharamEnglish
        )
    }

    private fun CachedHoroscopeEntity.toDomainModel(rasiId: RasiId): HoroscopeItem {
        return HoroscopeItem(
            rasiId = rasiId,
            dateKey = dateKey,
            dateDisplayTamil = dateDisplayTamil,
            dateDisplayEnglish = dateDisplayEnglish,
            overallPercentage = overallPercentage,
            careerScore = careerScore,
            financeScore = financeScore,
            healthScore = healthScore,
            familyScore = familyScore,
            generalTamil = generalTamil,
            generalEnglish = generalEnglish,
            careerTamil = careerTamil,
            careerEnglish = careerEnglish,
            financeTamil = financeTamil,
            financeEnglish = financeEnglish,
            healthTamil = healthTamil,
            healthEnglish = healthEnglish,
            familyTamil = familyTamil,
            familyEnglish = familyEnglish,
            luckyNumber = luckyNumber,
            luckyColorTamil = luckyColorTamil,
            luckyColorEnglish = luckyColorEnglish,
            luckyDirectionTamil = luckyDirectionTamil,
            luckyDirectionEnglish = luckyDirectionEnglish,
            pariharamTamil = pariharamTamil,
            pariharamEnglish = pariharamEnglish
        )
    }
}
