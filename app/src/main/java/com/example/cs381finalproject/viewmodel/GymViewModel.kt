package com.example.cs381finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cs381finalproject.data.GymRepository
import com.example.cs381finalproject.data.WorkoutLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class GymViewModel(private val repository: GymRepository) : ViewModel() {

    private val _logs = MutableStateFlow<List<WorkoutLog>>(emptyList())
    val logs: StateFlow<List<WorkoutLog>> = _logs.asStateFlow()

    // NEW: Track the currently selected date
    private val _selectedDate = MutableStateFlow(LocalDate.now().toString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allLogs.collect { logList ->
                _logs.value = logList
            }
        }
    }

    // NEW: Update the date when the user picks a new one on the calendar
    fun updateSelectedDate(newDate: String) {
        _selectedDate.value = newDate
    }

    // NEW: Handles the 'Update' requirement of CRUD
    fun updateLog(log: WorkoutLog) {
        viewModelScope.launch {
            repository.update(log)
        }
    }
    fun addLog(exercise: String, weight: Int, sets: Int, reps: Int, date: String) {
        viewModelScope.launch {
            repository.insert(WorkoutLog(
                exerciseName = exercise,
                weight = weight,
                sets = sets, // NEW
                reps = reps,
                date = date
            ))
        }
    }

    fun deleteLog(log: WorkoutLog) {
        viewModelScope.launch {
            repository.delete(log)
        }
    }

    suspend fun getLogDetails(id: Int): WorkoutLog? {
        return repository.getLogById(id)
    }
}

class GymViewModelFactory(private val repository: GymRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GymViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GymViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}