package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components.appbar.QuestionaryAppBar

@Composable
internal fun QuestionaryScreen(
    modifier: Modifier = Modifier,
) {
    val navigationController = LocalAiutaNavigationController.current

    val viewModel: QuestionaryViewModel = viewModel {
        QuestionaryViewModel(
            onBack = navigationController::navigateBack,
        )
    }
    val stepState = viewModel.currentStep.collectAsState()
    val configState = viewModel.configState.collectAsState()

    Column(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        QuestionaryAppBar(
            stepState = stepState,
            navigateBack = viewModel::navigateBack,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(64.dp))
    }
}
