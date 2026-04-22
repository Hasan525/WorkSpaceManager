package com.workspace.manager.ui.workspace

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.workspace.manager.domain.model.ConflictResolution
import com.workspace.manager.domain.model.WorkspaceItem
import com.workspace.manager.ui.components.ConflictDialog
import com.workspace.manager.ui.components.ImageAssetTile
import com.workspace.manager.ui.components.NoteTile
import com.workspace.manager.ui.theme.OfflineOrange
import com.workspace.manager.ui.theme.SyncGreen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreen(
    onNoteClick: (String) -> Unit,
    viewModel: WorkspaceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { viewModel.pickImage(it) } }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) imagePickerLauncher.launch("image/*") }

    Scaffold(
        topBar = {
            WorkspaceTopBar(
                isOnline = uiState.isOnline,
                conflictCount = uiState.conflicts.size,
                onConflictBannerClick = {
                    uiState.conflicts.firstOrNull()?.let { viewModel.showConflict(it.itemId) }
                }
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Add Image FAB
                SmallFloatingActionButton(
                    onClick = {
                        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            Manifest.permission.READ_MEDIA_IMAGES
                        else Manifest.permission.READ_EXTERNAL_STORAGE
                        permissionLauncher.launch(permission)
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Add Image")
                }

                // Add Note FAB
                FloatingActionButton(
                    onClick = { viewModel.createNote() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note", tint = Color.White)
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (uiState.items.isEmpty() && !uiState.isLoading) {
                EmptyWorkspaceHint(modifier = Modifier.align(Alignment.Center))
            } else {
                WorkspaceGrid(
                    items = uiState.items,
                    draggedItemId = uiState.draggedItemId,
                    onNoteClick = onNoteClick,
                    onDragStart = viewModel::onDragStart,
                    onDragEnd = viewModel::onDragEnd,
                    onAssetRotated = viewModel::onAssetRotated
                )
            }

            // Loading overlay
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            // Error snackbar
            uiState.error?.let { errorMsg ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                    action = {
                        TextButton(onClick = viewModel::clearError) { Text("OK") }
                    }
                ) { Text(errorMsg) }
            }
        }
    }

    // Conflict resolution dialog
    uiState.activeConflict?.let { conflict ->
        ConflictDialog(
            conflict = conflict,
            onResolve = { resolution -> viewModel.resolveConflict(resolution) },
            onDismiss = viewModel::dismissConflict
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkspaceTopBar(
    isOnline: Boolean,
    conflictCount: Int,
    onConflictBannerClick: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Workspace",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            actions = {
                // Online/Offline indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (isOnline) SyncGreen else OfflineOrange,
                                CircleShape
                            )
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = if (isOnline) "Online" else "Offline",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isOnline) SyncGreen else OfflineOrange
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        // Conflict banner
        AnimatedVisibility(visible = conflictCount > 0) {
            Surface(
                onClick = onConflictBannerClick,
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "$conflictCount unresolved conflict${if (conflictCount > 1) "s" else ""}. Tap to resolve.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WorkspaceGrid(
    items: List<WorkspaceItem>,
    draggedItemId: String?,
    onNoteClick: (String) -> Unit,
    onDragStart: (String) -> Unit,
    onDragEnd: (String, Int) -> Unit,
    onAssetRotated: (String, Float) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            val isDragged = item.id == draggedItemId
            val elevation by animateDpAsState(if (isDragged) 16.dp else 0.dp, label = "elev")
            val scale = if (isDragged) 1.05f else 1f

            Box(
                modifier = Modifier
                    .animateItemPlacement()
                    .scale(scale)
                    .shadow(elevation, RoundedCornerShape(16.dp))
                    .pointerInput(item.id) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { _ -> onDragStart(item.id) },
                            onDragEnd = { onDragEnd(item.id, index) },
                            onDragCancel = { onDragEnd(item.id, index) },
                            onDrag = { change, _ -> change.consume() }
                        )
                    }
            ) {
                when (item) {
                    is WorkspaceItem.NoteItem -> NoteTile(
                        note = item.note,
                        onClick = { onNoteClick(item.note.id) }
                    )
                    is WorkspaceItem.AssetItem -> ImageAssetTile(
                        asset = item.asset,
                        onRotationChanged = { angle -> onAssetRotated(item.asset.id, angle) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyWorkspaceHint(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("✨", style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Your workspace is empty",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tap + to create a note\nor add an image from your gallery.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}
