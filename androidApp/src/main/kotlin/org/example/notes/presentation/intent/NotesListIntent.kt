package org.example.notes.presentation.intent

sealed class NotesListIntent {
    object LoadNotes : NotesListIntent()
    data class DeleteNote(val id: Int) : NotesListIntent()
    data class ToggleNoteDone(val id: Int, val isDone: Boolean) : NotesListIntent()
    data class MarkSelectedAsDone(val ids: Set<Int>) : NotesListIntent()
}
