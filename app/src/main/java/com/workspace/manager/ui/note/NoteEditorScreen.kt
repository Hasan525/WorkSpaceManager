package com.workspace.manager.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.workspace.manager.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    noteId: String?,
    onBack: () -> Unit,
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Auto-navigate back after saving a new note (noteId == null means new note)
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved && noteId == null) onBack()
    }

    // Word count derived from content
    val wordCount = remember(uiState.content) {
        uiState.content.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
    }

    Scaffold(
        containerColor = BgBase,
        topBar = {
            TopAppBar(
                title = { },    // title area left clear — content speaks for itself
                navigationIcon = {
                    IconButton(onClick = {
                        // Auto-save on back if the note has any content
                        if (uiState.title.isNotBlank() || uiState.content.isNotBlank()) {
                            viewModel.save()
                        }
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = NeutralText
                        )
                    }
                },
                actions = {
                    if (noteId != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = StatusRed.copy(alpha = 0.8f)
                            )
                        }
                    }
                    // Save button / spinner
                    Box(
                        modifier = Modifier.padding(end = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Violet,
                                strokeWidth = 2.dp
                            )
                        } else {
                            IconButton(onClick = viewModel::save) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(Violet, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Save",
                                        tint = NeutralWhite,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BgBase
                )
            )
        },
        bottomBar = {
            // Minimal footer — word count + status label
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgSurface)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$wordCount ${if (wordCount == 1) "word" else "words"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeutralMuted
                )
                Text(
                    text = if (noteId == null) "New Note" else "Editing",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeutralMuted
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BgBase)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(8.dp))

                // Title field — large, prominent
                BasicTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChanged,
                    textStyle = TextStyle(
                        fontSize     = 28.sp,
                        fontWeight   = FontWeight.Bold,
                        color        = NeutralWhite,
                        lineHeight   = 36.sp,
                        letterSpacing = (-0.3).sp
                    ),
                    cursorBrush = SolidColor(Violet),
                    decorationBox = { inner ->
                        if (uiState.title.isEmpty()) {
                            Text(
                                "Title…",
                                style = TextStyle(
                                    fontSize     = 28.sp,
                                    fontWeight   = FontWeight.Bold,
                                    color        = NeutralMuted,
                                    lineHeight   = 36.sp,
                                    letterSpacing = (-0.3).sp
                                )
                            )
                        }
                        inner()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(6.dp))
                HorizontalDivider(
                    color = NeutralBorder,
                    thickness = 0.5.dp
                )
                Spacer(Modifier.height(16.dp))

                // Content field
                BasicTextField(
                    value = uiState.content,
                    onValueChange = viewModel::onContentChanged,
                    textStyle = TextStyle(
                        fontSize     = 16.sp,
                        lineHeight   = 27.sp,
                        color        = NeutralText,
                        letterSpacing = 0.1.sp
                    ),
                    cursorBrush = SolidColor(Violet),
                    decorationBox = { inner ->
                        if (uiState.content.isEmpty()) {
                            Text(
                                "Start writing…",
                                style = TextStyle(
                                    fontSize     = 16.sp,
                                    lineHeight   = 27.sp,
                                    color        = NeutralMuted,
                                    letterSpacing = 0.1.sp
                                )
                            )
                        }
                        inner()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 300.dp)
                )

                Spacer(Modifier.height(32.dp))
            }

            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor      = BgElevated,
                    contentColor        = NeutralWhite,
                    actionContentColor  = VioletLight,
                    action = { TextButton(onClick = viewModel::clearError) { Text("OK") } }
                ) { Text(error) }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = BgElevated,
            titleContentColor = NeutralWhite,
            textContentColor = NeutralText,
            title = { Text("Delete Note?") },
            text = { Text("This will permanently delete the note from all devices.") },
            confirmButton = {
                TextButton(
                    onClick = { showDeleteDialog = false; viewModel.delete(onBack) },
                    colors = ButtonDefaults.textButtonColors(contentColor = StatusRed)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = NeutralText)
                ) { Text("Cancel") }
            }
        )
    }
}
