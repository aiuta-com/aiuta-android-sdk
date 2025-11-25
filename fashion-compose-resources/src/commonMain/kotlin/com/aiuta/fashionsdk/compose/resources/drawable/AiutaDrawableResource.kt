package com.aiuta.fashionsdk.compose.resources.drawable

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.DrawableResource

/**
 * Platform-agnostic interface for drawable resources in the Aiuta SDK.
 *
 * This interface provides a unified way to handle drawable resources across
 * different platforms (Android, iOS, Desktop) within the Aiuta SDK. Each
 * platform can provide its own implementation while maintaining a consistent API.
 *
 * Platform implementations:
 * - Android: AiutaAndroidDrawable, AiutaAndroidDrawableRes
 * - Compose Multiplatform: [AiutaComposeDrawableResource]
 * - iOS/Native: AiutaIOSSkia
 *
 * ```kotlin
 * // Using with Compose Multiplatform resources
 * val icon = AiutaComposeDrawableResource(Res.drawable.my_icon)
 *
 * // Using with Android drawable resource
 * val androidIcon = AiutaAndroidDrawableRes(R.drawable.my_icon)
 * ```
 *
 * @property resource Platform-specific drawable resource object
 */
@Immutable
public interface AiutaDrawableResource {
    public val resource: Any?
}

/**
 * Compose Multiplatform implementation of [AiutaDrawableResource].
 *
 * This implementation wraps Jetpack Compose's [DrawableResource] for use
 * in cross-platform Compose applications. It's the recommended way to
 * handle drawable resources in Compose Multiplatform projects.
 *
 *
 * @property resource The underlying [DrawableResource] from Compose Multiplatform
 * @see DrawableResource
 * @see AiutaDrawableResource
 */
public class AiutaComposeDrawableResource(
    public override val resource: DrawableResource,
) : AiutaDrawableResource
