package com.example.cs381finalproject.data

import kotlinx.coroutines.flow.Flow

class GymRepository(private val workoutDao: WorkoutDao) {

    val allLogs: Flow<List<WorkoutLog>> = workoutDao.getAllLogs()

    suspend fun getLogById(id: Int): WorkoutLog? {
        return workoutDao.getLogById(id)
    }

    suspend fun insert(log: WorkoutLog) {
        workoutDao.insertLog(log)
    }


    suspend fun update(log: WorkoutLog) {
        workoutDao.update(log)
    }
    suspend fun delete(log: WorkoutLog) {
        workoutDao.deleteLog(log)
    }
}