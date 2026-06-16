package org.example.notes.presentation.intent

sealed class NotesListIntent {
    object LoadNotes : NotesListIntent()
    data class DeleteNote(val id: Int) : NotesListIntent()
}
