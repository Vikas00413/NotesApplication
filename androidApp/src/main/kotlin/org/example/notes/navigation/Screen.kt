package org.example.notes.navigation

import kotlinx.serialization.Serializable
import org.example.notes.domain.model.Notes

@Serializable
object NotesListRoute

@Serializable
object AddNoteRoute

@Serializable
data class NoteDetailRoute(val note: Notes)

@Serializable
data class EditNoteRoute(val note: Notes)
