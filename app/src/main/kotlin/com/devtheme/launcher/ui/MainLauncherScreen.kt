package com.devtheme.launcher.ui

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.devtheme.launcher.ChargingActivity
import com.devtheme.launcher.data.*
import com.devtheme.launcher.ui.components.*
import com.devtheme.launcher.ui.screens.*
import com.devtheme.launcher.ui.theme.DevLauncherTheme
import kotlinx.coroutines.launch

@Composable
fun MainLauncherScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val prefs = remember { LauncherPreferences(context) }

    // Screen State: 0: Home, 1: AppDrawer, 2: Settings, 3: ChargingPreview
    var currentScreen by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var appsList by remember { mutableStateOf<List<AppEntry>>(emptyList()) }
    var isLoadingApps by remember { mutableStateOf(true) }

    // Reload apps helper
    val reloadApps: () -> Unit = {
        coroutineScope.launch {
            appsList = AppRepository.loadInstalledApps(context, prefs)
            isLoadingApps = false
        }
    }

    LaunchedEffect(Unit) {
        reloadApps()
    }

    // Android Back button behavior (from drawer/settings returns to Home)
    BackHandler(enabled = currentScreen != 0) {
        if (currentScreen == 1 && searchQuery.isNotEmpty()) {
            searchQuery = ""
        } else {
            currentScreen = 0
        }
    }

    // Terminal command executor
    val executeCommand: (String) -> Unit = { cmd ->
        when (cmd.lowercase().trim()) {
            ":help" -> Toast.makeText(context, "Comandos: :theme, :charge, :font, :wp, :clear", Toast.LENGTH_LONG).show()
            ":theme", ":config", ":settings" -> currentScreen = 2
            ":charge" -> currentScreen = 3
            ":font" -> currentScreen = 2
            ":wp", ":wallpaper" -> currentScreen = 2
            ":clear" -> searchQuery = ""
            else -> Toast.makeText(context, "Comando desconocido: $cmd", Toast.LENGTH_SHORT).show()
        }
    }

    DevLauncherTheme(
        preset = prefs.themePreset,
        fontChoice = prefs.fontChoice
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            // 1. Live Background Wallpaper with Programming Animations
            SyntaxBackground(prefs = prefs)

            // 2. Main Scaffold Structure
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    Column(modifier = Modifier.statusBarsPadding()) {
                        // Top IDE Tabs: MainActivity.kt*, AppDrawer.json, Settings.yaml
                        IdeHeader(
                            currentScreen = currentScreen,
                            onSelectScreen = { screen ->
                                currentScreen = screen
                                if (screen != 1) searchQuery = ""
                            }
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Terminal Search Prompt
                        TerminalSearchBar(
                            query = searchQuery,
                            onQueryChange = {
                                searchQuery = it
                                if (it.isNotEmpty() && currentScreen != 1) {
                                    currentScreen = 1 // Switch to drawer when typing search
                                }
                            },
                            cursorBlink = prefs.cursorBlinkEnabled,
                            onExecuteCommand = executeCommand,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                },
                bottomBar = {
                    Column(modifier = Modifier.navigationBarsPadding()) {
                        // Bottom IDE Status Bar
                        IdeStatusBar(
                            onOpenSettings = { currentScreen = 2 },
                            onTestCharge = { currentScreen = 3 }
                        )
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (currentScreen) {
                        0 -> {
                            HomeScreen(
                                allApps = appsList,
                                prefs = prefs,
                                onOpenDrawer = { currentScreen = 1 },
                                onOpenSettings = { currentScreen = 2 },
                                onAppListChanged = reloadApps
                            )
                        }
                        1 -> {
                            AppDrawerScreen(
                                apps = appsList,
                                searchQuery = searchQuery,
                                prefs = prefs,
                                onAppListChanged = reloadApps
                            )
                        }
                        2 -> {
                            SettingsScreen(
                                prefs = prefs,
                                onTestChargingAnimation = { currentScreen = 3 }
                            )
                        }
                    }
                }
            }

            // 3. Fullscreen Charging Animation Overlay (when tested or triggered)
            AnimatedVisibility(
                visible = currentScreen == 3,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                ChargingScreen(onDismiss = { currentScreen = 0 })
            }
        }
    }
}

