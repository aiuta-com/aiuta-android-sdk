package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components.VariantBox
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigUiModel
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig
import kotlin.enums.EnumEntries

@Composable
internal fun BellyShapeScreen(
    configState: State<SizeFitConfigUiModel>,
    updateConfig: (SizeFitConfigUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ShapeBlock(
            title = sizeFitFeature.strings.bellyShapeTitle,
            items = AiutaSizeFitConfig.BellyShape.entries,
            itemToString = { item ->
                when (item) {
                    AiutaSizeFitConfig.BellyShape.FLAT -> "Flat"
                    AiutaSizeFitConfig.BellyShape.NORMAL -> "Average"
                    AiutaSizeFitConfig.BellyShape.CURVY -> "Full"
                }
            },
            selectedItem = configState.value.bellyShape,
            onClick = { item ->
                updateConfig(configState.value.copy(bellyShape = item))
            },
        )

        Spacer(Modifier.weight(1f))

        ShapeBlock(
            title = sizeFitFeature.strings.hipsShapeTitle,
            items = AiutaSizeFitConfig.HipShape.entries,
            itemToString = { item ->
                when (item) {
                    AiutaSizeFitConfig.HipShape.SLIM -> "Straight"
                    AiutaSizeFitConfig.HipShape.NORMAL -> "Average"
                    AiutaSizeFitConfig.HipShape.CURVY -> "Wide"
                }
            },
            selectedItem = configState.value.hipShape,
            onClick = { item ->
                updateConfig(configState.value.copy(hipShape = item))
            },
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun <T : Enum<T>> ShapeBlock(
    title: String,
    items: EnumEntries<T>,
    itemToString: (T) -> String,
    selectedItem: T?,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = theme.label.typography.titleM,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            items.forEach { item ->
                key(item.name) {
                    val isSelected = remember(selectedItem) {
                        derivedStateOf { item == selectedItem }
                    }

                    VariantBox(
                        text = itemToString(item),
                        isSelected = isSelected.value,
                        onClick = { onClick(item) },
                        textStyle = theme.label.typography.subtle,
                        modifier = Modifier.fillMaxHeight().weight(1f),
                    )
                }
            }
        }
    }
}
