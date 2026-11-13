package com.devtheme.launcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.devtheme.launcher.data.FontChoice
import com.devtheme.launcher.data.ThemePreset

// Dracula Color Palette
val DraculaBackground = Color(0xFF282A36)
val DraculaCurrentLine = Color(0xFF44475A)
val DraculaForeground = Color(0xFFF8F8F2)
val DraculaComment = Color(0xFF6272A4)
val DraculaCyan = Color(0xFF8BE9FD)
val DraculaGreen = Color(0xFF50FA7B)
val DraculaOrange = Color(0xFFFFB86C)
val DraculaPink = Color(0xFFFF79C6)
val DraculaPurple = Color(0xFFBD93F9)
val DraculaYellow = Color(0xFFF1FA8C)
val DraculaRed = Color(0xFFFF5555)
val DraculaSurface = Color(0xFF1E1F29)

// One Dark Pro Colors
val OneDarkBg = Color(0xFF1E1E1E)
val OneDarkBlue = Color(0xFF61AFEF)
val OneDarkGreen = Color(0xFF98C379)
val OneDarkGold = Color(0xFFE5C07B)
val OneDarkRed = Color(0xFFE06C75)
val OneDarkPurple = Color(0xFFC678DD)
val OneDarkSurface = Color(0xFF252526)

// Matrix Terminal Colors
val MatrixBlack = Color(0xFF0A0F0D)
val MatrixGreen = Color(0xFF00FF66)
val MatrixDimGreen = Color(0xFF008F11)
val MatrixSurface = Color(0xFF111A14)

fun getDevTypography(fontChoice: FontChoice): Typography {
    val family = when (fontChoice) {
        FontChoice.SANS_SERIF -> FontFamily.SansSerif
        else -> FontFamily.Monospace
    }

    val letterSpacing = when (fontChoice) {
        FontChoice.HACKER_VT323 -> 1.5.sp
        FontChoice.JETBRAINS_MONO -> (-0.2).sp
        FontChoice.FIRA_CODE -> 0.2.sp
        else -> 0.sp
    }

    val baseWeight = when (fontChoice) {
        FontChoice.FIRA_CODE -> FontWeight.SemiBold
        FontChoice.HACKER_VT323 -> FontWeight.Bold
        else -> FontWeight.Normal
    }

    return Typography(
        displayLarge = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold, fontSize = 48.sp, letterSpacing = letterSpacing),
        headlineLarge = TextStyle(fontFamily = family, fontWeight = FontWeight.Bold, fontSize = 28.sp, letterSpacing = letterSpacing),
        headlineMedium = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, letterSpacing = letterSpacing),
        titleLarge = TextStyle(fontFamily = family, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, letterSpacing = letterSpacing),
        titleMedium = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium, fontSize = 15.sp, letterSpacing = letterSpacing),
        bodyLarge = TextStyle(fontFamily = family, fontWeight = baseWeight, fontSize = 14.sp, letterSpacing = letterSpacing),
        bodyMedium = TextStyle(fontFamily = family, fontWeight = baseWeight, fontSize = 12.sp, letterSpacing = letterSpacing),
        labelLarge = TextStyle(fontFamily = family, fontWeight = FontWeight.Medium, fontSize = 13.sp, letterSpacing = letterSpacing),
        labelMedium = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal, fontSize = 11.sp, letterSpacing = letterSpacing),
        labelSmall = TextStyle(fontFamily = family, fontWeight = FontWeight.Normal, fontSize = 10.sp, letterSpacing = letterSpacing)
    )
}

fun getDevColorScheme(preset: ThemePreset) = when (preset) {
    ThemePreset.DRACULA -> darkColorScheme(
        primary = DraculaPurple,
        onPrimary = DraculaBackground,
        secondary = DraculaCyan,
        onSecondary = DraculaBackground,
        tertiary = DraculaGreen,
        background = DraculaBackground,
        surface = DraculaSurface,
        surfaceVariant = DraculaCurrentLine,
        onBackground = DraculaForeground,
        onSurface = DraculaForeground,
        outline = DraculaComment
    )
    ThemePreset.ONE_DARK_PRO -> darkColorScheme(
        primary = OneDarkBlue,
        onPrimary = OneDarkBg,
        secondary = OneDarkGreen,
        onSecondary = OneDarkBg,
        tertiary = OneDarkGold,
        background = OneDarkBg,
        surface = OneDarkSurface,
        surfaceVariant = Color(0xFF2D2D2D),
        onBackground = Color(0xFFD4D4D4),
        onSurface = Color(0xFFD4D4D4),
        outline = Color(0xFF5A5A5A)
    )
    ThemePreset.MATRIX_GREEN -> darkColorScheme(
        primary = MatrixGreen,
        onPrimary = MatrixBlack,
        secondary = MatrixDimGreen,
        onSecondary = MatrixBlack,
        tertiary = MatrixGreen,
        background = MatrixBlack,
        surface = MatrixSurface,
        surfaceVariant = Color(0xFF142417),
        onBackground = MatrixGreen,
        onSurface = MatrixGreen,
        outline = MatrixDimGreen
    )
    ThemePreset.CYBERPUNK_NEON -> darkColorScheme(
        primary = Color(0xFFFF007F),
        onPrimary = Color(0xFF0D0221),
        secondary = Color(0xFF00F0FF),
        onSecondary = Color(0xFF0D0221),
        tertiary = Color(0xFFFFE600),
        background = Color(0xFF0D0221),
        surface = Color(0xFF180838),
        surfaceVariant = Color(0xFF240E54),
        onBackground = Color(0xFFF0F0FF),
        onSurface = Color(0xFFF0F0FF),
        outline = Color(0xFF7000FF)
    )
}

@Composable
fun DevLauncherTheme(
    preset: ThemePreset = ThemePreset.DRACULA,
    fontChoice: FontChoice = FontChoice.JETBRAINS_MONO,
    content: @Composable () -> Unit
) {
    val colorScheme = getDevColorScheme(preset)
    val typography = getDevTypography(fontChoice)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

