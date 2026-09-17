package com.example.cs381finalproject.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorkoutLog::class], version = 1, exportSchema = false)
abstract class GymDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var Instance: GymDatabase? = null

        fun getDatabase(context: Context): GymDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, GymDatabase::class.java, "gym_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}