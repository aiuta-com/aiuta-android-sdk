package com.aiuta.fashionsdk.compose.resources.drawable

import android.graphics.drawable.Drawable

/**
 * Android implementation of [AiutaDrawableResource] using Drawable objects.
 *
 * This implementation allows you to use Android Drawable instances directly
 * within the Aiuta SDK. This is useful when you have programmatically created
 * drawables or when working with drawables obtained from other Android APIs.
 *
 *
 * @property resource The Android Drawable instance
 * @see AiutaDrawableResource
 */
public class AiutaAndroidDrawable(
    public override val resource: Drawable,
) : AiutaDrawableResource

/**
 * Android implementation of [AiutaDrawableResource] using drawable resource IDs.
 *
 * This implementation allows you to reference Android drawable resources by their
 * resource ID. This is the most common way to use drawable resources in Android
 * applications and provides compile-time safety for resource references.
 *
 *
 * @property resource The Android drawable resource ID
 * @see AiutaDrawableResource
 */
public class AiutaAndroidDrawableRes(
    public override val resource: Int,
) : AiutaDrawableResource
