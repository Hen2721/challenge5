package com.example.challengechapter5.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.challengechapter5.Database.local.UserDAO
import com.example.challengechapter5.Database.local.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = true)
abstract class DatabaseUser : RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        private const val DB_NAME = "MyDoctor.db"

        @Volatile
        private var INSTANCE: DatabaseUser? = null

        fun getInstance(context: Context): DatabaseUser {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): DatabaseUser {
            return Room.databaseBuilder(context, DatabaseUser::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}