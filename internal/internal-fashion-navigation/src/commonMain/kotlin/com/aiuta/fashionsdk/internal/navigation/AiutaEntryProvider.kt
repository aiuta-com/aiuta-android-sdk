package com.aiuta.fashionsdk.internal.navigation

import androidx.compose.runtime.Composable

@DslMarker
public annotation class AiutaEntryDsl

public inline fun <T : AiutaNavKey> aiutaEntryProvider(
    noinline fallback: (unknownScreen: T) -> AiutaNavEntry<T> = {
        throw IllegalStateException("Unknown screen $it")
    },
    builder: AiutaEntryProviderScope<T>.() -> Unit,
): (T) -> AiutaNavEntry<T> = AiutaEntryProviderScope(fallback).apply(builder).build()

@AiutaEntryDsl
public class AiutaEntryProviderScope<T : AiutaNavKey>(
    private val fallback: (unknownScreen: T) -> AiutaNavEntry<T>,
) {
    private val providers = mutableMapOf<T, AiutaNavEntry<T>>()

    public fun addEntryProvider(
        key: T,
        content: @Composable (T) -> Unit,
    ) {
        require(key !in providers) {
            "An `entry` with the key `key` has already been added: $key."
        }
        providers[key] = AiutaNavEntry(
            key = key,
            content = content,
        )
    }

    public fun AiutaEntryProviderScope<T>.aiutaEntry(
        key: T,
        content: @Composable (T) -> Unit,
    ) {
        addEntryProvider(key, content)
    }

    fun build(): (T) -> AiutaNavEntry<T> = { key ->
        providers[key] ?: fallback.invoke(key)
    }
}
