package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.shoes.components.appbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBar
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBarIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaConfiguration
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.selector.GenderSelector
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.models.GenderTabUiModel

/**
 * SHOES mode app bar — a centred icon + page title row (back navigation on the side), with the
 * gender toggle stretched full-width below it.
 */
@Composable
internal fun ShoesModelSelectorAppBar(
    genders: List<GenderTabUiModel>,
    activeGenderId: String?,
    onGenderClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    val configuration = LocalAiutaConfiguration.current

    val pageTitle = configuration.modes.shoes
        ?.imagePicker
        ?.predefinedModels
        ?.strings
        ?.predefinedModelShoesPageTitle
        .orEmpty()

    Column(
        modifier = modifier,
    ) {
        AiutaAppBar(
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                AiutaAppBarIcon(
                    modifier = Modifier.align(Alignment.CenterStart),
                    icon = theme.pageBar.icons.back24,
                    color = theme.color.primary,
                    onClick = onBack,
                )
            },
            title = {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = pageTitle,
                    style = theme.pageBar.typography.pageTitle,
                    color = theme.color.primary,
                    textAlign = TextAlign.Center,
                )
            },
        )

        Spacer(Modifier.height(6.dp))

        AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = genders,
            contentKey = { it.isNotEmpty() },
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { targetGenders ->
            if (targetGenders.isNotEmpty()) {
                GenderSelector(
                    modifier = Modifier.fillMaxWidth(),
                    genders = targetGenders,
                    activeGenderId = activeGenderId,
                    onGenderClick = onGenderClick,
                )
            }
        }
    }
}
