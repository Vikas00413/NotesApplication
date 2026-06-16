package org.example.notes.presentation.state

import org.example.notes.domain.model.Notes

data class NoteUiState(
    val notes: List<Notes>  = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)