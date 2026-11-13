package com.devtheme.launcher.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtheme.launcher.data.*
import com.devtheme.launcher.ui.theme.*

@Composable
fun SettingsScreen(
    prefs: LauncherPreferences,
    onTestChargingAnimation: () -> Unit,
    modifier: Modifier = Modifier
) {
    var wallpaperMode by remember { mutableStateOf(prefs.wallpaperMode) }
    var customUri by remember { mutableStateOf(prefs.customWallpaperUri) }
    var dimLevel by remember { mutableFloatStateOf(prefs.wallpaperDim) }
    var fontChoice by remember { mutableStateOf(prefs.fontChoice) }
    var themePreset by remember { mutableStateOf(prefs.themePreset) }
    var particlesEnabled by remember { mutableStateOf(prefs.syntaxParticlesEnabled) }
    var particleSpeed by remember { mutableFloatStateOf(prefs.syntaxParticleSpeed) }
    var cursorBlink by remember { mutableStateOf(prefs.cursorBlinkEnabled) }
    var chargingAnimEnabled by remember { mutableStateOf(prefs.chargingAnimationEnabled) }
    var chargingAutoLaunch by remember { mutableStateOf(prefs.chargingAutoLaunch) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            customUri = uri.toString()
            prefs.customWallpaperUri = uri.toString()
            wallpaperMode = WallpaperMode.CUSTOM_GALLERY
            prefs.wallpaperMode = WallpaperMode.CUSTOM_GALLERY
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DraculaBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp)
    ) {
        item {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null,
                    tint = DraculaPurple,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Configuración del Tema // Preferences",
                        style = MaterialTheme.typography.titleLarge,
                        color = DraculaForeground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Personaliza fondos, fuentes, animaciones y carga",
                        style = MaterialTheme.typography.labelSmall,
                        color = DraculaComment
                    )
                }
            }
        }

        // Section 1: Wallpaper
        item {
            SettingsCard(
                title = "1. Fondo de Pantalla a tu Gusto",
                subtitle = "Elige tu propia foto de galería o un fondo de código",
                icon = Icons.Rounded.Wallpaper,
                accentColor = DraculaCyan
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Wallpaper Options
                    WallpaperMode.values().forEach { mode ->
                        val isSelected = wallpaperMode == mode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) DraculaCurrentLine else DraculaSurface)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) DraculaCyan else DraculaCurrentLine,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    wallpaperMode = mode
                                    prefs.wallpaperMode = mode
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    wallpaperMode = mode
                                    prefs.wallpaperMode = mode
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = DraculaCyan)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = DraculaForeground,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = mode.subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DraculaComment
                                )
                            }
                        }
                    }

                    // Button to choose custom photo
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = DraculaCyan),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Rounded.AddPhotoAlternate, contentDescription = null, tint = DraculaBackground)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (customUri != null) "Cambiar Foto de Galería" else "Elegir Foto de Galería",
                            color = DraculaBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Dimming Slider
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Atenuación del fondo: ${(dimLevel * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = DraculaForeground
                    )
                    Slider(
                        value = dimLevel,
                        onValueChange = {
                            dimLevel = it
                            prefs.wallpaperDim = it
                        },
                        valueRange = 0f..0.85f,
                        colors = SliderDefaults.colors(
                            thumbColor = DraculaCyan,
                            activeTrackColor = DraculaCyan,
                            inactiveTrackColor = DraculaCurrentLine
                        )
                    )
                }
            }
        }

        // Section 2: Fonts
        item {
            SettingsCard(
                title = "2. Tipo de Fuente de Letra",
                subtitle = "Selecciona tu tipografía de programador preferida",
                icon = Icons.Rounded.FontDownload,
                accentColor = DraculaGreen
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FontChoice.values().forEach { choice ->
                        val isSelected = fontChoice == choice
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) DraculaCurrentLine else DraculaSurface)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) DraculaGreen else DraculaCurrentLine,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    fontChoice = choice
                                    prefs.fontChoice = choice
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    fontChoice = choice
                                    prefs.fontChoice = choice
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = DraculaGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = choice.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = DraculaForeground,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = choice.description,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DraculaComment
                                )
                                Text(
                                    text = "val code = \"${choice.name}\";",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DraculaGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Programming Animations
        item {
            SettingsCard(
                title = "3. Animaciones de Programación",
                subtitle = "Efectos sutiles de sintaxis y consola sin gastar batería",
                icon = Icons.Rounded.Animation,
                accentColor = DraculaOrange
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Partículas de sintaxis flotantes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DraculaForeground,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Tokens de código suaves que ascienden en el fondo",
                                style = MaterialTheme.typography.labelSmall,
                                color = DraculaComment
                            )
                        }
                        Switch(
                            checked = particlesEnabled,
                            onCheckedChange = {
                                particlesEnabled = it
                                prefs.syntaxParticlesEnabled = it
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = DraculaOrange)
                        )
                    }

                    if (particlesEnabled) {
                        Text(
                            text = "Velocidad de partículas: ${String.format("%.1f", particleSpeed)}x",
                            style = MaterialTheme.typography.labelSmall,
                            color = DraculaComment
                        )
                        Slider(
                            value = particleSpeed,
                            onValueChange = {
                                particleSpeed = it
                                prefs.syntaxParticleSpeed = it
                            },
                            valueRange = 0.5f..2.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = DraculaOrange,
                                activeTrackColor = DraculaOrange
                            )
                        )
                    }

                    HorizontalDivider(color = DraculaCurrentLine)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Cursor parpadeante en consola",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DraculaForeground,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Efecto terminal clásico en la barra de búsqueda",
                                style = MaterialTheme.typography.labelSmall,
                                color = DraculaComment
                            )
                        }
                        Switch(
                            checked = cursorBlink,
                            onCheckedChange = {
                                cursorBlink = it
                                prefs.cursorBlinkEnabled = it
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = DraculaOrange)
                        )
                    }
                }
            }
        }

        // Section 4: Charging Animation
        item {
            SettingsCard(
                title = "4. Animación de Carga de Programador",
                subtitle = "Pantalla de compilación con métricas de voltaje y batería",
                icon = Icons.Rounded.BatteryChargingFull,
                accentColor = DraculaYellow
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Animación de carga activa",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DraculaForeground,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Visualiza el kernel compilando al conectar el cargador",
                                style = MaterialTheme.typography.labelSmall,
                                color = DraculaComment
                            )
                        }
                        Switch(
                            checked = chargingAnimEnabled,
                            onCheckedChange = {
                                chargingAnimEnabled = it
                                prefs.chargingAnimationEnabled = it
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = DraculaYellow)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Lanzar automáticamente al enchufar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DraculaForeground,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Abre la pantalla de compilador sin tocar nada",
                                style = MaterialTheme.typography.labelSmall,
                                color = DraculaComment
                            )
                        }
                        Switch(
                            checked = chargingAutoLaunch,
                            onCheckedChange = {
                                chargingAutoLaunch = it
                                prefs.chargingAutoLaunch = it
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = DraculaYellow)
                        )
                    }

                    // Test Button
                    Button(
                        onClick = onTestChargingAnimation,
                        colors = ButtonDefaults.buttonColors(containerColor = DraculaYellow),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = DraculaBackground)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "⚡ Probar Animación de Carga Ahora",
                            color = DraculaBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Section 5: Color Palette Preset
        item {
            SettingsCard(
                title = "5. Paleta de Color IDE",
                subtitle = "Esquema cromático de tu entorno de desarrollo",
                icon = Icons.Rounded.Palette,
                accentColor = DraculaPurple
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ThemePreset.values().forEach { preset ->
                        val isSelected = themePreset == preset
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) DraculaCurrentLine else DraculaSurface)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) Color(preset.primaryColor) else DraculaCurrentLine,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    themePreset = preset
                                    prefs.themePreset = preset
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(preset.primaryColor))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = preset.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = DraculaForeground,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DraculaSurface),
        border = BorderStroke(1.dp, DraculaCurrentLine)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = DraculaForeground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = DraculaComment
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

