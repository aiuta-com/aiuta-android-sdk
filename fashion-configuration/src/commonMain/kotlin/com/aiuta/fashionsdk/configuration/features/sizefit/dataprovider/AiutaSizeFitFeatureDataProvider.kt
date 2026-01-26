package com.aiuta.fashionsdk.configuration.features.sizefit.dataprovider

/**
 * Interface for size fit feature data providers.
 *
 * This interface serves as a base for all data provider implementations
 * that handle size recommendation completion events.
 */
public interface AiutaSizeFitFeatureDataProvider {

    /**
     * Called when a size recommendation has been completed.
     *
     * @param recommendedSizeName The name of the recommended size
     */
    public suspend fun recommendationCompleted(recommendedSizeName: String)
}
