package com.devtheme.launcher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AppEntry(
    val packageName: String,
    val activityName: String,
    val label: String,
    val icon: Drawable?,
    val customIcon: CustomIconConfig = CustomIconConfig("DEFAULT", "")
) {
    /**
     * Code-styled file-like name for the IDE aesthetic, e.g. "whatsapp.apk", "camera.sh", "calc.py"
     */
    val devFileName: String
        get() {
            val clean = label.lowercase().replace(" ", "_").filter { it.isLetterOrDigit() || it == '_' }
            val ext = when {
                packageName.contains("camera") || packageName.contains("photo") -> ".raw"
                packageName.contains("music") || packageName.contains("audio") || packageName.contains("spotify") -> ".mp3"
                packageName.contains("video") || packageName.contains("youtube") -> ".mp4"
                packageName.contains("browser") || packageName.contains("chrome") -> ".html"
                packageName.contains("mail") || packageName.contains("message") || packageName.contains("chat") || packageName.contains("whatsapp") -> ".json"
                packageName.contains("setting") || packageName.contains("config") -> ".yaml"
                packageName.contains("calc") || packageName.contains("math") -> ".py"
                packageName.contains("file") || packageName.contains("doc") -> ".md"
                packageName.contains("terminal") || packageName.contains("term") -> ".sh"
                packageName.contains("code") || packageName.contains("git") -> ".kt"
                else -> ".apk"
            }
            return if (clean.isBlank()) "app$ext" else "$clean$ext"
        }
}

object AppRepository {
    suspend fun loadInstalledApps(context: Context, prefs: LauncherPreferences): List<AppEntry> = withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolveInfos: List<ResolveInfo> = try {
            pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        } catch (e: Exception) {
            pm.queryIntentActivities(intent, 0)
        }

        val appList = mutableListOf<AppEntry>()
        val myPackage = context.packageName

        for (info in resolveInfos) {
            val packageName = info.activityInfo.packageName
            // Don't show our own launcher inside the launcher apps list
            if (packageName == myPackage) continue

            val activityName = info.activityInfo.name
            val label = try {
                info.loadLabel(pm).toString()
            } catch (e: Exception) {
                packageName
            }
            val icon = try {
                info.loadIcon(pm)
            } catch (e: Exception) {
                null
            }

            val customConfig = prefs.getCustomIcon(packageName)

            appList.add(
                AppEntry(
                    packageName = packageName,
                    activityName = activityName,
                    label = label,
                    icon = icon,
                    customIcon = customConfig
                )
            )
        }

        // Sort alphabetically by label
        appList.sortedBy { it.label.lowercase() }
    }

    fun launchApp(context: Context, entry: AppEntry) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(entry.packageName)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openAppInfo(context: Context, packageName: String) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun uninstallApp(context: Context, packageName: String) {
        try {
            val intent = Intent(Intent.ACTION_DELETE).apply {
                data = Uri.fromParts("package", packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

