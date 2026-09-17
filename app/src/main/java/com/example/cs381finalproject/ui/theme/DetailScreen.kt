package com.example.cs381finalproject.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cs381finalproject.data.WorkoutLog
import com.example.cs381finalproject.viewmodel.GymViewModel

@Composable
fun DetailScreen(viewModel: GymViewModel, logId: Int, onNavigateBack: () -> Unit) {
    var log by remember { mutableStateOf<WorkoutLog?>(null) }

    // NEW: State to track if the user is currently editing
    var isEditing by remember { mutableStateOf(false) }

    // NEW: Temporary states to hold the text while editing
    var editWeight by remember { mutableStateOf("") }
    var editSets by remember { mutableStateOf("") }
    var editReps by remember { mutableStateOf("") }

    LaunchedEffect(logId) {
        val fetchedLog = viewModel.getLogDetails(logId)
        log = fetchedLog

        // Pre-fill the edit fields with the current data
        if (fetchedLog != null) {
            editWeight = fetchedLog.weight.toString()
            editSets = fetchedLog.sets.toString()
            editReps = fetchedLog.reps.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        log?.let { currentLog ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentLog.exerciseName,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isEditing) {
                        // --- EDIT MODE UI ---
                        OutlinedTextField(
                            value = editWeight,
                            onValueChange = { editWeight = it },
                            label = { Text("Weight (lbs)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editSets,
                            onValueChange = { editSets = it },
                            label = { Text("Sets") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editReps,
                            onValueChange = { editReps = it },
                            label = { Text("Reps") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // --- VIEW MODE UI ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Weight", style = MaterialTheme.typography.labelLarge)
                                Text(text = "${currentLog.weight} lbs", style = MaterialTheme.typography.titleLarge)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Sets", style = MaterialTheme.typography.labelLarge)
                                Text(text = "${currentLog.sets}", style = MaterialTheme.typography.titleLarge)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Reps", style = MaterialTheme.typography.labelLarge)
                                Text(text = "${currentLog.reps}", style = MaterialTheme.typography.titleLarge)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isEditing) {
                Button(
                    onClick = {
                        val updatedLog = currentLog.copy(
                            weight = editWeight.toIntOrNull() ?: currentLog.weight,
                            sets = editSets.toIntOrNull() ?: currentLog.sets,
                            reps = editReps.toIntOrNull() ?: currentLog.reps
                        )
                        viewModel.updateLog(updatedLog) // Update database
                        log = updatedLog // Update local screen state
                        isEditing = false // Exit edit mode
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
                        // Cancel editing and reset text fields to original values
                        editWeight = currentLog.weight.toString()
                        editSets = currentLog.sets.toString()
                        editReps = currentLog.reps.toString()
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            } else {

                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Record")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        viewModel.deleteLog(currentLog)
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete Record")
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Go Back")
                }
            }

        } ?: run {
            CircularProgressIndicator()
        }
    }
}