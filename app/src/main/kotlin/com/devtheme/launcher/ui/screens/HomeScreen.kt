package com.devtheme.launcher.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    allApps: List<AppEntry>,
    prefs: LauncherPreferences,
    onOpenDrawer: () -> Unit,
    onOpenSettings: () -> Unit,
    onAppListChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var editingApp by remember { mutableStateOf<AppEntry?>(null) }

    // Live clock
    var timeFormatted by remember { mutableStateOf("") }
    var dateFormatted by remember { mutableStateOf("") }
    var dayFormatted by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Date()
            timeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
            dateFormatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now)
            dayFormatted = SimpleDateFormat("EEEE", Locale.getDefault()).format(now)
            kotlinx.coroutines.delay(1000)
        }
    }

    // Pinned / Favorite Apps
    val pinnedApps = remember(allApps, prefs.pinnedApps) {
        val set = prefs.pinnedApps
        val list = allApps.filter { set.contains(it.packageName) }
        if (list.isNotEmpty()) list
        else allApps.take(5) // fallback to first 5 apps
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Big Developer Clock & Code Block Widget
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = DraculaSurface.copy(alpha = 0.85f)),
            border = BorderStroke(1.dp, DraculaCurrentLine)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "// Kernel.Clock.kt",
                        style = MaterialTheme.typography.labelSmall,
                        color = DraculaComment
                    )
                    Text(
                        text = "PID: ${android.os.Process.myPid()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = DraculaCyan
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = timeFormatted,
                        style = MaterialTheme.typography.displayLarge,
                        color = DraculaForeground,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.padding(bottom = 6.dp)) {
                        Text(
                            text = "val date = \"$dateFormatted\"",
                            style = MaterialTheme.typography.labelMedium,
                            color = DraculaGreen
                        )
                        Text(
                            text = "// $dayFormatted",
                            style = MaterialTheme.typography.labelSmall,
                            color = DraculaYellow
                        )
                    }
                }

                HorizontalDivider(
                    color = DraculaCurrentLine,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                // Code snippet status
                Text(
                    text = "val ideState = IDE.READY; val activeBranch = \"main*\";",
                    style = MaterialTheme.typography.labelSmall,
                    color = DraculaPurple
                )
                Text(
                    text = "val installedApps = ${allApps.size}; val memory = \"NORMAL\";",
                    style = MaterialTheme.typography.labelSmall,
                    color = DraculaCyan
                )
            }
        }

        // Quick Navigation Terminal Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onOpenDrawer,
                colors = ButtonDefaults.buttonColors(containerColor = DraculaSurface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DraculaPurple.copy(alpha = 0.6f)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Rounded.Apps, contentDescription = null, tint = DraculaPurple, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "open_drawer()",
                    color = DraculaForeground,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onOpenSettings,
                colors = ButtonDefaults.buttonColors(containerColor = DraculaSurface),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, DraculaCyan.copy(alpha = 0.6f)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Rounded.Tune, contentDescription = null, tint = DraculaCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "config.yaml",
                    color = DraculaForeground,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Bottom Dock: Pinned Developer Apps
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DraculaSurface.copy(alpha = 0.90f)),
            border = BorderStroke(1.dp, DraculaCurrentLine)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "// Pinned favorites - Mantén presionado para personalizar",
                    style = MaterialTheme.typography.labelSmall,
                    color = DraculaComment,
                    fontSize = 9.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    pinnedApps.take(5).forEach { app ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .combinedClickable(
                                    onClick = { AppRepository.launchApp(context, app) },
                                    onLongClick = { editingApp = app }
                                )
                                .padding(4.dp)
                        ) {
                            DevIconView(app = app, size = 48.dp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = app.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = DraculaForeground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Individual Icon Customizer Dialog
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

