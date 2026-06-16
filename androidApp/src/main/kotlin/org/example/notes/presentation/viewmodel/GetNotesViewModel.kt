package org.example.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.example.notes.domain.repository.NotesRepository
import org.example.notes.domain.usecase.DeleteNotesUseCase
import org.example.notes.domain.usecase.MarkNotesDoneUseCase
import org.example.notes.domain.usecase.MarkNotesNotDoneUseCase
import org.example.notes.presentation.intent.NotesListIntent
import org.example.notes.presentation.state.NoteUiState

class GetNotesViewModel(
    private val repository: NotesRepository,
    private val deleteNotesUseCase: DeleteNotesUseCase,
    private val markNotesDoneUseCase: MarkNotesDoneUseCase,
    private val markNotesNotDoneUseCase: MarkNotesNotDoneUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<NoteUiState>(NoteUiState.Loading)
    val state: StateFlow<NoteUiState> = _state.asStateFlow()

    fun onEvent(event: NotesListIntent) {
        when (event) {
            is NotesListIntent.LoadNotes -> loadNotes()
            is NotesListIntent.DeleteNote -> deleteNote(event.id)
            is NotesListIntent.ToggleNoteDone -> toggleNoteDone(event.id, event.isDone)
            is NotesListIntent.MarkSelectedAsDone -> markSelectedAsDone(event.ids)
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            _state.value = NoteUiState.Loading
            repository.observeNotes()
                .catch { throwable ->
                    _state.value = NoteUiState.Error(throwable.message ?: "Failed to load notes")
                }
                .collect { notes ->
                    _state.value = NoteUiState.Success(notes)
                }
        }
    }

    private fun deleteNote(id: Int) {
        viewModelScope.launch {
            runCatching { deleteNotesUseCase(id) }
                .onFailure { throwable ->
                    _state.value = NoteUiState.Error(throwable.message ?: "Failed to delete note")
                }
        }
    }

    private fun toggleNoteDone(id: Int, isDone: Boolean) {
        viewModelScope.launch {
            runCatching {
                if (isDone) {
                    markNotesDoneUseCase(id)
                } else {
                    markNotesNotDoneUseCase(id)
                }
            }
        }
    }

    private fun markSelectedAsDone(ids: Set<Int>) {
        viewModelScope.launch {
            ids.forEach { id ->
                runCatching { markNotesDoneUseCase(id) }
            }
        }
    }
}