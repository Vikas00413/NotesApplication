package org.example.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.notes.domain.repository.NotesRepository
import org.example.notes.domain.usecase.DeleteNotesUseCase
import org.example.notes.presentation.intent.NotesListIntent
import org.example.notes.presentation.state.NoteUiState

class GetNotesViewModel(
    private val repository: NotesRepository,
    private val deleteNotesUseCase: DeleteNotesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NoteUiState())
    val state: StateFlow<NoteUiState> = _state.asStateFlow()

    fun onEvent(event: NotesListIntent) {
        when (event) {
            is NotesListIntent.LoadNotes -> loadNotes()
            is NotesListIntent.DeleteNote -> deleteNote(event.id)
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.observeNotes()
                .catch { throwable ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Failed to load notes"
                        )
                    }
                }
                .collect { notes ->
                    _state.update {
                        it.copy(
                            notes = notes,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    private fun deleteNote(id: Int) {
        viewModelScope.launch {
            runCatching { deleteNotesUseCase(id) }
                .onFailure { throwable ->
                    _state.update {
                        it.copy(error = throwable.message ?: "Failed to delete note")
                    }
                }
        }
    }
}