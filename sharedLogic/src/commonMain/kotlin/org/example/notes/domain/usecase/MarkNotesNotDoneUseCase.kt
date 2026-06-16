package org.example.notes.domain.usecase

import org.example.notes.domain.repository.NotesRepository

class MarkNotesNotDoneUseCase (
    private val repository: NotesRepository,
) {
    suspend operator fun invoke(id: Int) {
        repository.markNoteAsNotDone(id)
    }
}