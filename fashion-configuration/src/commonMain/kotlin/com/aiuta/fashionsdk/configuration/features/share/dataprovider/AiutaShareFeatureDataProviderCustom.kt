package com.aiuta.fashionsdk.configuration.features.share.dataprovider

/**
 * Interface for providing custom share text in the SDK.
 *
 * This interface allows customization of the text that appears when sharing
 * generated images or try-on results, enabling integration with external
 * text generation or localization systems.
 *
 * @property getShareText Generates share text for the given product IDs
 */
public interface AiutaShareFeatureDataProviderCustom {
    /**
     * Generates share text for the given product IDs.
     *
     * @param productIds List of product identifiers to generate share text for
     * @return The generated share text
     */
    public suspend fun getShareText(productIds: List<String>): String
}
