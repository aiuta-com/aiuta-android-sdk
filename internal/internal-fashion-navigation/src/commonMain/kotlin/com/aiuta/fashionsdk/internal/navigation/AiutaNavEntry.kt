package com.aiuta.fashionsdk.internal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
class AiutaNavEntry<T : AiutaNavKey>(
    val key: T,
    val content: @Composable (T) -> Unit,
) {
    @Composable
    public fun Content() {
        this.content(key)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AiutaNavEntry<*>

        return key == other.key &&
            content === other.content
    }

    override fun hashCode(): Int = key.hashCode() * 31 + content.hashCode() * 31
}
