package com.aiuta.fashionsdk.internal.storage

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import kotlinx.serialization.KSerializer

/**
 * Storage interface for key-value persistence using DataStore Preferences.
 *
 * This interface provides type-safe operations for storing and retrieving data.
 * It supports both primitive types (via extension functions) and complex
 * serializable objects (via generic methods with kotlinx.serialization).
 *
 * All operations are suspending and thread-safe. Each storage instance is isolated
 * by namespace and does not share data with other instances.
 *
 * Usage example:
 * ```kotlin
 * // Create storage instance
 * val storage = AiutaStorageFactory.create("my_namespace", platformContext)
 *
 * // Save and retrieve primitives (via extension functions)
 * storage.saveString("name", "Alice")
 * val name = storage.getString("name") // "Alice"
 *
 * // Save and retrieve serializable objects
 * @Serializable
 * data class UserSettings(val theme: String, val fontSize: Int)
 *
 * storage.save("settings", UserSettings("dark", 14))
 * val settings = storage.get<UserSettings>("settings")
 * ```
 *
 * @see AiutaStorage.Factory for creating instances
 */
public interface AiutaStorage {

    /**
     * Saves a value for the given key using the provided serializer.
     *
     * @param key The storage key (must not be empty)
     * @param value The value to save
     * @param serializer The serializer for the value type
     * @throws IllegalArgumentException if key is empty
     */
    public suspend fun <T> save(key: String, value: T, serializer: KSerializer<T>)

    /**
     * Retrieves a value for the given key using the provided serializer.
     *
     * @param key The storage key (must not be empty)
     * @param serializer The serializer for the value type
     * @return The stored value, or null if not found
     * @throws IllegalArgumentException if key is empty
     */
    public suspend fun <T> get(key: String, serializer: KSerializer<T>): T?

    /**
     * Removes the value for the given key.
     *
     * If the key does not exist, this operation is a no-op.
     *
     * @param key The storage key to remove (must not be empty)
     * @throws IllegalArgumentException if key is empty
     */
    public suspend fun remove(key: String)

    /**
     * Clears all data in this storage instance.
     *
     * This operation is atomic and thread-safe.
     */
    public suspend fun clear()

    public class Factory {

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
