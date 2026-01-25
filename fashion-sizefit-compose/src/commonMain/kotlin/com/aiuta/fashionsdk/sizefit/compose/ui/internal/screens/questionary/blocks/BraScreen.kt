package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components.VariantBox
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig

@Composable
internal fun BraScreen(
    configState: State<SizeFitConfigState>,
    updateConfig: (SizeFitConfigState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()
    val chestItems = remember {
        (60..125 step 5).map { size ->
            SizeItem(
                item = size,
                text = "$size",
            )
        }
    }
    val cupItems = remember {
        AiutaSizeFitConfig.BraCup.entries.map { size ->
            SizeItem(
                item = size,
                text = when (size) {
                    AiutaSizeFitConfig.BraCup.AA -> "AA"
                    AiutaSizeFitConfig.BraCup.A -> "A"
                    AiutaSizeFitConfig.BraCup.B -> "B"
                    AiutaSizeFitConfig.BraCup.C -> "C"
                    AiutaSizeFitConfig.BraCup.D -> "D"
                    AiutaSizeFitConfig.BraCup.DD -> "DD"
                    AiutaSizeFitConfig.BraCup.E -> "E"
                    AiutaSizeFitConfig.BraCup.F -> "F"
                    AiutaSizeFitConfig.BraCup.G -> "G"
                },
            )
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = sizeFitFeature.strings.braSizeTitle,
            style = theme.label.typography.titleM,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(50.dp))

        SizeBlock(
            title = sizeFitFeature.strings.chestCircumferenceSubtitle,
            items = chestItems,
            selectedItem = configState.value.braSize,
            onClick = { sizeItem ->
                updateConfig(configState.value.copy(braSize = sizeItem.item))
            },
        )

        Spacer(Modifier.weight(1f))

        SizeBlock(
            title = sizeFitFeature.strings.braCupSubtitle,
            items = cupItems,
            selectedItem = configState.value.braCup,
            onClick = { sizeItem ->
                updateConfig(configState.value.copy(braCup = sizeItem.item))
            },
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun <T> SizeBlock(
    title: String,
    items: List<SizeItem<T>>,
    selectedItem: T?,
    onClick: (SizeItem<T>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val sharedItemModifier = Modifier.width(52.dp).height(50.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = theme.label.typography.regular,
            color = theme.color.primary,
            textAlign = TextAlign.Start,
        )

        Spacer(Modifier.height(20.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items.forEach { item ->
                val isSelected = remember(selectedItem) {
                    derivedStateOf { item.item == selectedItem }
                }

                VariantBox(
                    text = item.text,
                    isSelected = isSelected.value,
                    onClick = { onClick(item) },
                    textStyle = theme.label.typography.subtle,
                    modifier = sharedItemModifier,
                )
            }
        }
    }
}

private data class SizeItem<T>(
    val item: T,
    val text: String,
)
