package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.then
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils.animateTextStyleAsState
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun QuestionaryTextField(
    state: TextFieldState,
    labelText: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
) {
    val theme = LocalTheme.current
    val sharedShape = RoundedCornerShape(16.dp)

    val isTextFieldEmpty = remember {
        derivedStateOf { state.text.isNotEmpty() }
    }
    val isTextFieldFocused = remember { mutableStateOf(false) }
    val questionaryColors = TextFieldDefaults.textFieldColors(
        textColor = theme.color.primary,
        backgroundColor = theme.color.background,
        cursorColor = theme.color.brand,
        placeholderColor = theme.color.secondary,
        errorLabelColor = theme.errorSnackbar.colors.errorBackground,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        errorCursorColor = theme.errorSnackbar.colors.errorBackground,
    )
    val labelTextStyle = animateTextStyleAsState(
        targetValue = if (isTextFieldFocused.value || isTextFieldEmpty.value) {
            theme.productBar.typography.brand
        } else {
            theme.label.typography.regular
        },
    )
    val labelTextColor = animateColorAsState(
        targetValue = if (isError) {
            theme.errorSnackbar.colors.errorBackground
        } else {
            theme.color.border
        },
    )

    val currentOnTextChange by rememberUpdatedState(onTextChange)
    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }.collectLatest {
            currentOnTextChange(it)
        }
    }

    TextField(
        state = state,
        modifier = modifier
            .height(68.dp)
            .border(
                width = 1.dp,
                color = theme.color.border,
                shape = sharedShape,
            )
            .onFocusChanged { state ->
                isTextFieldFocused.value = state.isFocused
            },
        enabled = enabled,
        label = {
            Text(
                text = labelText,
                style = labelTextStyle.value,
                color = labelTextColor.value,
            )
        },
        trailingIcon = {
            AnimatedContent(
                targetState = isTextFieldEmpty.value,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
            ) { isCleanVisible ->
                if (isCleanVisible) {
                    AiutaIcon(
                        icon = theme.pageBar.icons.close24,
                        contentDescription = null,
                        tint = theme.color.border,
                        modifier = Modifier.clickableUnindicated(
                            onClick = state::clearText,
                        ),
                    )
                } else {
                    trailingIcon?.invoke()
                }
            }
        },
        inputTransformation = InputTransformation.then {
            if (!asCharSequence().isDigitsOnly()) {
                revertAllChanges()
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
        ),
        textStyle = theme.label.typography.regular,
        isError = isError,
        lineLimits = TextFieldLineLimits.SingleLine,
        shape = sharedShape,
        colors = questionaryColors,
    )
}

private fun CharSequence.isDigitsOnly() = all(Char::isDigit)
