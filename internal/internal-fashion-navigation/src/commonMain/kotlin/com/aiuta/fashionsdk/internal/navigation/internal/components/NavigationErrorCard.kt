package com.aiuta.fashionsdk.internal.navigation.internal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaErrorSnackbarController
import com.aiuta.fashionsdk.internal.navigation.snackbar.AiutaErrorSnackbarState

internal const val DEFAULT_SHOWING_DELAY = 3000L

@Composable
internal fun NavigationErrorCard(
    modifier: Modifier = Modifier,
    errorState: AiutaErrorSnackbarState,
) {
    val errorSnackbarController = LocalAiutaErrorSnackbarController.current
    val theme = LocalTheme.current

    Row(
        modifier =
        modifier
            .background(
                color = theme.errorSnackbar.colors.errorBackground,
                shape = theme.button.shapes.buttonMShape,
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AiutaIcon(
            modifier = Modifier.size(36.dp),
            icon = theme.errorSnackbar.icons.error36,
            contentDescription = null,
            tint = theme.errorSnackbar.colors.errorPrimary,
        )

        Spacer(Modifier.width(16.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = errorState.message ?: theme.errorSnackbar.strings.defaultErrorMessage,
            style = theme.label.typography.subtle,
            color = theme.errorSnackbar.colors.errorPrimary,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.width(16.dp))

        FashionButton(
            text = theme.errorSnackbar.strings.tryAgainButton,
            style = FashionButtonStyles.secondaryStyle(
                backgroundColor = theme.color.background,
                contentColor = theme.color.primary,
                borderColor = Color.Transparent,
            ),
            size = FashionButtonSizes.mSize(),
            onClick = {
                errorState.onRetry()
                errorSnackbarController.hideErrorState()
            },
        )
    }
}
