package com.devtheme.launcher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.devtheme.launcher.ChargingActivity
import com.devtheme.launcher.data.LauncherPreferences

class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_POWER_CONNECTED) {
            val prefs = LauncherPreferences(context)
            if (prefs.chargingAnimationEnabled && prefs.chargingAutoLaunch) {
                val chargeIntent = Intent(context, ChargingActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                context.startActivity(chargeIntent)
            }
        }
    }
}

