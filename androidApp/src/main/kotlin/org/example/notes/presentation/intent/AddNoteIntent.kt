package org.example.notes.presentation.intent

import org.example.notes.data.local.entity.Priority

sealed class AddNoteIntent {
    data class OnTitleChange(val title: String) : AddNoteIntent()
    data class OnPriorityChange(val priority: Priority) : AddNoteIntent()
    object SaveNote : AddNoteIntent()
}
