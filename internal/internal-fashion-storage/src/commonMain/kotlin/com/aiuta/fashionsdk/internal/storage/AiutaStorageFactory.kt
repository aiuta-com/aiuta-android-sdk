package com.aiuta.fashionsdk.internal.storage

import com.aiuta.fashionsdk.context.AiutaPlatformContext

/**
 * Factory for creating isolated DataStore-backed storage instances.
 *
 * Each instance is identified by a unique namespace and stores data independently.
 * Multiple calls with the same namespace and context return the same instance (singleton per namespace).
 *
 * Usage example:
 * ```kotlin
 * // Using Aiuta SDK context
 * val sizeFitStorage = AiutaStorageFactory.create("size_fit_preferences", aiuta.platformContext)
 *
 * // Using Android Context directly
 * val featureStorage = AiutaStorageFactory.create("feature_preferences", applicationContext)
 * ```
 *
 * @see AiutaStorage for available operations
 */
public object AiutaStorageFactory {

    private val namespaceRegex = Regex("^[A-Za-z0-9_-]+$")

    /**
     * Creates or retrieves a storage instance for the given namespace.
     *
     * The namespace is used to isolate data between different features.
     * Each namespace has its own DataStore file on disk.
     *
     * On Android, the files are stored in the app's private directory:
     * `{filesDir}/datastore/{name}.preferences_pb`
     *
     * On other platforms, platform-specific storage locations are used.
     *
     * @param name Unique namespace identifier (e.g., "size_fit_preferences").
     *             Must be a valid filename (alphanumeric, underscore, hyphen).
     *             Must not be empty.
     * @param platformContext Platform-specific context. On Android, this is
     *                        android.content.Context. On other platforms, use
     *                        AiutaPlatformContext.INSTANCE.
     * @return AiutaStorage instance for the given namespace
     * @throws IllegalArgumentException if name is empty or contains invalid characters
     */
    public fun create(
        name: String,
        platformContext: AiutaPlatformContext,
    ): AiutaStorage {
        require(name.isNotEmpty()) { "Storage namespace must not be empty" }
        require(namespaceRegex.matches(name)) {
            "Storage namespace must contain only alphanumeric characters, underscore, or hyphen"
        }
        return createPlatformStorage(name, platformContext)
    }
}

/**
 * Platform-specific factory function to create storage instance.
 *
 * This function is implemented differently on each platform to handle
 * platform-specific DataStore creation and file path management.
 */
internal expect fun createPlatformStorage(
    name: String,
    platformContext: AiutaPlatformContext,
): AiutaStorage
