package com.example.ciclomultfood.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ciclomultfood.data.dao.CommunityPostDao
import com.example.ciclomultfood.data.dao.ListingDao
import com.example.ciclomultfood.data.dao.LocalMarketDao
import com.example.ciclomultfood.data.dao.UserPreferencesDao
import com.example.ciclomultfood.data.model.CommunityPostEntity
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ciclomultfood.data.model.LocalMarketEntity
import com.example.ciclomultfood.data.model.UserPreferencesEntity

@Database(
    entities = [ListingEntity::class, CommunityPostEntity::class, UserPreferencesEntity::class, LocalMarketEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listingDao(): ListingDao
    abstract fun communityPostDao(): CommunityPostDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    abstract fun localMarketDao(): LocalMarketDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ciclomult_food_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
