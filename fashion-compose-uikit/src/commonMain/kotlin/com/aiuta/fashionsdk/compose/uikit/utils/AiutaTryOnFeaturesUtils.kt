package com.aiuta.fashionsdk.compose.uikit.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.aiuta.fashionsdk.compose.uikit.composition.LocalAiutaFeatures
import com.aiuta.fashionsdk.configuration.features.AiutaFeature

@Composable
@ReadOnlyComposable
public inline fun <reified T : AiutaFeature> provideFeature(): T? {
    val features = LocalAiutaFeatures.current
    return features.provideFeature()
}

@Composable
@ReadOnlyComposable
public inline fun <reified T : AiutaFeature> strictProvideFeature(): T {
    val features = LocalAiutaFeatures.current
    return features.strictProvideFeature()
}

@Composable
@ReadOnlyComposable
public inline fun <reified T : AiutaFeature> isFeatureInitialize(): Boolean {
    val features = LocalAiutaFeatures.current
    return features.isFeatureInitialize<T>()
}
