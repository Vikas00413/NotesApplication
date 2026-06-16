package org.example.notes.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.notes.domain.model.Notes
import org.example.notes.presentation.intent.NotesListIntent
import org.example.notes.presentation.viewmodel.GetNotesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotesListScreen(
    onClickDetail: (Notes) -> Unit,
    onEditNote: (Notes) -> Unit,
    onDeleteNote: (Notes) -> Unit,
    onAddNote: () -> Unit,
    viewModel: GetNotesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(NotesListIntent.LoadNotes)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (state.notes.isNotEmpty()) {
                FloatingActionButton(onClick = onAddNote) {
                    Text(text = "+")
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when {
                state.isLoading && state.notes.isEmpty() -> {
                    CircularProgressIndicator()
                }

                state.error != null && state.notes.isEmpty() -> {
                    EmptyNotesContent(
                        message = state.error.orEmpty(),
                        onAddNote = onAddNote,
                    )
                }

                state.notes.isEmpty() -> {
                    EmptyNotesContent(
                        message = "No notes yet.\nTap below to create your first note.",
                        onAddNote = onAddNote,
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.notes, key = { it.id }) { note ->
                            NoteListItem(
                                note = note,
                                onClickDetail = onClickDetail,
                                onEditNote = onEditNote,
                                onDeleteNote = { selectedNote ->
                                    viewModel.onEvent(NotesListIntent.DeleteNote(selectedNote.id))
                                    onDeleteNote(selectedNote)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyNotesContent(
    message: String,
    onAddNote: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Button(onClick = onAddNote) {
            Text(text = "Add Notes")
        }
    }
}

@Composable
private fun NoteListItem(
    note: Notes,
    onClickDetail: (Notes) -> Unit,
    onEditNote: (Notes) -> Unit,
    onDeleteNote: (Notes) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickDetail(note) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Priority: ${note.priority.name}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Column {
                TextButton(onClick = { onEditNote(note) }) {
                    Text(text = "Edit")
                }
                TextButton(onClick = { onDeleteNote(note) }) {
                    Text(text = "Delete")
                }
            }
        }
    }
}
