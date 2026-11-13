package com.devtheme.launcher.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.devtheme.launcher.data.LauncherPreferences
import com.devtheme.launcher.data.WallpaperMode
import com.devtheme.launcher.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

data class CodeParticle(
    val text: String,
    var x: Float,
    var y: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val alpha: Float
)

@Composable
fun SyntaxBackground(
    prefs: LauncherPreferences,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val wallpaperMode = prefs.wallpaperMode
    val customUri = prefs.customWallpaperUri
    val dimLevel = prefs.wallpaperDim
    val particlesEnabled = prefs.syntaxParticlesEnabled
    val particleSpeedMultiplier = prefs.syntaxParticleSpeed

    // Load custom wallpaper bitmap if in CUSTOM_GALLERY mode
    var galleryBitmap by remember(customUri, wallpaperMode) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(customUri, wallpaperMode) {
        if (wallpaperMode == WallpaperMode.CUSTOM_GALLERY && !customUri.isNullOrBlank()) {
            withContext(Dispatchers.IO) {
                try {
                    val uri = Uri.parse(customUri)
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        val options = BitmapFactory.Options().apply {
                            inSampleSize = 2 // Optimize memory for wallpapers
                        }
                        galleryBitmap = BitmapFactory.decodeStream(stream, null, options)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    galleryBitmap = null
                }
            }
        } else {
            galleryBitmap = null
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Base Wallpaper Layer
        when (wallpaperMode) {
            WallpaperMode.CUSTOM_GALLERY -> {
                if (galleryBitmap != null) {
                    Image(
                        bitmap = galleryBitmap!!.asImageBitmap(),
                        contentDescription = "Fondo personalizado",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback to dark canvas if no photo chosen yet
                    Box(modifier = Modifier.fillMaxSize().background(DraculaBackground))
                }
            }
            WallpaperMode.BUILTIN_DRACULA_IDE -> {
                BuiltInDraculaIdeWallpaper()
            }
            WallpaperMode.BUILTIN_MATRIX_CODE -> {
                BuiltInMatrixWallpaper()
            }
            WallpaperMode.BUILTIN_CIRCUIT_BLUE -> {
                BuiltInCircuitWallpaper()
            }
            WallpaperMode.BUILTIN_CARBON_DARK -> {
                BuiltInCarbonWallpaper()
            }
        }

        // Dimmer overlay so icons/text are always legible
        if (dimLevel > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = dimLevel))
            )
        }

        // Subtle Floating Code Syntax Particles
        if (particlesEnabled) {
            FloatingCodeStreamCanvas(speedMultiplier = particleSpeedMultiplier)
        }
    }
}

@Composable
fun BuiltInDraculaIdeWallpaper() {
    Canvas(modifier = Modifier.fillMaxSize().background(DraculaBackground)) {
        val width = size.width
        val height = size.height

        // Draw faint vertical gutter for IDE line numbers
        drawLine(
            color = DraculaCurrentLine.copy(alpha = 0.5f),
            start = Offset(60f, 0f),
            end = Offset(60f, height),
            strokeWidth = 2f
        )

        // Draw faint horizontal code line shadows
        val lineSpacing = 36f
        var currentY = 120f
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#44475A")
            textSize = 20f
            typeface = android.graphics.Typeface.MONOSPACE
            alpha = 40
        }

        var lineNum = 1
        while (currentY < height) {
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    String.format("%02d", lineNum),
                    14f,
                    currentY,
                    paint
                )
            }

            // Draw subtle code block shapes
            if (lineNum % 4 != 0) {
                val blockWidth = ((lineNum * 73) % 240 + 80).toFloat()
                val indent = if (lineNum % 3 == 0) 120f else 80f
                drawLine(
                    color = DraculaCurrentLine.copy(alpha = 0.25f),
                    start = Offset(indent, currentY - 6f),
                    end = Offset(indent + blockWidth, currentY - 6f),
                    strokeWidth = 4f
                )
            }

            currentY += lineSpacing
            lineNum++
        }
    }
}

@Composable
fun BuiltInMatrixWallpaper() {
    Canvas(modifier = Modifier.fillMaxSize().background(MatrixBlack)) {
        val width = size.width
        val height = size.height
        val colWidth = 40f
        val cols = (width / colWidth).toInt()

        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#00FF66")
            textSize = 22f
            typeface = android.graphics.Typeface.MONOSPACE
            alpha = 30
        }

        for (i in 0 until cols) {
            val startY = ((i * 137) % height.toInt()).toFloat()
            for (j in 0..12) {
                val y = (startY + j * 26f) % height
                val char = if ((i + j) % 2 == 0) "1" else "0"
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(char, i * colWidth + 10f, y, paint)
                }
            }
        }
    }
}

@Composable
fun BuiltInCircuitWallpaper() {
    Canvas(modifier = Modifier.fillMaxSize().background(Color(0xFF0D0221))) {
        val width = size.width
        val height = size.height

        // Draw faint grid lines
        val step = 60f
        var x = 0f
        while (x < width) {
            drawLine(
                color = Color(0xFF00F0FF).copy(alpha = 0.08f),
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1f
            )
            x += step
        }

        var y = 0f
        while (y < height) {
            drawLine(
                color = Color(0xFFFF007F).copy(alpha = 0.06f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
            y += step
        }
    }
}

@Composable
fun BuiltInCarbonWallpaper() {
    Canvas(modifier = Modifier.fillMaxSize().background(Color(0xFF121214))) {
        val width = size.width
        val height = size.height
        val sizeCell = 24f

        var cy = 0f
        while (cy < height) {
            var cx = 0f
            while (cx < width) {
                if (((cx / sizeCell).toInt() + (cy / sizeCell).toInt()) % 2 == 0) {
                    drawRect(
                        color = Color(0xFF1E1E22),
                        topLeft = Offset(cx, cy),
                        size = androidx.compose.ui.geometry.Size(sizeCell, sizeCell)
                    )
                }
                cx += sizeCell
            }
            cy += sizeCell
        }
    }
}

@Composable
fun FloatingCodeStreamCanvas(speedMultiplier: Float = 1.0f) {
    val tokens = remember {
        listOf(
            "const", "val", "fun()", "0x80", "async", "await",
            "import", "{}", "=>", "return", "0110", "true",
            "null", "status", "build()", "// dev", "git:main",
            "String", "Int", "Flow", "yield", "<compose/>"
        )
    }

    val colors = remember {
        listOf(DraculaCyan, DraculaGreen, DraculaPink, DraculaPurple, DraculaOrange, DraculaYellow)
    }

    val particleCount = 20
    val particles = remember {
        List(particleCount) {
            CodeParticle(
                text = tokens.random(),
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speed = (0.0003f + Random.nextFloat() * 0.0007f),
                size = 18f + Random.nextFloat() * 8f,
                color = colors.random(),
                alpha = 0.08f + Random.nextFloat() * 0.12f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val frame by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "frame"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        for (p in particles) {
            p.y -= p.speed * speedMultiplier
            if (p.y < 0f) {
                p.y = 1.0f
                p.x = Random.nextFloat()
            }

            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.argb(
                    (p.alpha * 255).toInt(),
                    (p.color.red * 255).toInt(),
                    (p.color.green * 255).toInt(),
                    (p.color.blue * 255).toInt()
                )
                textSize = p.size
                typeface = android.graphics.Typeface.MONOSPACE
            }

            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(p.text, p.x * w, p.y * h, paint)
            }
        }
    }
}

