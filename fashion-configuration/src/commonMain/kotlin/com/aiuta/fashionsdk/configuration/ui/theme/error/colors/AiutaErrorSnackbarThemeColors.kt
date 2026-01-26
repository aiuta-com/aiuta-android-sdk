package com.aiuta.fashionsdk.configuration.ui.theme.error.colors

import androidx.compose.ui.graphics.Color

/**
 * Color configuration for error snackbar notifications.
 *
 * This interface defines the colors used in error snackbar notifications
 * throughout the SDK. It provides consistent error styling that can be
 * customized to match your app's design system.
 *
 *
 * @property errorBackground Background color for the error snackbar
 * @property errorPrimary Primary text color for error messages
 */
public interface AiutaErrorSnackbarThemeColors {
    public val errorBackground: Color
    public val errorPrimary: Color

    /**
     * Default implementation of [AiutaErrorSnackbarThemeColors].
     *
     * This class provides standard colors for error snackbars
     *
     * ```kotlin
     * theme {
     *     errorSnackbar {
     *         colors = AiutaErrorSnackbarThemeColors.Default()
     *     }
     * }
     * ```
     */
    public class Default : AiutaErrorSnackbarThemeColors {
        override val errorBackground: Color = Color(0xFFEF5754)
        override val errorPrimary: Color = Color(0xFFFFFFFF)
    }
}
