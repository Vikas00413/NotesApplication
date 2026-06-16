package org.example.notes.domain.usecase

import org.example.notes.domain.model.Notes
import org.example.notes.domain.repository.NotesRepository

class UpdateNotesUseCase(
    private val repository: NotesRepository,
) {
    suspend operator fun invoke(note: Notes){
        repository.updateNote(note)
    }
}