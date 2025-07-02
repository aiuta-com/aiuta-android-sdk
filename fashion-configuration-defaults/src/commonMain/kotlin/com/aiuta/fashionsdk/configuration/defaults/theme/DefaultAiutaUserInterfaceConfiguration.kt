package com.aiuta.fashionsdk.configuration.defaults.theme

import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.defaults.theme.button.defaultButton
import com.aiuta.fashionsdk.configuration.defaults.theme.error.defaultErrorSnackbar
import com.aiuta.fashionsdk.configuration.defaults.theme.image.defaultImage
import com.aiuta.fashionsdk.configuration.defaults.theme.indicator.defaultActivityIndicator
import com.aiuta.fashionsdk.configuration.defaults.theme.label.defaultLabel
import com.aiuta.fashionsdk.configuration.defaults.theme.pagebar.defaultPageBar
import com.aiuta.fashionsdk.configuration.defaults.theme.powerby.defaultPoweredBar
import com.aiuta.fashionsdk.configuration.defaults.theme.productbar.defaultProductBar
import com.aiuta.fashionsdk.configuration.defaults.theme.selection.defaultSelectionSnackbar
import com.aiuta.fashionsdk.configuration.defaults.theme.sheet.defaultBottomSheet
import com.aiuta.fashionsdk.configuration.ui.actions.AiutaUserInterfaceActions
import com.aiuta.fashionsdk.configuration.ui.theme.color.AiutaColorTheme
import com.aiuta.fashionsdk.configuration.ui.theme.theme
import com.aiuta.fashionsdk.configuration.ui.userInterface

/**
 * Configures the default user interface settings for the Aiuta SDK.
 *
 * This function sets up the user interface with default actions and theme configurations,
 * including buttons, error snackbars, images, labels, page bars, product bars, powered bars,
 * selection snackbars, and bottom sheets.
 *
 * @param actions The user interface actions to be configured.
 * @return The updated [AiutaConfiguration.Builder] instance.
 */
public fun AiutaConfiguration.Builder.defaultAiutaUserInterfaceConfiguration(
    actions: AiutaUserInterfaceActions,
): AiutaConfiguration.Builder = userInterface {
    this.actions = actions
    theme {
        color = AiutaColorTheme.Default()

        defaultActivityIndicator()
        defaultButton()
        defaultErrorSnackbar()
        defaultImage()
        defaultLabel()
        defaultPageBar()
        defaultProductBar()
        defaultPoweredBar()
        defaultSelectionSnackbar()
        defaultBottomSheet()
    }
}
