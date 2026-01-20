package com.aiuta.fashionsdk.internal.storage

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
 * @see AiutaStorageFactory for creating instances
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
}
