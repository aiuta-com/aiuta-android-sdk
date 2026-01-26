package com.aiuta.fashionsdk.internal.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.aiuta.fashionsdk.internal.storage.internal.DataStoreStorage
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import okio.Path.Companion.toPath

/**
 * Unit tests for [AiutaStorage] implementation.
 *
 * Tests cover all storage operations including primitives, serialization,
 * error handling, and edge cases.
 */
class AiutaStorageTest {

    @Serializable
    data class TestUser(
        val name: String,
        val age: Int,
        val active: Boolean = true,
    )

    @Serializable
    data class TestSettings(
        val theme: String,
        val fontSize: Int,
        val notifications: Boolean,
    )

    private fun createTestStorage(): AiutaStorage {
        val testPath = "test_${Random.nextInt()}.preferences_pb"
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
            produceFile = { testPath.toPath() },
        )
        return DataStoreStorage(dataStore)
    }

    // ==================== String Tests ====================

    @Test
    fun saveAndGetStringValue() = runTest {
        val storage = createTestStorage()
        storage.saveString("name", "Alice")
        assertEquals("Alice", storage.getString("name"))
    }

    @Test
    fun getNonExistentStringReturnsNull() = runTest {
        val storage = createTestStorage()
        assertNull(storage.getString("nonexistent"))
    }

    @Test
    fun overwriteStringValue() = runTest {
        val storage = createTestStorage()
        storage.saveString("name", "Alice")
        storage.saveString("name", "Bob")
        assertEquals("Bob", storage.getString("name"))
    }

    @Test
    fun saveEmptyStringValue() = runTest {
        val storage = createTestStorage()
        storage.saveString("empty", "")
        assertEquals("", storage.getString("empty"))
    }

    // ==================== Int Tests ====================

    @Test
    fun saveAndGetIntValue() = runTest {
        val storage = createTestStorage()
        storage.saveInt("age", 25)
        assertEquals(25, storage.getInt("age"))
    }

    @Test
    fun getNonExistentIntReturnsNull() = runTest {
        val storage = createTestStorage()
        assertNull(storage.getInt("nonexistent"))
    }

    @Test
    fun saveNegativeIntValue() = runTest {
        val storage = createTestStorage()
        storage.saveInt("negative", -100)
        assertEquals(-100, storage.getInt("negative"))
    }

    @Test
    fun saveIntMaxAndMinValues() = runTest {
        val storage = createTestStorage()
        storage.saveInt("max", Int.MAX_VALUE)
        storage.saveInt("min", Int.MIN_VALUE)
        assertEquals(Int.MAX_VALUE, storage.getInt("max"))
        assertEquals(Int.MIN_VALUE, storage.getInt("min"))
    }

    // ==================== Boolean Tests ====================

    @Test
    fun saveAndGetBooleanTrue() = runTest {
        val storage = createTestStorage()
        storage.saveBoolean("flag", true)
        assertEquals(true, storage.getBoolean("flag"))
    }

    @Test
    fun saveAndGetBooleanFalse() = runTest {
        val storage = createTestStorage()
        storage.saveBoolean("flag", false)
        assertEquals(false, storage.getBoolean("flag"))
    }

    @Test
    fun getNonExistentBooleanReturnsNull() = runTest {
        val storage = createTestStorage()
        assertNull(storage.getBoolean("nonexistent"))
    }

    // ==================== Long Tests ====================

    @Test
    fun saveAndGetLongValue() = runTest {
        val storage = createTestStorage()
        storage.saveLong("timestamp", 1234567890123L)
        assertEquals(1234567890123L, storage.getLong("timestamp"))
    }

    @Test
    fun saveLongMaxAndMinValues() = runTest {
        val storage = createTestStorage()
        storage.saveLong("max", Long.MAX_VALUE)
        storage.saveLong("min", Long.MIN_VALUE)
        assertEquals(Long.MAX_VALUE, storage.getLong("max"))
        assertEquals(Long.MIN_VALUE, storage.getLong("min"))
    }

    // ==================== Float Tests ====================

    @Test
    fun saveAndGetFloatValue() = runTest {
        val storage = createTestStorage()
        storage.saveFloat("price", 19.99f)
        assertEquals(19.99f, storage.getFloat("price"))
    }

    @Test
    fun saveFloatSpecialValues() = runTest {
        val storage = createTestStorage()
        storage.saveFloat("zero", 0.0f)
        storage.saveFloat("negative", -123.456f)
        assertEquals(0.0f, storage.getFloat("zero"))
        assertEquals(-123.456f, storage.getFloat("negative"))
    }

    // ==================== Double Tests ====================

    @Test
    fun saveAndGetDoubleValue() = runTest {
        val storage = createTestStorage()
        storage.saveDouble("precise", 3.141592653589793)
        assertEquals(3.141592653589793, storage.getDouble("precise"))
    }

    // ==================== Serialization Tests ====================

    @Test
    fun saveAndGetSerializableObject() = runTest {
        val storage = createTestStorage()
        val user = TestUser("Alice", 30, true)
        storage.save("user", user)
        assertEquals(user, storage.get<TestUser>("user"))
    }

    @Test
    fun getNonExistentObjectReturnsNull() = runTest {
        val storage = createTestStorage()
        assertNull(storage.get<TestUser>("nonexistent"))
    }

    @Test
    fun overwriteSerializableObject() = runTest {
        val storage = createTestStorage()
        val user1 = TestUser("Alice", 30)
        val user2 = TestUser("Bob", 25, false)
        storage.save("user", user1)
        storage.save("user", user2)
        assertEquals(user2, storage.get<TestUser>("user"))
    }

    @Test
    fun saveMultipleDifferentObjects() = runTest {
        val storage = createTestStorage()
        val user = TestUser("Alice", 30)
        val settings = TestSettings("dark", 14, true)

        storage.save("user", user)
        storage.save("settings", settings)

        assertEquals(user, storage.get<TestUser>("user"))
        assertEquals(settings, storage.get<TestSettings>("settings"))
    }

    // ==================== Remove Tests ====================

    @Test
    fun removeExistingKey() = runTest {
        val storage = createTestStorage()
        storage.saveString("name", "Alice")
        storage.remove("name")
        assertNull(storage.getString("name"))
    }

    @Test
    fun removeNonExistentKeyIsNoOp() = runTest {
        val storage = createTestStorage()
        storage.remove("nonexistent")
        // Should not throw
    }

    @Test
    fun removeClearsAllTypeVariants() = runTest {
        val storage = createTestStorage()
        storage.saveString("key", "value")
        storage.saveInt("key", 42)
        storage.remove("key")
        assertNull(storage.getString("key"))
        assertNull(storage.getInt("key"))
    }

    // ==================== Clear Tests ====================

    @Test
    fun clearRemovesAllData() = runTest {
        val storage = createTestStorage()
        storage.saveString("name", "Alice")
        storage.saveInt("age", 30)
        storage.saveBoolean("active", true)

        storage.clear()

        assertNull(storage.getString("name"))
        assertNull(storage.getInt("age"))
        assertNull(storage.getBoolean("active"))
    }

    @Test
    fun clearOnEmptyStorageIsNoOp() = runTest {
        val storage = createTestStorage()
        storage.clear()
        // Should not throw
    }

    // ==================== Key Validation Tests ====================

    @Test
    fun emptyKeyThrowsIllegalArgumentException() = runTest {
        val storage = createTestStorage()
        assertFailsWith<IllegalArgumentException> {
            storage.saveString("", "value")
        }
    }

    @Test
    fun emptyKeyOnGetThrowsIllegalArgumentException() = runTest {
        val storage = createTestStorage()
        assertFailsWith<IllegalArgumentException> {
            storage.getString("")
        }
    }

    @Test
    fun emptyKeyOnRemoveThrowsIllegalArgumentException() = runTest {
        val storage = createTestStorage()
        assertFailsWith<IllegalArgumentException> {
            storage.remove("")
        }
    }
}
