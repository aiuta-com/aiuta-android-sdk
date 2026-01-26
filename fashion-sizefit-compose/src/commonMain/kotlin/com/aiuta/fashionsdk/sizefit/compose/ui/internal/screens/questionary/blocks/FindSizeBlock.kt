package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.buildAnnotatedStringFromHtml
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components.QuestionaryTextField
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components.VariantBox
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigUiModel
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig

@Composable
internal fun FindSizeBlock(
    configState: State<SizeFitConfigUiModel>,
    shouldShowErrorState: State<Boolean>,
    updateConfig: (SizeFitConfigUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()

    val textFieldModifier = Modifier.fillMaxWidth()
    val ageTextFieldState = rememberTextFieldState(
        initialText = configState.value.age?.let { "$it" } ?: "",
    )
    val heightTextFieldState = rememberTextFieldState(
        initialText = configState.value.height?.let { "$it" } ?: "",
    )
    val weightTextFieldState = rememberTextFieldState(
        initialText = configState.value.weight?.let { "$it" } ?: "",
    )

    val isAgeInputError = remember { mutableStateOf(false) }
    val isHeightInputError = remember { mutableStateOf(false) }
    val isWeightInputError = remember { mutableStateOf(false) }

    val suffixIcon: @Composable (suffixText: String) -> Unit = { suffixText ->
        Text(
            text = suffixText,
            style = theme.label.typography.subtle.copy(
                fontWeight = FontWeight.Medium,
            ),
            color = sizeFitFeature.styles.suffixColor,
        )
    }

    LaunchedEffect(shouldShowErrorState.value) {
        if (shouldShowErrorState.value) {
            val config = configState.value

            config.run {
                isAgeInputError.value = age == null || age <= 0
                isHeightInputError.value = height == null || height <= 0
                isWeightInputError.value = weight == null || weight <= 0
            }
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = sizeFitFeature.strings.questionaryTitle,
            style = theme.label.typography.titleM,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(40.dp))

        GenderSubBlock(
            configState = configState,
            updateConfig = updateConfig,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(20.dp))

        QuestionaryTextField(
            state = ageTextFieldState,
            labelText = sizeFitFeature.strings.agePlaceholder,
            modifier = textFieldModifier,
            isError = isAgeInputError.value,
            onTextChange = {
                updateConfig(configState.value.copy(age = it.toIntOrNull()))
            },
        )

        Spacer(Modifier.height(12.dp))

        QuestionaryTextField(
            state = heightTextFieldState,
            labelText = sizeFitFeature.strings.heightPlaceholder,
            modifier = textFieldModifier,
            trailingIcon = {
                suffixIcon(sizeFitFeature.strings.heightSuffix)
            },
            isError = isHeightInputError.value,
            onTextChange = {
                updateConfig(configState.value.copy(height = it.toIntOrNull()))
            },
        )

        Spacer(Modifier.height(12.dp))

        QuestionaryTextField(
            state = weightTextFieldState,
            labelText = sizeFitFeature.strings.weightPlaceholder,
            modifier = textFieldModifier,
            trailingIcon = {
                suffixIcon(sizeFitFeature.strings.weightSuffix)
            },
            isError = isWeightInputError.value,
            onTextChange = {
                updateConfig(configState.value.copy(weight = it.toIntOrNull()))
            },
        )

        Spacer(Modifier.weight(1f))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedStringFromHtml(sizeFitFeature.strings.privacyPolicyHtml),
            style = theme.productBar.typography.product,
            color = theme.color.secondary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun GenderSubBlock(
    configState: State<SizeFitConfigUiModel>,
    updateConfig: (SizeFitConfigUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()
    val genderList = remember { AiutaSizeFitConfig.Gender.entries }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        val sharedModifier = Modifier.height(68.dp).weight(1f)

        genderList.forEach { gender ->
            key(gender.name) {
                val isSelected = remember {
                    derivedStateOf {
                        configState.value.gender == gender
                    }
                }

                VariantBox(
                    text = when (gender) {
                        AiutaSizeFitConfig.Gender.FEMALE -> sizeFitFeature.strings.genderFemale
                        AiutaSizeFitConfig.Gender.MALE -> sizeFitFeature.strings.genderMale
                    },
                    icon = when (gender) {
                        AiutaSizeFitConfig.Gender.FEMALE -> sizeFitFeature.icons.female20
                        AiutaSizeFitConfig.Gender.MALE -> sizeFitFeature.icons.male20
                    },
                    isSelected = isSelected.value,
                    onClick = {
                        updateConfig(configState.value.copy(gender = gender))
                    },
                    modifier = sharedModifier,
                )
            }
        }
    }
}
