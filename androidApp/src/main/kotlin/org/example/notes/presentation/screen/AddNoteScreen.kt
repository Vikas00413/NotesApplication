package org.example.notes.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.notes.data.local.entity.Priority
import org.example.notes.presentation.intent.AddNoteIntent
import org.example.notes.presentation.viewmodel.AddNoteViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteScreen(
    onBack: () -> Unit,
    viewModel: AddNoteViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = "Add Note")

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.title,
            onValueChange = { viewModel.onEvent(AddNoteIntent.OnTitleChange(it)) },
            label = { Text(text = "Title") },
            singleLine = true,
        )

        PrioritySelector(
            selectedPriority = state.priority,
            onPrioritySelected = { viewModel.onEvent(AddNoteIntent.OnPriorityChange(it)) },
        )

        state.error?.let { error ->
            Text(text = error)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TextButton(onClick = onBack) {
                Text(text = "Cancel")
            }

            Button(
                onClick = { viewModel.onEvent(AddNoteIntent.SaveNote) },
                enabled = !state.isLoading,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                } else {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Composable
private fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Priority.entries.forEach { priority ->
            TextButton(onClick = { onPrioritySelected(priority) }) {
                Text(text = priority.name)
            }
        }
    }
}
