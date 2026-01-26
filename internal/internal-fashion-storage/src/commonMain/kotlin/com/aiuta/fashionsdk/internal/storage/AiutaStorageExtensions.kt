package com.aiuta.fashionsdk.internal.storage

import kotlinx.serialization.serializer

/**
 * Inline helper that automatically resolves serializer for type [T].
 */
public suspend inline fun <reified T> AiutaStorage.save(key: String, value: T) {
    save(key, value, serializer())
}

/**
 * Inline helper that automatically resolves serializer for type [T].
 */
public suspend inline fun <reified T> AiutaStorage.get(key: String): T? = get(key, serializer())

/** Convenience extension for storing plain strings. */
public suspend fun AiutaStorage.saveString(key: String, value: String) {
    save(key, value)
}

/** Convenience extension for reading plain strings. */
public suspend fun AiutaStorage.getString(key: String): String? = get(key)

/** Convenience extension for storing ints. */
public suspend fun AiutaStorage.saveInt(key: String, value: Int) {
    save(key, value)
}

/** Convenience extension for reading ints. */
public suspend fun AiutaStorage.getInt(key: String): Int? = get(key)

/** Convenience extension for storing booleans. */
public suspend fun AiutaStorage.saveBoolean(key: String, value: Boolean) {
    save(key, value)
}

/** Convenience extension for reading booleans. */
public suspend fun AiutaStorage.getBoolean(key: String): Boolean? = get(key)

/** Convenience extension for storing longs. */
public suspend fun AiutaStorage.saveLong(key: String, value: Long) {
    save(key, value)
}

/** Convenience extension for reading longs. */
public suspend fun AiutaStorage.getLong(key: String): Long? = get(key)

/** Convenience extension for storing floats. */
public suspend fun AiutaStorage.saveFloat(key: String, value: Float) {
    save(key, value)
}

/** Convenience extension for reading floats. */
public suspend fun AiutaStorage.getFloat(key: String): Float? = get(key)

/** Convenience extension for storing doubles. */
public suspend fun AiutaStorage.saveDouble(key: String, value: Double) {
    save(key, value)
}

/** Convenience extension for reading doubles. */
public suspend fun AiutaStorage.getDouble(key: String): Double? = get(key)
