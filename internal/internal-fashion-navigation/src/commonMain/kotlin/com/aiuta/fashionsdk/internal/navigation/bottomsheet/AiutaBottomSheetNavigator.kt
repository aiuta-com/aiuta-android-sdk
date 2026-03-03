package com.aiuta.fashionsdk.internal.navigation.bottomsheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.internal.navigation.AiutaNavEntry
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun rememberAiutaBottomSheetNavigator(
    logger: AiutaLogger?,
    bottomSheetEntryProvider: (AiutaNavigationBottomSheetScreen) -> AiutaNavEntry<AiutaNavigationBottomSheetScreen>,
): AiutaBottomSheetNavigator {
    val defaultScope = rememberCoroutineScope()
    val defaultBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    return remember(
        defaultScope,
        defaultBottomSheetState,
        logger,
    ) {
        AiutaBottomSheetNavigator(
            scope = defaultScope,
            bottomSheetEntryProvider = bottomSheetEntryProvider,
            logger = logger,
            sheetState = defaultBottomSheetState,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Immutable
public class AiutaBottomSheetNavigator internal constructor(
    private val scope: CoroutineScope,
    private val bottomSheetEntryProvider: (AiutaNavigationBottomSheetScreen) -> AiutaNavEntry<AiutaNavigationBottomSheetScreen>,
    private val logger: AiutaLogger?,
    internal val sheetState: ModalBottomSheetState,
) {
    private val currentBottomSheetScreen =
        mutableStateOf<AiutaNavigationBottomSheetScreen>(AiutaNavigationBottomSheetScreen.IDLE)
    public val lastBottomSheetScreen: MutableState<AiutaNavigationBottomSheetScreen> = mutableStateOf(currentBottomSheetScreen.value)

    internal val sheetContent: @Composable ColumnScope.() -> Unit =
        {
            val key = currentBottomSheetScreen.value
            if (key !is AiutaNavigationBottomSheetScreen.IDLE) {
                bottomSheetEntryProvider(key).Content()
            }

            Spacer(Modifier.height(8.dp))
            Spacer(Modifier.windowInsetsPadding(WindowInsets.navigationBars))
        }

    public val isVisible: Boolean
        get() = sheetState.isVisible

    public fun show(newSheetScreen: AiutaNavigationBottomSheetScreen) {
        scope.launch {
            logger?.d("AiutaBottomSheetNavigator: show $newSheetScreen")

            currentBottomSheetScreen.value = newSheetScreen
            sheetState.show()
        }
    }

    public fun hide() {
        scope.launch {
            logger?.d("AiutaBottomSheetNavigator: hide")

            sheetState.hide()
            lastBottomSheetScreen.value = currentBottomSheetScreen.value
            currentBottomSheetScreen.value = AiutaNavigationBottomSheetScreen.IDLE
        }
    }

    public fun change(newSheetScreen: AiutaNavigationBottomSheetScreen) {
        scope.launch {
            logger?.d("AiutaBottomSheetNavigator: change to $newSheetScreen")

            sheetState.hide()
            currentBottomSheetScreen.value = newSheetScreen
            sheetState.show()
        }
    }
}
