package org.example.notes.presentation.intent

import org.example.notes.data.local.entity.Priority

sealed class UpdateNoteIntent {
    data class OnTitleChange(val title: String) : UpdateNoteIntent()
    data class OnPriorityChange(val priority: Priority) : UpdateNoteIntent()
    object UpdateNote : UpdateNoteIntent()
}
