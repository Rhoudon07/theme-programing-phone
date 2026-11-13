package com.devtheme.launcher.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.window.Dialog
import com.devtheme.launcher.data.*
import com.devtheme.launcher.ui.theme.*

@Composable
fun IconEditorDialog(
    app: AppEntry,
    onDismiss: () -> Unit,
    onSave: (CustomIconConfig) -> Unit,
    onReset: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dev Icons, 1: Badge IDE, 2: Galería
    var currentDevIcon by remember { mutableStateOf(if (app.customIcon.type == "DEV_CATALOG") app.customIcon.value else "code") }
    var currentBadgeText by remember { mutableStateOf(if (app.customIcon.type == "BADGE_MONO") app.customIcon.value else app.label.take(2).uppercase()) }
    var currentTintColor by remember { mutableLongStateOf(app.customIcon.tintColor) }
    var galleryUriString by remember { mutableStateOf(if (app.customIcon.type == "GALLERY_URI") app.customIcon.value else null) }

    val galleryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            galleryUriString = uri.toString()
            selectedTab = 2
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            color = DraculaSurface,
            tonalElevation = 8.dp,
            border = BorderStroke(1.dp, DraculaCurrentLine)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                // Header with App Info and Preview
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val previewConfig = when (selectedTab) {
                        0 -> CustomIconConfig("DEV_CATALOG", currentDevIcon, currentTintColor)
                        1 -> CustomIconConfig("BADGE_MONO", currentBadgeText, currentTintColor)
                        2 -> if (galleryUriString != null) CustomIconConfig("GALLERY_URI", galleryUriString!!) else app.customIcon
                        else -> app.customIcon
                    }
                    val previewApp = app.copy(customIcon = previewConfig)

                    DevIconView(app = previewApp, size = 60.dp)

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = app.label,
                            style = MaterialTheme.typography.titleMedium,
                            color = DraculaForeground,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = app.devFileName,
                            style = MaterialTheme.typography.labelSmall,
                            color = DraculaComment
                        )
                        Text(
                            text = "Refactor: icon_override()",
                            style = MaterialTheme.typography.labelSmall,
                            color = DraculaCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mode Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DraculaBackground,
                    contentColor = DraculaPurple,
                    indicator = {},
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Dev Icons", fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Badge IDE", fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Galería", fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Color Picker Row (applicable to Dev Icons & Badges)
                if (selectedTab == 0 || selectedTab == 1) {
                    Text(
                        text = "Color de acento de sintaxis:",
                        style = MaterialTheme.typography.labelSmall,
                        color = DraculaComment
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DevIconCatalog.accentColors.forEach { colLong ->
                            val color = Color(colLong)
                            val isSelected = currentTintColor == colLong
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 2.5.dp else 0.dp,
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { currentTintColor = colLong }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Tab Content
                when (selectedTab) {
                    0 -> {
                        // Dev Icon Grid
                        Text(
                            text = "Selecciona un icono técnico:",
                            style = MaterialTheme.typography.labelSmall,
                            color = DraculaComment
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(DevIconCatalog.items) { item ->
                                val isSelected = currentDevIcon == item.id
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) DraculaCurrentLine else DraculaBackground)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.5.dp,
                                            color = if (isSelected) Color(currentTintColor) else DraculaCurrentLine,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { currentDevIcon = item.id },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.name,
                                        tint = if (isSelected) Color(currentTintColor) else DraculaForeground.copy(alpha = 0.7f),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                    1 -> {
                        // Badge Editor
                        Text(
                            text = "Texto de insignia (1 a 3 letras, ej. Py, WA, YT):",
                            style = MaterialTheme.typography.labelSmall,
                            color = DraculaComment
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = currentBadgeText,
                            onValueChange = { if (it.length <= 4) currentBadgeText = it.uppercase() },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.titleMedium.copy(color = Color(currentTintColor)),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(currentTintColor),
                                unfocusedBorderColor = DraculaCurrentLine,
                                focusedContainerColor = DraculaBackground,
                                unfocusedContainerColor = DraculaBackground
                            )
                        )
                    }
                    2 -> {
                        // Custom Gallery Picker
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { galleryPicker.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(containerColor = DraculaPurple)
                            ) {
                                Icon(Icons.Rounded.AddPhotoAlternate, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Elegir foto de tu galería")
                            }
                            if (galleryUriString != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Foto seleccionada correctamente",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = DraculaGreen
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onReset,
                        colors = ButtonDefaults.textButtonColors(contentColor = DraculaRed)
                    ) {
                        Text("Restablecer", fontSize = 12.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DraculaForeground)
                        ) {
                            Text("Cancelar", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val config = when (selectedTab) {
                                    0 -> CustomIconConfig("DEV_CATALOG", currentDevIcon, currentTintColor)
                                    1 -> CustomIconConfig("BADGE_MONO", currentBadgeText, currentTintColor)
                                    2 -> if (galleryUriString != null) CustomIconConfig("GALLERY_URI", galleryUriString!!) else app.customIcon
                                    else -> app.customIcon
                                }
                                onSave(config)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DraculaGreen)
                        ) {
                            Text("Guardar", color = DraculaBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

