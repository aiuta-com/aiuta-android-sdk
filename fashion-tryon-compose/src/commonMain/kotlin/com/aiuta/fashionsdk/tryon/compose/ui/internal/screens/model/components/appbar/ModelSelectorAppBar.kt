package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.appbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBar
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBarIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.models.GenderTabUiModel

@Composable
internal fun ModelSelectorAppBar(
    genders: List<GenderTabUiModel>,
    activeGenderId: String?,
    onGenderClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    AiutaAppBar(
        modifier = modifier,
        navigationIcon = {
            AiutaAppBarIcon(
                modifier = Modifier.align(Alignment.CenterStart),
                icon = theme.pageBar.icons.back24,
                color = theme.color.primary,
                onClick = onBack,
            )
        },
        title = {
            AnimatedContent(
                modifier = Modifier.align(Alignment.Center),
                targetState = genders,
                contentKey = { it.isNotEmpty() },
                transitionSpec = { fadeIn() togetherWith fadeOut() },
            ) { targetGenders ->
                if (targetGenders.isNotEmpty()) {
                    GenderSelector(
                        genders = targetGenders,
                        activeGenderId = activeGenderId,
                        onGenderClick = onGenderClick,
                    )
                }
            }
        },
    )
}
