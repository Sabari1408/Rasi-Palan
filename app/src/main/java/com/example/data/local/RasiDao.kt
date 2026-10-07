package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RasiDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM cached_horoscopes WHERE cacheKey = :key LIMIT 1")
    fun getCachedHoroscope(key: String): Flow<CachedHoroscopeEntity?>

    @Query("SELECT * FROM cached_horoscopes WHERE rasiIndex = :rasiIndex")
    fun getCachedForRasi(rasiIndex: Int): Flow<List<CachedHoroscopeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoroscopes(list: List<CachedHoroscopeEntity>)
}
