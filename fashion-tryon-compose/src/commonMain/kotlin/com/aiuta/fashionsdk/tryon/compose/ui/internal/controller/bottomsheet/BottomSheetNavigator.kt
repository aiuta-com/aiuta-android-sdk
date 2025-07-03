package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.disclaimer.FitDisclaimerSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.feedback.ExtraFeedbackSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.feedback.FeedbackSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.operations.GeneratedOperationsSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.picker.ImagePickerSheet
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.skuinfo.ProductInfoSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun rememberBottomSheetNavigator(): BottomSheetNavigator {
    val defaultScope = rememberCoroutineScope()

    val defaultBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        )

    val defaultBottomSheetScreen =
        remember {
            mutableStateOf<NavigationBottomSheetScreen>(NavigationBottomSheetScreen.IDLE)
        }

    return remember {
        BottomSheetNavigator(
            scope = defaultScope,
            sheetState = defaultBottomSheetState,
            currentBottomSheetScreen = defaultBottomSheetScreen,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Immutable
internal class BottomSheetNavigator(
    private val scope: CoroutineScope,
    internal val sheetState: ModalBottomSheetState,
    public val currentBottomSheetScreen: MutableState<NavigationBottomSheetScreen>,
) {
    val lastBottomSheetScreen = mutableStateOf(currentBottomSheetScreen.value)

    public val sheetContent: @Composable ColumnScope.() -> Unit = {
        when (val bottomScreen = currentBottomSheetScreen.value) {
            is NavigationBottomSheetScreen.ImagePicker -> {
                ImagePickerSheet(pickerData = bottomScreen)
            }

            is NavigationBottomSheetScreen.ProductInfo -> {
                ProductInfoSheet(
                    productInfo = bottomScreen,
                )
            }

            is NavigationBottomSheetScreen.Feedback -> {
                FeedbackSheet(args = bottomScreen)
            }

            is NavigationBottomSheetScreen.ExtraFeedback -> {
                ExtraFeedbackSheet(
                    data = bottomScreen,
                )
            }

            is NavigationBottomSheetScreen.FitDisclaimer -> {
                FitDisclaimerSheet()
            }

            is NavigationBottomSheetScreen.GeneratedOperations -> {
                GeneratedOperationsSheet()
            }

            is NavigationBottomSheetScreen.IDLE -> Unit
        }

        Spacer(Modifier.height(8.dp))
        Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
    }

    public fun show(newSheetScreen: NavigationBottomSheetScreen) {
        scope.launch {
            currentBottomSheetScreen.value = newSheetScreen
            sheetState.show()
        }
    }

    public fun hide() {
        scope.launch {
            sheetState.hide()
            lastBottomSheetScreen.value = currentBottomSheetScreen.value
            currentBottomSheetScreen.value = NavigationBottomSheetScreen.IDLE
        }
    }

    public fun change(newSheetScreen: NavigationBottomSheetScreen) {
        scope.launch {
            sheetState.hide()
            currentBottomSheetScreen.value = newSheetScreen
            sheetState.show()
        }
    }
}
