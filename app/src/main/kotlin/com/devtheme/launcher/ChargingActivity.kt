package com.devtheme.launcher

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.devtheme.launcher.data.LauncherPreferences
import com.devtheme.launcher.ui.screens.ChargingScreen
import com.devtheme.launcher.ui.theme.DevLauncherTheme

class ChargingActivity : ComponentActivity() {

    private var powerDisconnectReceiver: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Allow showing over lockscreen and turning on screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        val prefs = LauncherPreferences(this)

        setContent {
            DevLauncherTheme(preset = prefs.themePreset, fontChoice = prefs.fontChoice) {
                ChargingScreen(onDismiss = { finish() })
            }
        }

        // Auto close when unplugged
        powerDisconnectReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Intent.ACTION_POWER_DISCONNECTED) {
                    finish()
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(powerDisconnectReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        powerDisconnectReceiver?.let {
            try {
                unregisterReceiver(it)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
}

