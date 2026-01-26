package com.aiuta.fashionsdk.internal.navigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

/**
 * Internal ViewModel that manages ViewModelStores for navigation entries.
 *
 * Each navigation screen gets its own ViewModelStore instance, identified by screen ID.
 * When a screen is popped from the backstack, its ViewModelStore is cleared to prevent memory leaks.
 *
 * This is inspired by androidx.lifecycle.viewmodel.navigation3.ViewModelStoreNavEntryDecorator
 * but adapted for Aiuta's custom navigation system.
 */
internal class NavigationViewModelStoreManager : ViewModel() {
    private val viewModelStores = mutableMapOf<String, ViewModelStore>()

    /**
     * Gets or creates a ViewModelStore for the given screen ID.
     *
     * @param screenId Unique identifier for the navigation screen
     * @return ViewModelStore scoped to this screen
     */
    fun getViewModelStoreForScreen(screenId: String): ViewModelStore = viewModelStores.getOrPut(screenId) { ViewModelStore() }

    /**
     * Clears and removes the ViewModelStore for the given screen ID.
     * Should be called when the screen is popped from backstack.
     *
     * @param screenId Unique identifier for the navigation screen
     */
    fun clearViewModelStoreForScreen(screenId: String) {
        viewModelStores.remove(screenId)?.clear()
    }

    /**
     * Clears ViewModelStores for multiple screens at once.
     * Useful for popUpTo operations.
     *
     * @param screenIds Set of screen IDs to clear
     */
    fun clearViewModelStoresForScreens(screenIds: Set<String>) {
        screenIds.forEach { screenId ->
            viewModelStores.remove(screenId)?.clear()
        }
    }

    override fun onCleared() {
        // Clear all ViewModelStores when this manager is cleared
        viewModelStores.values.forEach { it.clear() }
        viewModelStores.clear()
    }
}

/**
 * Extension to get NavigationViewModelStoreManager from a ViewModelStore.
 */
internal fun ViewModelStore.getNavigationViewModelStoreManager(): NavigationViewModelStoreManager {
    val provider = ViewModelProvider.create(
        store = this,
        factory = viewModelFactory {
            initializer { NavigationViewModelStoreManager() }
        },
    )
    return provider[NavigationViewModelStoreManager::class]
}
