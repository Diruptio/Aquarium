package diruptio.aquarium.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val appColorScheme
    @Composable get() = MaterialTheme.colorScheme.copy(
        primary = Color(0xFFAA00DD),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF111122),
        surface = Color(0xFF333355),
        onSurface = Color(0xFFDDDDDD),
        surfaceContainer = Color(0xFF333355),
        outlineVariant = Color(0xFF888888))

val appTypography
    @Composable get() = MaterialTheme.typography.copy(
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(color = appColorScheme.onPrimary),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(color = appColorScheme.onPrimary),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(color = appColorScheme.onPrimary),
        titleLarge = MaterialTheme.typography.titleLarge.copy(color = appColorScheme.onPrimary),
        titleMedium = MaterialTheme.typography.titleMedium.copy(color = appColorScheme.onPrimary),
        titleSmall = MaterialTheme.typography.titleSmall.copy(color = appColorScheme.onPrimary),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(color = appColorScheme.onSurface),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(color = appColorScheme.onSurface),
        bodySmall = MaterialTheme.typography.bodySmall.copy(color = appColorScheme.onSurface),
        labelLarge = MaterialTheme.typography.labelLarge.copy(color = appColorScheme.onPrimary),
        labelMedium = MaterialTheme.typography.labelMedium.copy(color = appColorScheme.onPrimary),
        labelSmall = MaterialTheme.typography.labelSmall.copy(color = appColorScheme.onPrimary))
