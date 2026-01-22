package com.aiuta.fashionsdk.internal.storage.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aiuta.fashionsdk.internal.storage.AiutaStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.json.Json

/**
 * Internal implementation of [AiutaStorage] using DataStore Preferences.
 *
 * This class provides thread-safe, atomic operations for storing and retrieving
 * key-value data. Primitive types are stored natively in DataStore for optimal
 * performance, while complex objects are serialized to JSON strings.
 *
 * @property dataStore The underlying DataStore instance for this namespace
 */
internal class DataStoreStorage(
    private val dataStore: DataStore<Preferences>,
) : AiutaStorage {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun <T> save(key: String, value: T, serializer: KSerializer<T>) {
        validateKey(key)
        if (value == null) {
            remove(key)
            return
        }
        when (serializer.descriptor.kind) {
            PrimitiveKind.STRING -> savePreference(stringPreferencesKey(key), value as String)
            PrimitiveKind.INT -> savePreference(intPreferencesKey(key), value as Int)
            PrimitiveKind.BOOLEAN -> savePreference(booleanPreferencesKey(key), value as Boolean)
            PrimitiveKind.LONG -> savePreference(longPreferencesKey(key), value as Long)
            PrimitiveKind.FLOAT -> savePreference(floatPreferencesKey(key), value as Float)
            PrimitiveKind.DOUBLE -> savePreference(doublePreferencesKey(key), value as Double)
            else -> savePreference(stringPreferencesKey(key), json.encodeToString(serializer, value))
        }
    }

    override suspend fun <T> get(key: String, serializer: KSerializer<T>): T? {
        validateKey(key)
        @Suppress("UNCHECKED_CAST")
        return when (serializer.descriptor.kind) {
            PrimitiveKind.STRING -> readPreference(stringPreferencesKey(key)) as T?
            PrimitiveKind.INT -> readPreference(intPreferencesKey(key)) as T?
            PrimitiveKind.BOOLEAN -> readPreference(booleanPreferencesKey(key)) as T?
            PrimitiveKind.LONG -> readPreference(longPreferencesKey(key)) as T?
            PrimitiveKind.FLOAT -> readPreference(floatPreferencesKey(key)) as T?
            PrimitiveKind.DOUBLE -> readPreference(doublePreferencesKey(key)) as T?
            else -> {
                val jsonString = readPreference(stringPreferencesKey(key)) ?: return null
                json.decodeFromString(serializer, jsonString)
            }
        }
    }

    override suspend fun remove(key: String) {
        validateKey(key)
        dataStore.edit { preferences ->
            // Remove all possible type variants of this key
            preferences.remove(stringPreferencesKey(key))
            preferences.remove(intPreferencesKey(key))
            preferences.remove(booleanPreferencesKey(key))
            preferences.remove(longPreferencesKey(key))
            preferences.remove(floatPreferencesKey(key))
            preferences.remove(doublePreferencesKey(key))
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private fun validateKey(key: String) {
        require(key.isNotEmpty()) { "Storage key must not be empty" }
    }

    private suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    private suspend fun <T> readPreference(key: Preferences.Key<T>): T? = dataStore.data.map { preferences ->
        preferences[key]
    }.first()
}
