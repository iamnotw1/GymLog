package com.example.cs381finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.cs381finalproject.data.GymDatabase
import com.example.cs381finalproject.data.GymRepository
import com.example.cs381finalproject.ui.theme.GymAppNavigation
import com.example.cs381finalproject.ui.theme.GymLogTheme
import com.example.cs381finalproject.viewmodel.GymViewModel
import com.example.cs381finalproject.viewmodel.GymViewModelFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manual Dependency Injection for the Database and Repository
        val database = GymDatabase.getDatabase(applicationContext)
        val repository = GymRepository(database.workoutDao())

        setContent {
            GymLogTheme {
                // Initialize the ViewModel using the Factory we created
                val viewModel: GymViewModel = viewModel(
                    factory = GymViewModelFactory(repository)
                )

                // Launch the navigation host
                GymAppNavigation(viewModel = viewModel)
            }
        }
    }
}

