package com.dicoding.asclepius.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.data.local.dao.UserHistoryDao
import com.dicoding.asclepius.data.model.UserActivity

@Database(entities = [UserActivity::class], version = 7, exportSchema = false)
abstract class UserHistoryDatabase : RoomDatabase() {

    abstract fun historyDataSource(): UserHistoryDao

    companion object {
        @Volatile
        private var singletonInstance: UserHistoryDatabase? = null

        @JvmStatic
        fun getDatabaseInstance(context: Context): UserHistoryDatabase {
            return singletonInstance ?: synchronized(this) {
                singletonInstance ?: buildDatabaseInstance(context).also { singletonInstance = it }
            }
        }

        private fun buildDatabaseInstance(context: Context): UserHistoryDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                UserHistoryDatabase::class.java, DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }

        private const val DB_NAME = "user_history_db"
    }
}