package com.devtheme.launcher.ui.components

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtheme.launcher.data.AppEntry
import com.devtheme.launcher.data.DevIconCatalog
import com.devtheme.launcher.ui.theme.DraculaCurrentLine
import com.devtheme.launcher.ui.theme.DraculaSurface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun drawableToBitmap(drawable: Drawable?): Bitmap? {
    if (drawable == null) return null
    if (drawable is BitmapDrawable && drawable.bitmap != null) {
        return drawable.bitmap
    }
    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 96
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 96
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

@Composable
fun DevIconView(
    app: AppEntry,
    size: Dp = 54.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val custom = app.customIcon

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        when (custom.type) {
            "DEV_CATALOG" -> {
                val item = DevIconCatalog.getIconById(custom.value)
                val tint = Color(custom.tintColor)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DraculaSurface)
                        .border(1.5.dp, tint.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (item != null) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = tint,
                            modifier = Modifier.size(size * 0.58f)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Android,
                            contentDescription = null,
                            tint = tint,
                            modifier = Modifier.size(size * 0.58f)
                        )
                    }
                }
            }
            "BADGE_MONO" -> {
                val tint = Color(custom.tintColor)
                val text = if (custom.value.isNotBlank()) custom.value.take(3).uppercase() else app.label.take(2).uppercase()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DraculaSurface)
                        .border(1.5.dp, tint.copy(alpha = 0.7f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "[$text]",
                        color = tint,
                        fontWeight = FontWeight.Bold,
                        fontSize = (size.value * 0.28f).sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            "GALLERY_URI" -> {
                var galleryBmp by remember(custom.value) { mutableStateOf<Bitmap?>(null) }
                LaunchedEffect(custom.value) {
                    withContext(Dispatchers.IO) {
                        try {
                            val uri = Uri.parse(custom.value)
                            context.contentResolver.openInputStream(uri)?.use { stream ->
                                galleryBmp = android.graphics.BitmapFactory.decodeStream(stream)
                            }
                        } catch (e: Exception) {
                            galleryBmp = null
                        }
                    }
                }

                if (galleryBmp != null) {
                    Image(
                        bitmap = galleryBmp!!.asImageBitmap(),
                        contentDescription = app.label,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback to original app icon if gallery decode failed
                    DefaultAppIcon(app, size)
                }
            }
            else -> {
                // DEFAULT system icon
                DefaultAppIcon(app, size)
            }
        }
    }
}

@Composable
fun DefaultAppIcon(app: AppEntry, size: Dp) {
    val bitmap = remember(app.icon) { drawableToBitmap(app.icon) }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = app.label,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DraculaCurrentLine),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Android,
                contentDescription = app.label,
                tint = Color.White,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

