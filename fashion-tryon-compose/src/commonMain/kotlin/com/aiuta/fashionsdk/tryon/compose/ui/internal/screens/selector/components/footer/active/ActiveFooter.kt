package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.footer.active

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isSingleTryOnMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.shadow.dropShadow

@Composable
internal fun ActiveFooter(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val isSingleTryOnMode = controller.isSingleTryOnMode()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))

        Column(
            modifier = Modifier
                .dropShadow(
                    shape = theme.bottomSheet.shapes.bottomSheetShape,
                    color = theme.color.primary.copy(alpha = 0.04f),
                    blur = 15.dp,
                    offsetY = (-10).dp,
                )
                .background(
                    color = theme.color.background,
                    shape = theme.bottomSheet.shapes.bottomSheetShape,
                )
                .padding(
                    horizontal = when (isSingleTryOnMode.value) {
                        true -> 16.dp
                        false -> 20.dp
                    },
                ),
        ) {
            when (isSingleTryOnMode.value) {
                true -> {
                    SingleTryOnFooterContent(modifier = Modifier.fillMaxWidth())
                }

                false -> {
                    MultiTryOnFooterContent(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
