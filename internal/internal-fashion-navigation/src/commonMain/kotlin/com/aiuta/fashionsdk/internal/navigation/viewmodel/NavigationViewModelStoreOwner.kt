package com.aiuta.fashionsdk.internal.navigation.viewmodel

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistryOwner

/**
 * ViewModelStoreOwner implementation for navigation screens.
 *
 * Each navigation screen gets its own ViewModelStoreOwner with a dedicated ViewModelStore.
 * This ensures ViewModels are properly scoped to individual screens and cleared when
 * the screen is removed from the backstack.
 *
 * @property viewModelStore The ViewModelStore for this screen
 * @property savedStateRegistryOwner The SavedStateRegistryOwner for SavedStateHandle support
 */
internal class NavigationViewModelStoreOwner(
    override val viewModelStore: ViewModelStore,
    private val savedStateRegistryOwner: SavedStateRegistryOwner,
) : ViewModelStoreOwner,
    SavedStateRegistryOwner by savedStateRegistryOwner,
    HasDefaultViewModelProviderFactory {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = SavedStateViewModelFactory()

    override val defaultViewModelCreationExtras: CreationExtras
        get() = MutableCreationExtras().apply {
            this[androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY] = this@NavigationViewModelStoreOwner
            this[androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY] = this@NavigationViewModelStoreOwner
        }

    init {
        // Enable SavedStateHandle support for ViewModels
        // This requires the lifecycle to be in INITIALIZED state
        if (lifecycle.currentState == Lifecycle.State.INITIALIZED) {
            enableSavedStateHandles()
        }
    }
}
