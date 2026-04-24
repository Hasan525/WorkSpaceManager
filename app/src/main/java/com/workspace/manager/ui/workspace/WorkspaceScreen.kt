package com.workspace.manager.ui.workspace

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    var fabExpanded by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { viewModel.pickImage(it) } }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) imagePickerLauncher.launch("image/*")
    }

    fun launchImagePicker() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
        permissionLauncher.launch(permission)
    }

    Scaffold(
        containerColor = BgBase,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            WorkspaceTopBar(
                isOnline = uiState.isOnline,
                itemCount = uiState.items.size,
                conflictCount = uiState.conflicts.size,
                onConflictBannerClick = {
                    uiState.conflicts.firstOrNull()?.let { viewModel.showConflict(it.itemId) }
                }
            )
        },
        floatingActionButton = {
            ExpandingFab(
                expanded = fabExpanded,
                onToggle = { fabExpanded = !fabExpanded },
                onCreateNote = {
                    fabExpanded = false
                    viewModel.createNote(onCreated = onNoteClick)
                },
                onPickImage = {
                    fabExpanded = false
                    launchImagePicker()
                }
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
            if (uiState.items.isEmpty() && !uiState.isLoading) {
                EmptyWorkspaceHint(modifier = Modifier.align(Alignment.Center))
            } else {
                WorkspaceGrid(
                    items = uiState.items,
                    draggedItemId = uiState.draggedItemId,
                    selectedAssetId = uiState.selectedAssetId,
                    onNoteClick = onNoteClick,
                    onAssetClick = viewModel::toggleAssetSelection,
                    onDragStart = viewModel::onDragStart,
                    onDragEnd = viewModel::onDragEnd,
                    onAssetRotated = viewModel::onAssetRotated,
                    onAssetDeleteRequested = viewModel::requestDeleteAsset
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Forest,
                    strokeWidth = Dim.BorderThick
                )
            }

            uiState.error?.let { errorMsg ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(Dim.Space16)
                        .navigationBarsPadding(),
                    containerColor     = BgElevated,
                    contentColor       = TextPrimary,
                    actionContentColor = ForestLight,
                    shape              = RoundedCornerShape(Dim.RadiusMd),
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

    if (uiState.assetPendingDelete != null) {
        AlertDialog(
            onDismissRequest = viewModel::cancelDeleteAsset,
            containerColor = BgElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary,
            shape = RoundedCornerShape(Dim.RadiusXl),
            title = { Text("Delete this image?") },
            text = { Text("This will permanently remove the image from all devices.") },
            confirmButton = {
                TextButton(
                    onClick = viewModel::confirmDeleteAsset,
                    colors = ButtonDefaults.textButtonColors(contentColor = StatusRed)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(
                    onClick = viewModel::cancelDeleteAsset,
                    colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                ) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun WorkspaceTopBar(
    isOnline: Boolean,
    itemCount: Int,
    conflictCount: Int,
    onConflictBannerClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgBase)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dim.ScreenEdge, vertical = Dim.Space16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Workspace",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(Dim.Space2))
                Text(
                    text = if (itemCount == 0) "No items yet"
                    else "$itemCount item${if (itemCount > 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }

            StatusPill(isOnline = isOnline)
        }

        AnimatedVisibility(
            visible = conflictCount > 0,
            enter = expandVertically() + fadeIn(),
            exit  = shrinkVertically() + fadeOut()
        ) {
            ConflictBanner(
                count = conflictCount,
                onClick = onConflictBannerClick
            )
        }

        HorizontalDivider(color = BorderSubtle, thickness = Dim.BorderHair)
    }
}

@Composable
private fun StatusPill(isOnline: Boolean) {
    val accent = if (isOnline) StatusGreen else StatusAmber
    Row(
        modifier = Modifier
            .background(accent.copy(alpha = 0.10f), RoundedCornerShape(Dim.RadiusXl))
            .border(Dim.BorderThin, accent.copy(alpha = 0.20f), RoundedCornerShape(Dim.RadiusXl))
            .padding(horizontal = Dim.Space12, vertical = Dim.Space6),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dim.Space6)
    ) {
        Box(
            modifier = Modifier
                .size(Dim.Space6)
                .background(accent, CircleShape)
        )
        Text(
            text = if (isOnline) "Live" else "Offline",
            style = MaterialTheme.typography.labelSmall,
            color = accent
        )
    }
}

@Composable
private fun ConflictBanner(count: Int, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = StatusRed.copy(alpha = 0.08f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Dim.ScreenEdge, vertical = Dim.Space12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dim.Space12)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(StatusRed.copy(alpha = 0.14f), RoundedCornerShape(Dim.RadiusSm)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = StatusRed,
                    modifier = Modifier.size(Dim.IconSm)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$count unresolved conflict${if (count > 1) "s" else ""}",
                    style = MaterialTheme.typography.labelLarge,
                    color = StatusRed
                )
                Text(
                    text = "Tap to review",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(Dim.IconSm)
            )
        }
    }
}

@Composable
private fun ExpandingFab(
    expanded: Boolean,
    onToggle: () -> Unit,
    onCreateNote: () -> Unit,
    onPickImage: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(Dim.Space12),
        modifier = Modifier.navigationBarsPadding()
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit  = fadeOut() + slideOutVertically { it / 2 }
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(Dim.Space8)
            ) {
                FabAction(
                    icon = Icons.Default.EditNote,
                    label = "New note",
                    onClick = onCreateNote
                )
                FabAction(
                    icon = Icons.Default.Image,
                    label = "Add image",
                    onClick = onPickImage
                )
            }
        }

        FloatingActionButton(
            onClick = onToggle,
            containerColor = Forest,
            contentColor   = TextPrimary,
            shape          = CircleShape,
            modifier       = Modifier.size(Dim.FabSize)
        ) {
            val iconRotation by animateDpAsState(
                targetValue = if (expanded) 45.dp else 0.dp,
                label = "fabRotation"
            )
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = if (expanded) "Close" else "Add",
                modifier = Modifier
                    .size(Dim.IconLg)
                    .scale(1f + iconRotation.value / 100f)
            )
        }
    }
}

