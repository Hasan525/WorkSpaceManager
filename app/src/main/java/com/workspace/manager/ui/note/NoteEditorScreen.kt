package com.workspace.manager.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
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

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved && noteId == null) onBack()
    }

    val wordCount = remember(uiState.content) {
        uiState.content.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
    }
    val charCount = uiState.content.length

    Scaffold(
        containerColor = BgBase,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            EditorTopBar(
                isSaving = uiState.isSaving,
                showDelete = noteId != null,
                onBack = {
                    if (uiState.title.isNotBlank() || uiState.content.isNotBlank()) {
                        viewModel.save()
                    }
                    onBack()
                },
                onSave = viewModel::save,
                onDelete = { showDeleteDialog = true }
            )
        },
        bottomBar = {
            EditorStatusBar(
                wordCount = wordCount,
                charCount = charCount,
                isNew = noteId == null
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BgBase)
                .imePadding()
        ) {
            // Centred reading column — caps line length on tablets, full width on phones
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = Dim.ContentMaxWidth)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = Dim.ScreenEdge)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(Dim.Space8))

                BasicTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChanged,
                    textStyle = TextStyle(
                        fontSize      = 32.sp,
                        fontWeight    = FontWeight.Medium,
                        color         = TextPrimary,
                        lineHeight    = 40.sp,
                        letterSpacing = (-0.6).sp
                    ),
                    cursorBrush = SolidColor(Forest),
                    decorationBox = { inner ->
                        if (uiState.title.isEmpty()) {
                            Text(
                                "Untitled",
                                style = TextStyle(
                                    fontSize      = 32.sp,
                                    fontWeight    = FontWeight.Medium,
                                    color         = TextMuted,
                                    lineHeight    = 40.sp,
                                    letterSpacing = (-0.6).sp
                                )
                            )
                        }
                        inner()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(Dim.Space20))

                BasicTextField(
                    value = uiState.content,
                    onValueChange = viewModel::onContentChanged,
                    textStyle = TextStyle(
                        fontSize      = 16.sp,
                        lineHeight    = 28.sp,
                        color         = TextSecondary,
                        letterSpacing = 0.sp
                    ),
                    cursorBrush = SolidColor(Forest),
                    decorationBox = { inner ->
                        if (uiState.content.isEmpty()) {
                            Text(
                                "Start writing…",
                                style = TextStyle(
                                    fontSize      = 16.sp,
                                    lineHeight    = 28.sp,
                                    color         = TextMuted
                                )
                            )
                        }
                        inner()
                    },
                    // Generous min height so the field is comfortably tappable; grows with content
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 280.dp)
                )

                Spacer(Modifier.height(Dim.Space40))
            }

            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(Dim.Space16),
                    containerColor      = BgElevated,
                    contentColor        = TextPrimary,
                    actionContentColor  = ForestLight,
                    shape               = RoundedCornerShape(Dim.RadiusMd),
                    action = { TextButton(onClick = viewModel::clearError) { Text("OK") } }
                ) { Text(error) }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = BgElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary,
            shape = RoundedCornerShape(Dim.RadiusXl),
            title = { Text("Delete this note?") },
            text = { Text("This will permanently remove the note from all devices.") },
            confirmButton = {
                TextButton(
                    onClick = { showDeleteDialog = false; viewModel.delete(onBack) },
                    colors = ButtonDefaults.textButtonColors(contentColor = StatusRed)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                ) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorTopBar(
    isSaving: Boolean,
    showDelete: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextSecondary
                )
            }
        },
        actions = {
            if (showDelete) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = TextMuted
                    )
                }
            }
            Box(
                modifier = Modifier.padding(end = Dim.Space12),
                contentAlignment = Alignment.Center
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Dim.IconMd),
                        color = Forest,
                        strokeWidth = Dim.BorderThick
                    )
                } else {
                    IconButton(onClick = onSave) {
                        Box(
                            modifier = Modifier
                                .size(Dim.SaveBtnSize)
                                .background(Forest, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Save",
                                tint = TextPrimary,
                                modifier = Modifier.size(Dim.IconSm)
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BgBase
        ),
        windowInsets = TopAppBarDefaults.windowInsets
    )
}

@Composable
private fun EditorStatusBar(
    wordCount: Int,
    charCount: Int,
    isNew: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgBase)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = BorderSubtle, thickness = Dim.BorderHair)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dim.ScreenEdge, vertical = Dim.Space12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dim.Space12)
            ) {
                StatusItem(value = wordCount.toString(), label = if (wordCount == 1) "word" else "words")
                Box(
                    modifier = Modifier
                        .size(Dim.Space2 + Dim.Space2)   // 4dp dot
                        .background(TextMuted, CircleShape)
                )
                StatusItem(value = charCount.toString(), label = if (charCount == 1) "char" else "chars")
            }
            Text(
                text = if (isNew) "Draft" else "Editing",
                style = MaterialTheme.typography.labelSmall,
                color = ForestLight
            )
        }
    }
}

@Composable
private fun StatusItem(value: String, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dim.Space4)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
    }
}
