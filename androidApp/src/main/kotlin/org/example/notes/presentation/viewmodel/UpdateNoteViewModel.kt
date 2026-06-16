package org.example.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.notes.data.local.entity.SyncStatus
import org.example.notes.domain.model.Notes
import org.example.notes.domain.usecase.UpdateNotesUseCase
import org.example.notes.platform.currentTimeMillis
import org.example.notes.presentation.intent.UpdateNoteIntent
import org.example.notes.presentation.state.UpdateNoteUiState

class UpdateNoteViewModel(
    private val note: Notes,
    private val updateNotesUseCase: UpdateNotesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        UpdateNoteUiState(
            title = note.title,
            priority = note.priority,
        )
    )
    val state: StateFlow<UpdateNoteUiState> = _state.asStateFlow()

    fun onEvent(event: UpdateNoteIntent) {
        when (event) {
            is UpdateNoteIntent.OnTitleChange -> {
                _state.update { it.copy(title = event.title, error = null) }
            }

            is UpdateNoteIntent.OnPriorityChange -> {
                _state.update { it.copy(priority = event.priority, error = null) }
            }

            UpdateNoteIntent.UpdateNote -> updateNote()
        }
    }

    private fun updateNote() {
        val currentState = _state.value
        if (currentState.title.isBlank()) {
            _state.update { it.copy(error = "Title cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val updatedNote = note.copy(
                title = currentState.title.trim(),
                priority = currentState.priority,
                lastUpdatedAt = currentTimeMillis(),
                syncStatus = when (note.syncStatus) {
                    SyncStatus.SYNCED -> SyncStatus.PENDING_UPDATE
                    else -> note.syncStatus
                },
            )

            runCatching { updateNotesUseCase(updatedNote) }
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isUpdated = true) }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Failed to update note",
                        )
                    }
                }
        }
    }
}
