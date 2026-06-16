package org.example.notes.presentation.state

import org.example.notes.domain.model.Notes

sealed class NoteUiState {
    object Loading : NoteUiState()
    data class Success(val notes: List<Notes>) : NoteUiState()
    data class Error(val message: String) : NoteUiState()
}
