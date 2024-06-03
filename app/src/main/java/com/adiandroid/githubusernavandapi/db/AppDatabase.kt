package com.adiandroid.githubusernavandapi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteUser::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (Instance == null) {
                synchronized(AppDatabase::class) {
                    Instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "user_database"
                    ).build()
                }
            }
            return Instance
        }
    }

    abstract fun userFavoriteDao(): FavoriteUserDao
}

