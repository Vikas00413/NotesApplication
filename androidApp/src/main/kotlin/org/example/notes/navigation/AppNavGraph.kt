package org.example.notes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.example.notes.presentation.screen.AddNoteScreen
import org.example.notes.presentation.screen.EditNoteScreen
import org.example.notes.presentation.screen.NotesDetailScreen
import org.example.notes.presentation.screen.NotesListScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NotesListRoute,
        typeMap = notesNavTypeMap,
    ) {
        composable<NotesListRoute> {
            NotesListScreen(
                onClickDetail = { note ->
                    navController.navigate(NoteDetailRoute(note = note))
                },
                onEditNote = { note ->
                    navController.navigate(EditNoteRoute(note = note))
                },
                onDeleteNote = {

                },
                onAddNote = {
                    navController.navigate(AddNoteRoute)
                },
            )
        }
        composable<NoteDetailRoute>(
            typeMap = notesNavTypeMap,
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<NoteDetailRoute>()
            NotesDetailScreen(
                note = route.note,
                onBack = { navController.popBackStack() },
            )
        }
        composable<EditNoteRoute>(
            typeMap = notesNavTypeMap,
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<EditNoteRoute>()
            EditNoteScreen(
                note = route.note,
                onBack = { navController.popBackStack() },
            )
        }
        composable<AddNoteRoute> {
            AddNoteScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
