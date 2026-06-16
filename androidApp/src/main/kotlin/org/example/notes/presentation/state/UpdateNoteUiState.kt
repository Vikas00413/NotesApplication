package org.example.notes.presentation.state

import org.example.notes.data.local.entity.Priority

data class UpdateNoteUiState(
    val title: String = "",
    val priority: Priority = Priority.LOW,
    val isLoading: Boolean = false,
    val isUpdated: Boolean = false,
    val error: String? = null,
)
