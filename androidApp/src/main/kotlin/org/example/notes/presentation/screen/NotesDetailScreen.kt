package org.example.notes.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.notes.domain.model.Notes

@Composable
fun NotesDetailScreen(
    note: Notes,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(text = "Note Detail: ${note.title}")
        Text(text = "Priority: ${note.priority}")
        Text(text = "Done: ${note.isDone}")
        TextButton(onClick = onBack) {
            Text(text = "Back")
        }
    }
}
