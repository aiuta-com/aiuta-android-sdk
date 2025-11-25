package com.aiuta.fashionsdk.configuration.ui.actions

import com.aiuta.fashionsdk.configuration.ui.AiutaUserInterfaceConfiguration

/**
 * Interface for handling user interface actions within the Aiuta SDK.
 *
 * This interface defines callbacks for various user interactions that occur
 * within the SDK's UI components. Implement this interface to handle user
 * actions such as navigation, closing screens, and other UI interactions.
 *
 * @see AiutaUserInterfaceConfiguration
 */
public interface AiutaUserInterfaceActions {
    /**
     * Called when the user clicks a close button or performs a close action.
     *
     * This method is invoked when the user wants to close or exit from
     * SDK screens. Implement this to handle navigation back to your app's
     * previous screen or to close the current activity.
     */
    public fun closeClick()
}
