package com.devtheme.launcher.ui.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devtheme.launcher.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

data class BatteryStatus(
    val level: Int = 50,
    val isCharging: Boolean = true,
    val plugType: String = "AC_FAST_CHARGE",
    val voltage: Float = 4.15f,
    val temperature: Float = 30.5f,
    val health: String = "HEALTH_GOOD"
)

@Composable
fun ChargingScreen(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var batteryStatus by remember { mutableStateOf(BatteryStatus()) }

    // Listen to Battery status updates
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (intent == null) return
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL
                val chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                val plugType = when (chargePlug) {
                    BatteryManager.BATTERY_PLUGGED_USB -> "USB_PORT"
                    BatteryManager.BATTERY_PLUGGED_AC -> "AC_FAST_CHARGE"
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> "WIRELESS_INDUCTION"
                    else -> "POWER_BANK_PD"
                }
                val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 4100) / 1000f
                val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 300) / 10f
                val pct = if (level >= 0 && scale > 0) ((level.toFloat() / scale.toFloat()) * 100).toInt() else 75

                batteryStatus = BatteryStatus(
                    level = pct,
                    isCharging = isCharging,
                    plugType = plugType,
                    voltage = voltage,
                    temperature = temp
                )
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val stickyIntent = context.registerReceiver(receiver, filter)
        if (stickyIntent != null) {
            receiver.onReceive(context, stickyIntent)
        }

        onDispose {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    // Glowing animation for energy pulse
    val infiniteTransition = rememberInfiniteTransition(label = "charging_pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val scanlineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline"
    )

    val timeString = remember {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        sdf.format(Date())
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0E15))
            .clickable { onDismiss() }
    ) {
        // Subtle animated scanline & circuit grid
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background matrix grid lines
            val step = 48f
            var x = 0f
            while (x < w) {
                drawLine(
                    color = DraculaCyan.copy(alpha = 0.04f),
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 1f
                )
                x += step
            }

            // Moving laser scanline
            val scanY = scanlineY * h
            drawLine(
                color = DraculaGreen.copy(alpha = 0.25f),
                start = Offset(0f, scanY),
                end = Offset(w, scanY),
                strokeWidth = 2f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Header: Terminal prompt
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DraculaSurface)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Terminal,
                    contentDescription = null,
                    tint = DraculaGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "kernel::power_daemon --watch",
                    style = MaterialTheme.typography.labelMedium,
                    color = DraculaForeground,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.labelSmall,
                    color = DraculaComment
                )
            }

            // Center: Big Futuristic Battery Meter & Progress
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Pulsing Outer Ring
                    Canvas(modifier = Modifier.size(240.dp)) {
                        val stroke = 8.dp.toPx()
                        val radius = (size.minDimension - stroke) / 2
                        drawCircle(
                            color = DraculaCurrentLine,
                            radius = radius,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke)
                        )
                        // Progress arc
                        val sweep = (batteryStatus.level.toFloat() / 100f) * 360f
                        drawArc(
                            color = DraculaGreen.copy(alpha = pulseGlow),
                            startAngle = -90f,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                width = stroke * 1.4f,
                                cap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        )
                    }

                    // Inner Battery Stats
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Bolt,
                                contentDescription = null,
                                tint = DraculaYellow,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "${batteryStatus.level}%",
                                style = MaterialTheme.typography.displayLarge,
                                color = DraculaForeground,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = if (batteryStatus.level == 100) "STATUS: COMPILED_100%" else "STATUS: FAST_CHARGING",
                            style = MaterialTheme.typography.labelMedium,
                            color = DraculaGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "flow: +2450mA // 9.1V",
                            style = MaterialTheme.typography.labelSmall,
                            color = DraculaCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Terminal ASCII Progress Bar
                val totalBars = 20
                val filledBars = ((batteryStatus.level.toFloat() / 100f) * totalBars).toInt()
                val emptyBars = totalBars - filledBars
                val asciiBar = "[" + "#".repeat(filledBars) + ".".repeat(emptyBars) + "]"

                Text(
                    text = asciiBar,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DraculaGreen,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            // Bottom: Code Syntax Console Output
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DraculaSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "// BatteryController.kt",
                        style = MaterialTheme.typography.labelSmall,
                        color = DraculaComment
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "val source = Source.${batteryStatus.plugType}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DraculaOrange
                    )
                    Text(
                        text = "val voltage = ${batteryStatus.voltage}f; // V",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DraculaCyan
                    )
                    Text(
                        text = "val temperature = ${batteryStatus.temperature}°C; // OPTIMAL",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DraculaYellow
                    )
                    Text(
                        text = "val health = Condition.${batteryStatus.health}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DraculaPurple
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "> while (battery < 100) { injectEnergyBuffer() }",
                        style = MaterialTheme.typography.bodySmall,
                        color = DraculaGreen
                    )
                }
            }

            // Unlock prompt
            Text(
                text = "[ Toca en cualquier lugar para salir ]",
                style = MaterialTheme.typography.labelSmall,
                color = DraculaComment.copy(alpha = 0.8f)
            )
        }
    }
}

