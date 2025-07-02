package com.aiuta.fashionsdk.configuration.ui.theme.indicator.settings

/**
 * Theme settings for activity indicator.
 */
public interface AiutaActivityIndicatorThemeSettings {
    /**
     * The delay in milliseconds before the activity indicator becomes visible.
     * This can be used to prevent the indicator from appearing for very short operations.
     */
    public val indicationDelay: Long

    /**
     * The duration in milliseconds for one complete spin animation of the activity indicator.
     */
    public val spinDuration: Long

    /**
     * Default implementation of [AiutaActivityIndicatorThemeSettings].
     */
    public class Default : AiutaActivityIndicatorThemeSettings {
        override val indicationDelay: Long = 1500L
        override val spinDuration: Long = 1000L
    }
}
