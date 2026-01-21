package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.configuration.features.wishlist.AiutaWishlistFeature

@Composable
internal fun LikeButton(
    modifier: Modifier = Modifier,
    isLiked: Boolean,
    iconSize: Dp,
    wishlistFeature: AiutaWishlistFeature,
    onClick: (currentState: Boolean) -> Unit,
) {
    val theme = LocalTheme.current

    Box(
        modifier =
        modifier
            .background(
                color = theme.color.background,
                shape = CircleShape,
            )
            .clickableUnindicated {
                onClick(isLiked)
            },
        contentAlignment = Alignment.Center,
    ) {
        AiutaIcon(
            modifier = Modifier.size(iconSize),
            icon = if (isLiked) {
                wishlistFeature.icons.wishlistFill24
            } else {
                wishlistFeature.icons.wishlist24
            },
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}
