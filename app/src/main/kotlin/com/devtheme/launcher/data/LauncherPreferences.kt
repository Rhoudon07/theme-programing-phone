package com.devtheme.launcher.data

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import org.json.JSONObject

enum class WallpaperMode(val title: String, val subtitle: String) {
    BUILTIN_DRACULA_IDE("Dracula IDE Code", "Bloque de código Kotlin/Compose con sintaxis destacada"),
    BUILTIN_MATRIX_CODE("Matrix Code Rain", "Líneas de código y binario verde fosforescente en cascada"),
    BUILTIN_CIRCUIT_BLUE("Cyberpunk Circuit", "Malla tecnológica en azul eléctrico y púrpura"),
    BUILTIN_CARBON_DARK("Carbon Dark Minimal", "Fondo oscuro de fibra de carbono para máxima batería"),
    CUSTOM_GALLERY("Galería Personal", "Tu propia imagen seleccionada de la galería")
}

enum class FontChoice(val title: String, val description: String) {
    JETBRAINS_MONO("JetBrains Mono", "Tipografía moderna para desarrolladores, clara y elegante"),
    FIRA_CODE("Fira Code", "Monospace clásico con estilo de ligaduras"),
    HACKER_VT323("Hacker Terminal (VT323)", "Estilo retro consola 80s / CRT"),
    SYSTEM_MONOSPACE("Android Monospace", "Fuente monoespaciada nativa de Android"),
    SANS_SERIF("Modern Sans", "Tipografía limpia y moderna sin serifa")
}

enum class ThemePreset(val title: String, val primaryColor: Long, val backgroundColor: Long) {
    DRACULA("Dracula Pro", 0xFFBD93F9, 0xFF282A36),
    ONE_DARK_PRO("One Dark Pro", 0xFF61AFEF, 0xFF1E1E1E),
    CYBERPUNK_NEON("Cyberpunk Neon", 0xFFFF007F, 0xFF0D0221),
    MATRIX_GREEN("Terminal Green", 0xFF00FF66, 0xFF0B0F0B)
}

data class CustomIconConfig(
    val type: String, // "DEV_CATALOG", "GALLERY_URI", "BADGE_MONO", "DEFAULT"
    val value: String, // Dev icon key or Uri string or Badge letter(s)
    val tintColor: Long = 0xFFBD93F9
) {
    fun toJson(): String {
        val obj = JSONObject()
        obj.put("type", type)
        obj.put("value", value)
        obj.put("tintColor", tintColor)
        return obj.toString()
    }

    companion object {
        fun fromJson(json: String?): CustomIconConfig {
            if (json.isNullOrBlank()) return CustomIconConfig("DEFAULT", "")
            return try {
                val obj = JSONObject(json)
                CustomIconConfig(
                    type = obj.optString("type", "DEFAULT"),
                    value = obj.optString("value", ""),
                    tintColor = obj.optLong("tintColor", 0xFFBD93F9)
                )
            } catch (e: Exception) {
                CustomIconConfig("DEFAULT", "")
            }
        }
    }
}

class LauncherPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("dev_launcher_prefs", Context.MODE_PRIVATE)

    var wallpaperMode: WallpaperMode
        get() {
            val name = prefs.getString("wallpaper_mode", WallpaperMode.BUILTIN_DRACULA_IDE.name)
            return try { WallpaperMode.valueOf(name ?: WallpaperMode.BUILTIN_DRACULA_IDE.name) }
            catch (e: Exception) { WallpaperMode.BUILTIN_DRACULA_IDE }
        }
        set(value) = prefs.edit().putString("wallpaper_mode", value.name).apply()

    var customWallpaperUri: String?
        get() = prefs.getString("custom_wallpaper_uri", null)
        set(value) = prefs.edit().putString("custom_wallpaper_uri", value).apply()

    var wallpaperDim: Float
        get() = prefs.getFloat("wallpaper_dim", 0.40f)
        set(value) = prefs.edit().putFloat("wallpaper_dim", value.coerceIn(0f, 0.9f)).apply()

    var fontChoice: FontChoice
        get() {
            val name = prefs.getString("font_choice", FontChoice.JETBRAINS_MONO.name)
            return try { FontChoice.valueOf(name ?: FontChoice.JETBRAINS_MONO.name) }
            catch (e: Exception) { FontChoice.JETBRAINS_MONO }
        }
        set(value) = prefs.edit().putString("font_choice", value.name).apply()

    var themePreset: ThemePreset
        get() {
            val name = prefs.getString("theme_preset", ThemePreset.DRACULA.name)
            return try { ThemePreset.valueOf(name ?: ThemePreset.DRACULA.name) }
            catch (e: Exception) { ThemePreset.DRACULA }
        }
        set(value) = prefs.edit().putString("theme_preset", value.name).apply()

    var syntaxParticlesEnabled: Boolean
        get() = prefs.getBoolean("syntax_particles_enabled", true)
        set(value) = prefs.edit().putBoolean("syntax_particles_enabled", value).apply()

    var syntaxParticleSpeed: Float
        get() = prefs.getFloat("syntax_particle_speed", 1.0f)
        set(value) = prefs.edit().putFloat("syntax_particle_speed", value).apply()

    var cursorBlinkEnabled: Boolean
        get() = prefs.getBoolean("cursor_blink_enabled", true)
        set(value) = prefs.edit().putBoolean("cursor_blink_enabled", value).apply()

    var chargingAnimationEnabled: Boolean
        get() = prefs.getBoolean("charging_anim_enabled", true)
        set(value) = prefs.edit().putBoolean("charging_anim_enabled", value).apply()

    var chargingAutoLaunch: Boolean
        get() = prefs.getBoolean("charging_auto_launch", true)
        set(value) = prefs.edit().putBoolean("charging_auto_launch", value).apply()

    fun getCustomIcon(packageName: String): CustomIconConfig {
        val raw = prefs.getString("icon_$packageName", null)
        return CustomIconConfig.fromJson(raw)
    }

    fun setCustomIcon(packageName: String, config: CustomIconConfig) {
        prefs.edit().putString("icon_$packageName", config.toJson()).apply()
    }

    fun clearCustomIcon(packageName: String) {
        prefs.edit().remove("icon_$packageName").apply()
    }

    var pinnedApps: Set<String>
        get() = prefs.getStringSet("pinned_apps", setOf(
            "com.android.chrome",
            "com.whatsapp",
            "com.google.android.dialer",
            "com.google.android.apps.messaging",
            "com.sec.android.app.camera"
        )) ?: emptySet()
        set(value) = prefs.edit().putStringSet("pinned_apps", value).apply()

    fun togglePinnedApp(packageName: String) {
        val current = pinnedApps.toMutableSet()
        if (current.contains(packageName)) {
            current.remove(packageName)
        } else {
            current.add(packageName)
        }
        pinnedApps = current
    }
}

