package com.aiuta.fashionsdk.compose.tokens

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.compose.tokens.color.AiutaColors
import com.aiuta.fashionsdk.compose.tokens.color.DefaultAiutaColors
import com.aiuta.fashionsdk.compose.tokens.typography.AiutaTypography
import com.aiuta.fashionsdk.compose.tokens.typography.DefaultAiutaTypography

/**
 * Final theme for all UI flows
 */
@Immutable
public class AiutaTheme(
    @Deprecated("Should delete in new release")
    public val navBarTheme: AiutaNavBarTheme,
    public val colors: AiutaColors,
    public val typography: AiutaTypography,
    @get:DrawableRes
    public val watermarkRes: Int?,
)

public fun aiutaTheme(
    colors: AiutaColors = DefaultAiutaColors,
    typography: AiutaTypography = DefaultAiutaTypography,
    @DrawableRes watermarkRes: Int? = null,
): AiutaTheme {
    return AiutaTheme(
        colors = colors,
        typography = typography,
        watermarkRes = watermarkRes,
        navBarTheme = defaultAiutaNavBarTheme(),
    )
}