@Composable
private fun FabAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dim.Space8)
    ) {
        Surface(
            color = BgElevated,
            shape = RoundedCornerShape(Dim.RadiusSm),
            border = BorderStroke(Dim.BorderThin, BorderSubtle)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = Dim.Space12, vertical = Dim.Space6)
            )
        }
        SmallFloatingActionButton(
            onClick = onClick,
            containerColor = BgElevated,
            contentColor   = ForestLight,
            shape          = CircleShape,
            elevation      = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(Dim.IconSm))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WorkspaceGrid(
    items: List<WorkspaceItem>,
    draggedItemId: String?,
    selectedAssetId: String?,
    onNoteClick: (String) -> Unit,
    onAssetClick: (String) -> Unit,
    onDragStart: (String) -> Unit,
    onDragEnd: (String, Int) -> Unit,
    onAssetRotated: (String, Float) -> Unit,
    onAssetDeleteRequested: (String) -> Unit
) {
    val gridState = rememberLazyStaggeredGridState()
    var dragTargetIndex by remember { mutableIntStateOf(-1) }
    val expanded = isExpandedWidth()
    val cellMinWidth = if (expanded) 200.dp else Dim.GridMinTileWidth

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(cellMinWidth),
        state = gridState,
        contentPadding = PaddingValues(
            start  = Dim.ScreenEdge,
            end    = Dim.ScreenEdge,
            top    = Dim.Space12,
            bottom = Dim.FabSize + Dim.Space24
        ),
        horizontalArrangement = Arrangement.spacedBy(Dim.Space12),
        verticalItemSpacing = Dim.Space12,
        modifier = Modifier
            .fillMaxSize()
            .background(BgBase)
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            val isDragged = item.id == draggedItemId
            val elevation by animateDpAsState(if (isDragged) 18.dp else 0.dp, label = "elev")

            Box(
                modifier = Modifier
                    .animateItemPlacement()
                    .scale(if (isDragged) 1.04f else 1f)
                    .shadow(elevation, RoundedCornerShape(Dim.RadiusLg))
                    .pointerInput(item.id) {
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
                        onClick = { if (!isDragged) onNoteClick(item.note.id) }
                    )
                    is WorkspaceItem.AssetItem -> ImageAssetTile(
                        asset = item.asset,
                        isSelected = item.asset.id == selectedAssetId,
                        onClick = { if (!isDragged) onAssetClick(item.asset.id) },
                        onRotationChanged = { angle -> onAssetRotated(item.asset.id, angle) },
                        onDeleteRequested = { onAssetDeleteRequested(item.asset.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyWorkspaceHint(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .widthIn(max = 360.dp)
            .padding(Dim.Space40),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(ForestDeep, BgElevated)
                    ),
                    shape = RoundedCornerShape(Dim.RadiusXl)
                )
                .border(Dim.BorderThin, BorderSubtle, RoundedCornerShape(Dim.RadiusXl)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.EditNote,
                contentDescription = null,
                tint = ForestLight,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(Modifier.height(Dim.Space24))
        Text(
            text = "A clean canvas",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary
        )
        Spacer(Modifier.height(Dim.Space8))
        Text(
            text = "Tap the button below to start a note or add an image from your gallery.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
