package com.example.cs381finalproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // Returning a Flow here means Room automatically pushes updates to the ViewModel
    @Query("SELECT * FROM workout_logs")
    fun getAllLogs(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs WHERE id = :logId")
    suspend fun getLogById(logId: Int): WorkoutLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: WorkoutLog)

    @Update
    suspend fun update(log: WorkoutLog)

    @Delete
    suspend fun deleteLog(log: WorkoutLog)
}
