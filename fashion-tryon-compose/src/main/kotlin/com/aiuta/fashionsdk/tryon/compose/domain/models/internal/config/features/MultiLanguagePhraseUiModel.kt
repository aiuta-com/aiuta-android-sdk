package com.aiuta.fashionsdk.tryon.compose.domain.models.internal.config.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.config.features.MultiLanguagePhrase
import com.aiuta.fashionsdk.tryon.compose.domain.internal.language.isoCode
import com.aiuta.fashionsdk.tryon.compose.domain.models.AiutaTryOnLanguage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalAiutaTryOnStringResources

@Immutable
internal class MultiLanguagePhraseUiModel(
    val englishTranslation: String? = null,
    val russianTranslation: String? = null,
    val turkishTranslation: String? = null,
)

internal fun MultiLanguagePhrase.toUiModel(): MultiLanguagePhraseUiModel {
    return MultiLanguagePhraseUiModel(
        englishTranslation = englishTranslation,
        russianTranslation = russianTranslation,
        turkishTranslation = turkishTranslation,
    )
}

@Composable
internal fun MultiLanguagePhraseUiModel.toTranslatedString(): String? {
    val stringResources = LocalAiutaTryOnStringResources.current
    val currentIsoCode = stringResources.isoCode()

    return when (currentIsoCode) {
        AiutaTryOnLanguage.ENGLISH.code -> englishTranslation
        AiutaTryOnLanguage.RUSSIAN.code -> russianTranslation
        AiutaTryOnLanguage.TURKISH.code -> turkishTranslation
        else -> throw IllegalStateException("Not supported language")
    }
}
