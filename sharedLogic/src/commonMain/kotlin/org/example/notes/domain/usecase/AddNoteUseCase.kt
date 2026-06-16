package org.example.notes.domain.usecase

import org.example.notes.domain.model.Notes
import org.example.notes.domain.repository.NotesRepository

class AddNoteUseCase(
    private val repository: NotesRepository,
) {
    suspend operator fun invoke(note: Notes) {
        repository.saveNote(note)
    }
}
