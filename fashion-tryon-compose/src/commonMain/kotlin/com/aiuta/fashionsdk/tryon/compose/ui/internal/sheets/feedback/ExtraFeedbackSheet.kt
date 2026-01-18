package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.feedback

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.other.AiutaTryOnFeedbackOtherFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic.sendGenerationFeedback
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.tryon.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun ExtraFeedbackSheet(
    data: TryOnBottomSheetScreen.ExtraFeedback,
    modifier: Modifier = Modifier,
) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val controller = LocalController.current
    val theme = LocalTheme.current

    val otherFeedbackFeature = strictProvideFeature<AiutaTryOnFeedbackOtherFeature>()

    val feedbackText = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .windowInsetsPadding(WindowInsets.ime),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(14.dp))

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .clickableUnindicated {
                    bottomSheetNavigator.hide()
                },
            text = otherFeedbackFeature.strings.otherFeedbackButtonCancel,
            style = theme.label.typography.regular,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(32.dp))

        Text(
            modifier = Modifier.align(Alignment.Start),
            text = otherFeedbackFeature.strings.otherFeedbackTitle,
            style = theme.label.typography.titleL,
            color = theme.color.primary,
            textAlign = TextAlign.Start,
        )

        Spacer(Modifier.height(20.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 160.dp)
                .animateContentSize(),
            value = feedbackText.value,
            textStyle = theme.label.typography.regular,
            onValueChange = { feedbackText.value = it },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = theme.color.primary,
                backgroundColor = theme.color.neutral,
                cursorColor = theme.color.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )

        Spacer(Modifier.weight(1f))

        FashionButton(
            modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.ime),
            text = otherFeedbackFeature.strings.otherFeedbackButtonSend,
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            onClick = {
                controller.sendGenerationFeedback(
                    optionIndex = data.optionIndex,
                    feedback = feedbackText.value,
                    productIds = data.productIds,
                )
                bottomSheetNavigator.hide()
            },
        )

        Spacer(Modifier.height(8.dp))
    }
}
