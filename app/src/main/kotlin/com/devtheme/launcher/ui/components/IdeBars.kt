package com.devtheme.launcher.ui.components

import android.app.ActivityManager
import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtheme.launcher.ui.theme.*

@Composable
fun IdeHeader(
    currentScreen: Int, // 0: Home, 1: Drawer, 2: Settings
    onSelectScreen: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(DraculaSurface)
            .border(width = 0.5.dp, color = DraculaCurrentLine)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tab 1: Home (MainActivity.kt)
        IdeTabItem(
            title = "MainActivity.kt",
            extension = "kt",
            iconColor = DraculaPurple,
            isActive = currentScreen == 0,
            hasUnsavedChanges = true,
            onClick = { onSelectScreen(0) }
        )

        // Tab 2: AppDrawer (Apps.json)
        IdeTabItem(
            title = "AppDrawer.json",
            extension = "json",
            iconColor = DraculaYellow,
            isActive = currentScreen == 1,
            hasUnsavedChanges = false,
            onClick = { onSelectScreen(1) }
        )

        // Tab 3: Settings (Config.yaml)
        IdeTabItem(
            title = "Settings.yaml",
            extension = "yaml",
            iconColor = DraculaCyan,
            isActive = currentScreen == 2,
            hasUnsavedChanges = false,
            onClick = { onSelectScreen(2) }
        )
    }
}

@Composable
fun IdeTabItem(
    title: String,
    extension: String,
    iconColor: Color,
    isActive: Boolean,
    hasUnsavedChanges: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isActive) DraculaBackground else DraculaSurface
    val textCol = if (isActive) DraculaForeground else DraculaComment

    Row(
        modifier = Modifier
            .fillMaxHeight()
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Dot or code symbol
        Text(
            text = "</>",
            color = iconColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = textCol,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
        )
        if (hasUnsavedChanges) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(DraculaCyan)
            )
        }
    }
}

@Composable
fun TerminalSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    cursorBlink: Boolean,
    onExecuteCommand: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorVisible by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorBlink"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = DraculaSurface,
        border = BorderStroke(1.dp, DraculaCurrentLine)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "dev@phone:~$ ",
                style = MaterialTheme.typography.bodyMedium,
                color = DraculaGreen,
                fontWeight = FontWeight.Bold
            )

            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "search_apps() o comando :help",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DraculaComment
                        )
                        if (cursorBlink && cursorVisible > 0.5f) {
                            Text(
                                text = "█",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DraculaGreen
                            )
                        }
                    }
                }

                BasicTextField(
                    value = query,
                    onValueChange = {
                        onQueryChange(it)
                        if (it.startsWith(":") && it.endsWith("\n")) {
                            onExecuteCommand(it.trim())
                            onQueryChange("")
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = DraculaForeground),
                    cursorBrush = SolidColor(if (cursorBlink && cursorVisible > 0.5f) DraculaGreen else Color.Transparent),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Limpiar",
                    tint = DraculaComment,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onQueryChange("") }
                )
            }
        }
    }
}

@Composable
fun IdeStatusBar(
    onOpenSettings: () -> Unit,
    onTestCharge: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var freeMemMb by remember { mutableIntStateOf(2048) }

    LaunchedEffect(Unit) {
        try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)
            freeMemMb = (memInfo.availMem / (1024 * 1024)).toInt()
        } catch (e: Exception) {
            // Ignore
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(DraculaPurple)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onOpenSettings() }
        ) {
            Icon(
                imageVector = Icons.Rounded.ForkRight,
                contentDescription = null,
                tint = DraculaBackground,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "git:main*",
                style = MaterialTheme.typography.labelSmall,
                color = DraculaBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "UTF-8",
                style = MaterialTheme.typography.labelSmall,
                color = DraculaBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Kotlin 2.2",
                style = MaterialTheme.typography.labelSmall,
                color = DraculaBackground
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onTestCharge() }
        ) {
            Text(
                text = "RAM: ${freeMemMb}MB libre",
                style = MaterialTheme.typography.labelSmall,
                color = DraculaBackground,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Rounded.Bolt,
                contentDescription = "Carga",
                tint = DraculaBackground,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

