package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface QuestionaryStep {

    val previousStep: QuestionaryStep?

    object FindSizeStep : QuestionaryStep {
        override val previousStep: QuestionaryStep? = null
    }

    object BellyShapeStep : QuestionaryStep {
        override val previousStep: QuestionaryStep = FindSizeStep
    }

    object BraStep : QuestionaryStep {
        override val previousStep: QuestionaryStep = BellyShapeStep
    }
}
