package com.devtheme.launcher.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtheme.launcher.data.*
import com.devtheme.launcher.ui.components.DevIconView
import com.devtheme.launcher.ui.components.IconEditorDialog
import com.devtheme.launcher.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppDrawerScreen(
    apps: List<AppEntry>,
    searchQuery: String,
    prefs: LauncherPreferences,
    onAppListChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var editingApp by remember { mutableStateOf<AppEntry?>(null) }
    var contextMenuApp by remember { mutableStateOf<AppEntry?>(null) }

    // Filter apps based on query
    val filteredApps = remember(apps, searchQuery) {
        if (searchQuery.isBlank()) apps
        else {
            val q = searchQuery.lowercase().trim()
            apps.filter {
                it.label.lowercase().contains(q) ||
                it.packageName.lowercase().contains(q) ||
                it.devFileName.lowercase().contains(q)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (filteredApps.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "// 404: No symbols found",
                        style = MaterialTheme.typography.titleMedium,
                        color = DraculaComment
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "grep \"$searchQuery\" returned empty",
                        style = MaterialTheme.typography.labelSmall,
                        color = DraculaRed
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(filteredApps, key = { _, item -> item.packageName }) { index, app ->
                    val lineNumStr = String.format("%03d", index + 1)
                    val isPinned = prefs.pinnedApps.contains(app.packageName)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .combinedClickable(
                                onClick = { AppRepository.launchApp(context, app) },
                                onLongClick = { editingApp = app }
                            )
                            .padding(vertical = 6.dp, horizontal = 2.dp)
                    ) {
                        // Line number indicator above icon
                        Text(
                            text = ":$lineNumStr",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isPinned) DraculaGreen else DraculaComment.copy(alpha = 0.6f),
                            fontSize = 9.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // App Icon (Customizable individual dev icon, badge, photo, or system)
                        DevIconView(app = app, size = 52.dp)

                        Spacer(modifier = Modifier.height(4.dp))

                        // App Name formatted with IDE syntax styling
                        Text(
                            text = app.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = DraculaForeground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = app.devFileName,
                            style = MaterialTheme.typography.labelSmall,
                            color = DraculaComment,
                            fontSize = 9.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Individual Icon Customization Dialog
        editingApp?.let { app ->
            IconEditorDialog(
                app = app,
                onDismiss = { editingApp = null },
                onSave = { newConfig ->
                    prefs.setCustomIcon(app.packageName, newConfig)
                    editingApp = null
                    onAppListChanged()
                },
                onReset = {
                    prefs.clearCustomIcon(app.packageName)
                    editingApp = null
                    onAppListChanged()
                }
            )
        }
    }
}

