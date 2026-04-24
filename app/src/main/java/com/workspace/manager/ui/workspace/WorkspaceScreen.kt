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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.workspace.manager.domain.model.WorkspaceItem
import com.workspace.manager.ui.components.ConflictDialog
import com.workspace.manager.ui.components.ImageAssetTile
import com.workspace.manager.ui.components.NoteTile
import com.workspace.manager.ui.theme.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceScreen(
    onNoteClick: (String) -> Unit,
    viewModel: WorkspaceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { viewModel.pickImage(it) } }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) imagePickerLauncher.launch("image/*")
    }

    Scaffold(
        containerColor = BgBase,
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
                // Image picker FAB
                SmallFloatingActionButton(
                    onClick = {
                        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            Manifest.permission.READ_MEDIA_IMAGES
                        else Manifest.permission.READ_EXTERNAL_STORAGE
                        permissionLauncher.launch(permission)
                    },
                    containerColor = BgHighlight,
                    contentColor   = NeutralText,
                    shape          = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Add Image")
                }

                // Creates note then immediately navigates to the editor
                FloatingActionButton(
                    onClick = { viewModel.createNote(onCreated = onNoteClick) },
                    containerColor = Violet,
                    contentColor   = NeutralWhite,
                    shape          = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(BgBase)
        ) {
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

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Violet,
                    strokeWidth = 3.dp
                )
            }

            uiState.error?.let { errorMsg ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor     = BgElevated,
                    contentColor       = NeutralWhite,
                    actionContentColor = VioletLight,
                    action = {
                        TextButton(onClick = viewModel::clearError) { Text("OK") }
                    }
                ) { Text(errorMsg) }
            }
        }
    }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Branded "W" logo box
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Violet, RoundedCornerShape(9.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "W",
                            color = NeutralWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "Workspace",
                        style = MaterialTheme.typography.titleLarge,
                        color = NeutralWhite
                    )
                }
            },
            actions = {
                // Online / offline pill
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .background(
                            color = (if (isOnline) StatusGreen else StatusAmber).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(
                                if (isOnline) StatusGreen else StatusAmber,
                                CircleShape
                            )
                    )
                    Text(
                        text = if (isOnline) "Online" else "Offline",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isOnline) StatusGreen else StatusAmber
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BgBase,
                scrolledContainerColor = BgSurface
            )
        )

        // Conflict banner
        AnimatedVisibility(
            visible = conflictCount > 0,
            enter = expandVertically() + fadeIn(),
            exit  = shrinkVertically() + fadeOut()
        ) {
            Surface(
                onClick = onConflictBannerClick,
                color = StatusRed.copy(alpha = 0.10f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(StatusRed.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = StatusRed,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$conflictCount unresolved conflict${if (conflictCount > 1) "s" else ""}",
                            style = MaterialTheme.typography.labelLarge,
                            color = StatusRed
                        )
                        Text(
                            text = "Tap to resolve",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeutralText
                        )
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = NeutralMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Bottom divider
        HorizontalDivider(color = NeutralBorder, thickness = 0.5.dp)
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
    val gridState = rememberLazyStaggeredGridState()
    // Index of the item the dragged tile is currently hovering over.
    var dragTargetIndex by remember { mutableIntStateOf(-1) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(160.dp),
        state = gridState,
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
        modifier = Modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            val isDragged = item.id == draggedItemId
            val elevation by animateDpAsState(if (isDragged) 16.dp else 0.dp, label = "elev")

            Box(
                modifier = Modifier
                    .animateItemPlacement()
                    .scale(if (isDragged) 1.05f else 1f)
                    .shadow(elevation, RoundedCornerShape(16.dp))
                    .pointerInput(item.id) {
                        // Cumulative drag delta from this item's starting position.
                        var cumulativeDrag = Offset.Zero

                        detectDragGesturesAfterLongPress(
                            onDragStart = { _ ->
                                onDragStart(item.id)
                                cumulativeDrag = Offset.Zero
                                dragTargetIndex = index
                            },
                            onDragEnd = {
                                onDragEnd(
                                    item.id,
                                    dragTargetIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0))
                                )
                                dragTargetIndex = -1
                            },
                            onDragCancel = {
                                onDragEnd(item.id, index)
                                dragTargetIndex = -1
                            },
                            onDrag = { change, delta ->
                                change.consume()
                                cumulativeDrag += delta

                                // Find the item whose centre is closest to the
                                // dragged tile's estimated current centre.  The
                                // layoutInfo offsets are relative to the viewport
                                // top-left, which is what we want here.
                                val myInfo = gridState.layoutInfo.visibleItemsInfo
                                    .find { it.key == item.id }

                                if (myInfo != null) {
                                    val cx = myInfo.offset.x + myInfo.size.width / 2f + cumulativeDrag.x
                                    val cy = myInfo.offset.y + myInfo.size.height / 2f + cumulativeDrag.y

                                    val closest = gridState.layoutInfo.visibleItemsInfo
                                        .minByOrNull { info ->
                                            val dx = info.offset.x + info.size.width / 2f - cx
                                            val dy = info.offset.y + info.size.height / 2f - cy
                                            dx * dx + dy * dy
                                        }
                                    dragTargetIndex = closest?.index ?: index
                                }
                            }
                        )
                    }
            ) {
                when (item) {
                    is WorkspaceItem.NoteItem -> NoteTile(
                        note = item.note,
                        // Suppress click while dragging to avoid accidental navigation
                        onClick = { if (!isDragged) onNoteClick(item.note.id) }
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
        modifier = modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon box
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(VioletDeep, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = VioletLight,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Your workspace is empty",
            style = MaterialTheme.typography.titleLarge,
            color = NeutralWhite
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tap  +  to create your first note\nor add an image from your gallery.",
            style = MaterialTheme.typography.bodyMedium,
            color = NeutralText
        )
    }
}
