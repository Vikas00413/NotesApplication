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
import org.example.notes.domain.usecase.AddNoteUseCase
import org.example.notes.platform.currentTimeMillis
import org.example.notes.presentation.intent.AddNoteIntent
import org.example.notes.presentation.state.AddNoteUiState

class AddNoteViewModel(
    private val addNoteUseCase: AddNoteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddNoteUiState())
    val state: StateFlow<AddNoteUiState> = _state.asStateFlow()

    fun onEvent(event: AddNoteIntent) {
        when (event) {
            is AddNoteIntent.OnTitleChange -> {
                _state.update { it.copy(title = event.title, error = null) }
            }

            is AddNoteIntent.OnPriorityChange -> {
                _state.update { it.copy(priority = event.priority, error = null) }
            }

            AddNoteIntent.SaveNote -> saveNote()
        }
    }

    private fun saveNote() {
        val currentState = _state.value
        if (currentState.title.isBlank()) {
            _state.update { it.copy(error = "Title cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val now = currentTimeMillis()
            val note = Notes(
                id = 0,
                title = currentState.title.trim(),
                priority = currentState.priority,
                syncStatus = SyncStatus.PENDING_CREATE,
                createdAt = now.toString(),
                lastUpdatedAt = now,
                isDone = false,
            )

            runCatching { addNoteUseCase(note) }
                .onSuccess {
                    _state.update { it.copy(isLoading = false, isSaved = true) }
                }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Failed to save note",
                        )
                    }
                }
        }
    }
}
