package org.example.notes.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.notes.domain.model.Notes
import org.example.notes.domain.repository.NotesRepository

class GetAllNotesUseCase(val repository: NotesRepository){
     operator fun invoke(): Flow<List<Notes>> {
        return repository.observeNotes()
    }
}