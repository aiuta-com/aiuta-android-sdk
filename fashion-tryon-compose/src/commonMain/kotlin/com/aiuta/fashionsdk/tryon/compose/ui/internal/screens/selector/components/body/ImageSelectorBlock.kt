package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

@Composable
internal fun ImageSelectorBlock(
    modifier: Modifier = Modifier,
    uploadPhoto: () -> Unit,
) {
    val theme = LocalTheme.current
    val hazeState = remember { HazeState() }

    Box(
        modifier = modifier.background(
            color = theme.color.neutral,
            shape = theme.image.shapes.imageLShape,
        ),
    ) {
        ImageSelectorPhoto(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(hazeState),
        )

        ImageSelectorBottom(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            hazeState = hazeState,
            uploadPhoto = uploadPhoto,
        )
    }
}
