package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfileEntity::class,
        CachedHoroscopeEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class RasiPalanDatabase : RoomDatabase() {
    abstract fun rasiDao(): RasiDao

    companion object {
        @Volatile
        private var INSTANCE: RasiPalanDatabase? = null

        fun getInstance(context: Context): RasiPalanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RasiPalanDatabase::class.java,
                    "rasi_palan_v3_db"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
