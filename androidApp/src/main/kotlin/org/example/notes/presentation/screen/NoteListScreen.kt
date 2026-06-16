package org.example.notes.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.example.notes.domain.model.Notes
import org.example.notes.presentation.intent.NotesListIntent
import org.example.notes.presentation.state.NoteUiState
import org.example.notes.presentation.viewmodel.GetNotesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NotesListScreen(
    onClickDetail: (Notes) -> Unit,
    onEditNote: (Notes) -> Unit,
    onDeleteNote: () -> Unit,
    onAddNote: () -> Unit,
    viewModel: GetNotesViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var pendingDeletedIds by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var selectedNoteIds by remember { mutableStateOf<Set<Int>>(emptySet()) }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val isSelectionMode = selectedNoteIds.isNotEmpty()
    viewModel.onEvent(NotesListIntent.LoadNotes)


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(
                    onClick = onAddNote,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            }
        },
        topBar = {
            val visibleNotes = (state as? NoteUiState.Success)?.notes
                ?.filter { it.id !in pendingDeletedIds }
                .orEmpty()
            val allSelected = visibleNotes.isNotEmpty() &&
                visibleNotes.all { it.id in selectedNoteIds }

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = {
                    Text(
                        text = if (isSelectionMode) {
                            "${selectedNoteIds.size} selected"
                        } else {
                            "My Header"
                        },
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = { selectedNoteIds = emptySet() }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear selection")
                        }
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        TextButton(
                            onClick = {
                                selectedNoteIds = if (allSelected) {
                                    emptySet()
                                } else {
                                    visibleNotes.map { it.id }.toSet()
                                }
                            }
                        ) {
                            Text(if (allSelected) "Deselect all" else "Select all")
                        }
                        IconButton(
                            onClick = {
                                viewModel.onEvent(NotesListIntent.MarkSelectedAsDone(selectedNoteIds))
                                selectedNoteIds = emptySet()
                            }
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Mark done")
                        }
                        IconButton(
                            onClick = {
                                pendingDeletedIds = pendingDeletedIds + selectedNoteIds
                                val idsToDelete = selectedNoteIds
                                selectedNoteIds = emptySet()
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "${idsToDelete.size} notes deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short,
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        pendingDeletedIds = pendingDeletedIds - idsToDelete
                                    } else if (result == SnackbarResult.Dismissed) {
                                        idsToDelete.forEach { id ->
                                            viewModel.onEvent(NotesListIntent.DeleteNote(id))
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete selected")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when (state) {
                is NoteUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }

                is NoteUiState.Error -> {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = (state as NoteUiState.Error).message,
                        textAlign = TextAlign.Center,
                        style = TextStyle(color = Color.Red)
                    )
                }

                is NoteUiState.Success -> {
                    val list = (state as NoteUiState.Success).notes.filter { it.id !in pendingDeletedIds }
                    if (list.isEmpty()) {
                        NoDataScreen(
                            onAddNote = onAddNote,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 88.dp,
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(list, key = { item -> item.id }) { note ->
                                val isSelected = note.id in selectedNoteIds
                                SwipeableNoteItem(
                                    note = note,
                                    isSelected = isSelected,
                                    selectionEnabled = isSelectionMode,
                                    onClickDetail = {
                                        if (isSelectionMode) {
                                            selectedNoteIds = if (isSelected) {
                                                selectedNoteIds - note.id
                                            } else {
                                                selectedNoteIds + note.id
                                            }
                                        } else {
                                            onClickDetail(note)
                                        }
                                    },
                                    onEditNote = { onEditNote(note) },
                                    onToggleSelection = { checked ->
                                        selectedNoteIds = if (checked) {
                                            selectedNoteIds + note.id
                                        } else {
                                            selectedNoteIds - note.id
                                        }
                                    },
                                    onDeleteNote = { note ->
                                        if (isSelectionMode) return@SwipeableNoteItem
                                        if (note.id in pendingDeletedIds) return@SwipeableNoteItem
                                        pendingDeletedIds = pendingDeletedIds + note.id
                                        scope.launch {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "Note deleted",
                                                actionLabel = "Undo",
                                                duration = SnackbarDuration.Short
                                            )

                                            if (result == SnackbarResult.ActionPerformed) {
                                                pendingDeletedIds = pendingDeletedIds - note.id
                                            } else if (result == SnackbarResult.Dismissed) {
                                                viewModel.onEvent(NotesListIntent.DeleteNote(note.id))
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun SwipeableNoteItem(
    note: Notes,
    isSelected: Boolean,
    selectionEnabled: Boolean,
    onClickDetail: () -> Unit,
    onEditNote: () -> Unit,
    onToggleSelection: (Boolean) -> Unit,
    onDeleteNote: (Notes) -> Unit
) {
    if (selectionEnabled) {
        NoteItem(
            note = note,
            isSelected = isSelected,
            onClickDetail = onClickDetail,
            onEditNote = onEditNote,
            onToggleSelection = onToggleSelection,
            onDeleteNote = { onDeleteNote(note) },
            showActions = false,
        )
        return
    }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { newValue ->
            if (newValue == SwipeToDismissBoxValue.EndToStart) {
                onDeleteNote(note)
            }
            true
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red, shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        }
    ) {
        NoteItem(
            note = note,
            isSelected = isSelected,
            onClickDetail = onClickDetail,
            onEditNote = onEditNote,
            onToggleSelection = onToggleSelection,
            onDeleteNote = { onDeleteNote(note) },
            showActions = true,
        )
    }
}

@Composable
fun NoteItem(
    note: Notes,
    isSelected: Boolean,
    onClickDetail: () -> Unit,
    onEditNote: () -> Unit,
    onToggleSelection: (Boolean) -> Unit,
    onDeleteNote: (Notes) -> Unit,
    showActions: Boolean,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            } else {
                Color(note.priority.color)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .combinedClickable(
                    onClick = onClickDetail,
                    onLongClick = { onToggleSelection(!isSelected) },
                )
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = onToggleSelection,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TaskStatusBadge(isDone = note.isDone)
                    Text(
                        text = note.priority.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (showActions) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onEditNote,
                        modifier = Modifier.size(36.dp),
                    ) {
                        Icon(
                            contentDescription = "Edit",
                            imageVector = Icons.Default.Edit,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                }
            }
        }
    }
}


@Composable
private fun TaskStatusBadge(isDone: Boolean) {
    val label = if (isDone) "Done" else "Pending"
    val backgroundColor = if (isDone) {
        MaterialTheme.colorScheme.tertiaryContainer
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    val textColor = if (isDone) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = textColor,
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    )
}


@Composable
fun NoDataScreen(onAddNote: () -> Unit, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(30.dp)
            .height(200.dp),
        shape = RoundedCornerShape(10.dp), elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "No notes available. Please add a note.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAddNote) {
                Text(text = "Add Note")
            }

        }
    }

}



