package com.workspace.manager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.workspace.manager.ui.note.NoteEditorScreen
import com.workspace.manager.ui.workspace.WorkspaceScreen

object Routes {
    const val WORKSPACE = "workspace"
    const val NOTE_EDITOR = "note_editor"
    const val ARG_NOTE_ID = "noteId"
    fun noteEditor(id: String? = null) =
        if (id == null) "$NOTE_EDITOR/new" else "$NOTE_EDITOR/$id"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.WORKSPACE
    ) {
        composable(Routes.WORKSPACE) {
            WorkspaceScreen(
                onNoteClick = { noteId ->
                    navController.navigate(Routes.noteEditor(noteId))
                }
            )
        }

        // New note
        composable(route = "${Routes.NOTE_EDITOR}/new") {
            NoteEditorScreen(
                noteId = null,
                onBack = { navController.popBackStack() }
            )
        }

        // Edit existing note
        composable(
            route = "${Routes.NOTE_EDITOR}/{${Routes.ARG_NOTE_ID}}",
            arguments = listOf(navArgument(Routes.ARG_NOTE_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString(Routes.ARG_NOTE_ID)
            NoteEditorScreen(
                noteId = noteId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
