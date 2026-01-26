package com.aiuta.fashionsdk.internal.navigation

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

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
    private val clazzProviders = mutableMapOf<KClass<out T>, AiutaEntryClassProvider<out T>>()
    private val providers = mutableMapOf<T, AiutaEntryProvider<out T>>()

    public fun <K : T> addEntryProvider(
        key: K,
        content: @Composable (K) -> Unit,
    ) {
        require(key !in providers) {
            "An `entry` with the key `key` has already been added: $key."
        }
        providers[key] = AiutaEntryProvider(
            key = key,
            content = content,
        )
    }

    public fun <K : T> addEntryProvider(
        clazz: KClass<out K>,
        content: @Composable (K) -> Unit,
    ) {
        require(clazz !in clazzProviders) {
            "An `entry` with the same `clazz` has already been added: ${clazz.simpleName}."
        }
        clazzProviders[clazz] = AiutaEntryClassProvider(
            clazz = clazz,
            content = content,
        )
    }

    public fun <K : T> aiutaEntry(
        key: K,
        content: @Composable (K) -> Unit,
    ) {
        addEntryProvider(key, content)
    }

    public inline fun <reified K : T> aiutaEntry(
        noinline content: @Composable (K) -> Unit,
    ) {
        addEntryProvider(K::class, content)
    }

    public fun build(): (T) -> AiutaNavEntry<T> = { key ->
        val entryClassProvider = clazzProviders[key::class] as? AiutaEntryClassProvider<T>
        val entryProvider = providers[key] as? AiutaEntryProvider<T>
        entryClassProvider?.run { AiutaNavEntry(key, content) }
            ?: entryProvider?.run { AiutaNavEntry(key, content) }
            ?: fallback.invoke(key)
    }
}

private data class AiutaEntryClassProvider<T : AiutaNavKey>(
    val clazz: KClass<T>,
    val content: @Composable (T) -> Unit,
)

private data class AiutaEntryProvider<T : AiutaNavKey>(
    val key: T,
    val content: @Composable (T) -> Unit,
)
