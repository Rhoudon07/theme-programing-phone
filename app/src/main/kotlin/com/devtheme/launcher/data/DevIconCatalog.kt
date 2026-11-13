package com.devtheme.launcher.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

data class DevIconItem(
    val id: String,
    val name: String,
    val category: String,
    val icon: ImageVector,
    val defaultColor: Long = 0xFFBD93F9
)

object DevIconCatalog {
    val items: List<DevIconItem> = listOf(
        // Coding & Languages
        DevIconItem("code", "Code Brackets </>", "Lenguajes", Icons.Rounded.Code, 0xFF50FA7B),
        DevIconItem("terminal", "Terminal / Bash", "Lenguajes", Icons.Rounded.Terminal, 0xFF8BE9FD),
        DevIconItem("developer_mode", "Dev Mode", "Lenguajes", Icons.Rounded.DeveloperMode, 0xFFFF79C6),
        DevIconItem("integration", "API / Integration", "Lenguajes", Icons.Rounded.IntegrationInstructions, 0xFFFFB86C),
        DevIconItem("data_object", "JSON / Object {}", "Lenguajes", Icons.Rounded.DataObject, 0xFFF1FA8C),
        DevIconItem("data_array", "Array / Matrix []", "Lenguajes", Icons.Rounded.DataArray, 0xFFBD93F9),
        DevIconItem("javascript", "JavaScript / JS", "Lenguajes", Icons.Rounded.Javascript, 0xFFF1FA8C),
        DevIconItem("html", "HTML5 / Web", "Lenguajes", Icons.Rounded.Html, 0xFFFF5555),
        DevIconItem("css", "CSS3 / Style", "Lenguajes", Icons.Rounded.Css, 0xFF8BE9FD),
        DevIconItem("php", "PHP / Backend", "Lenguajes", Icons.Rounded.Php, 0xFFBD93F9),
        DevIconItem("data_thresholding", "Data Science", "Lenguajes", Icons.Rounded.DataThresholding, 0xFF50FA7B),

        // Infrastructure & Systems
        DevIconItem("git", "Git / Branch", "DevOps", Icons.Rounded.ForkRight, 0xFFFF79C6),
        DevIconItem("commit", "Git Commit", "DevOps", Icons.Rounded.Commit, 0xFF50FA7B),
        DevIconItem("database", "Database / SQL", "DevOps", Icons.Rounded.Storage, 0xFF8BE9FD),
        DevIconItem("dns", "DNS / Server", "DevOps", Icons.Rounded.Dns, 0xFFFFB86C),
        DevIconItem("cloud", "Cloud Platform", "DevOps", Icons.Rounded.CloudQueue, 0xFF8BE9FD),
        DevIconItem("bug", "Bug / Debugger", "DevOps", Icons.Rounded.BugReport, 0xFFFF5555),
        DevIconItem("security", "Security / SSL", "DevOps", Icons.Rounded.Security, 0xFF50FA7B),
        DevIconItem("memory", "CPU / Memory", "DevOps", Icons.Rounded.Memory, 0xFFBD93F9),
        DevIconItem("speed", "Profiler / Bench", "DevOps", Icons.Rounded.Speed, 0xFFFFB86C),
        DevIconItem("shield", "Shield / Auth", "DevOps", Icons.Rounded.Shield, 0xFF50FA7B),

        // Modern App Categories styled for Devs
        DevIconItem("phone", "VoIP / Call / Socket", "Comunicaciones", Icons.Rounded.Call, 0xFF50FA7B),
        DevIconItem("chat", "Chat / WebSocket", "Comunicaciones", Icons.Rounded.Chat, 0xFF8BE9FD),
        DevIconItem("mail", "SMTP / Mail", "Comunicaciones", Icons.Rounded.Email, 0xFFFF79C6),
        DevIconItem("browser", "Browser / HTTP", "Navegación", Icons.Rounded.Language, 0xFF8BE9FD),
        DevIconItem("camera", "Camera / Sensor", "Media", Icons.Rounded.PhotoCamera, 0xFFFFB86C),
        DevIconItem("music", "Audio / Stream", "Media", Icons.Rounded.Headphones, 0xFFFF79C6),
        DevIconItem("video", "Video / Render", "Media", Icons.Rounded.PlayCircle, 0xFFFF5555),
        DevIconItem("image", "Image / Asset", "Media", Icons.Rounded.Image, 0xFFBD93F9),
        DevIconItem("folder", "Directory / Files", "Sistema", Icons.Rounded.Folder, 0xFFF1FA8C),
        DevIconItem("settings", "Config / Settings", "Sistema", Icons.Rounded.Tune, 0xFF8BE9FD),
        DevIconItem("terminal_power", "Power / Reboot", "Sistema", Icons.Rounded.PowerSettingsNew, 0xFFFF5555),
        DevIconItem("battery", "Battery / Kernel", "Sistema", Icons.Rounded.BatteryChargingFull, 0xFF50FA7B),
        DevIconItem("game", "Game / OpenGL", "Ocio", Icons.Rounded.SportsEsports, 0xFFFF79C6),
        DevIconItem("shopping", "Checkout / E-comm", "Ocio", Icons.Rounded.ShoppingCart, 0xFFFFB86C),
        DevIconItem("brain", "AI / Neural Net", "AI", Icons.Rounded.Psychology, 0xFFFF79C6),
        DevIconItem("sparkles", "Gemini / Synth", "AI", Icons.Rounded.AutoAwesome, 0xFF8BE9FD)
    )

    fun getIconById(id: String): DevIconItem? = items.find { it.id == id }

    val accentColors: List<Long> = listOf(
        0xFFBD93F9, // Dracula Purple
        0xFF8BE9FD, // Dracula Cyan
        0xFF50FA7B, // Dracula Green
        0xFFFF79C6, // Dracula Pink
        0xFFFFB86C, // Dracula Orange
        0xFFF1FA8C, // Dracula Yellow
        0xFFFF5555, // Dracula Red
        0xFF61AFEF, // One Dark Blue
        0xFF98C379, // One Dark Green
        0xFFE5C07B, // One Dark Gold
        0xFFFFFFFF  // Pure White
    )
}

