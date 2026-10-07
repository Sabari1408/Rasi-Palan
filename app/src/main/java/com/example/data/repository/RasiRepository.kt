package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class RasiRepository(
    private val rasiDao: RasiDao,
    private val geminiRasiRepository: GeminiRasiRepository? = null,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getUserProfile(): Flow<UserProfileEntity?> = rasiDao.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfileEntity) = withContext(ioDispatcher) {
        rasiDao.saveUserProfile(profile)
    }

    fun getAllRasis(): List<RasiInfo> = AstrologyDataProvider.allRasis

    fun getRasiById(rasiId: RasiId): RasiInfo {
        return AstrologyDataProvider.allRasis.firstOrNull { it.id == rasiId }
            ?: AstrologyDataProvider.allRasis.first()
    }

    fun getAllNakshatras(): List<NakshatraInfo> = AstrologyDataProvider.allNakshatras

    fun getPanchangam(calendar: Calendar = Calendar.getInstance()): PanchangamData =
        AstrologyDataProvider.getPanchangamForDate(calendar)

    fun getHoroscope(rasiId: RasiId, calendar: Calendar, forceRefresh: Boolean = false): Flow<HoroscopeItem> {
        if (geminiRasiRepository != null) {
            return geminiRasiRepository.fetchRasiPalanForDate(rasiId, calendar, forceRefresh)
        }

        return flow {
            val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(calendar.time)
            val cacheKey = "${rasiId.name}_$dateKey"

            // Check local database cache
            val cached = rasiDao.getCachedHoroscope(cacheKey).firstOrNull()
            if (cached != null && !forceRefresh) {
                emit(cached.toDomainModel(rasiId))
            } else {
                val item = AstrologyDataProvider.generateHoroscopeForRasi(rasiId, calendar)
                rasiDao.insertHoroscopes(listOf(item.toEntity(cacheKey)))
                emit(item)
            }
        }.flowOn(ioDispatcher)
    }

    suspend fun preloadAndCacheTodayHoroscopes() = withContext(ioDispatcher) {
        val today = Calendar.getInstance()
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(today.time)
        val entities = mutableListOf<CachedHoroscopeEntity>()
        for (rasi in AstrologyDataProvider.allRasis) {
            val cacheKey = "${rasi.id.name}_$dateKey"
            val item = AstrologyDataProvider.generateHoroscopeForRasi(rasi.id, today)
            entities.add(item.toEntity(cacheKey))
        }
        rasiDao.insertHoroscopes(entities)
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
