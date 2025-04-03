package com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.images

import androidx.paging.PagingData
import com.aiuta.fashionsdk.tryon.compose.configuration.features.tryon.history.dataprovider.AiutaTryOnGenerationsHistoryFeatureDataProvider
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toImageUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toPublic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HostGeneratedImageInteractor(
    private val dataProvider: AiutaTryOnGenerationsHistoryFeatureDataProvider,
) : GeneratedImageInteractor {
    override suspend fun insertAll(
        generatedSkuId: String,
        images: List<GeneratedImageUIModel>,
    ) {
        dataProvider.addGeneratedImagesAction(
            generatedSkuId,
            images.map { image -> image.toPublic() },
        )
    }

    override fun generatedImagesFlow(): Flow<PagingData<GeneratedImageUIModel>> = dataProvider.generatedImages
        .map { images ->
            images.map { image -> image.toImageUiModel() }
        }
        .map { images -> PagingData.from(images) }

    override suspend fun remove(generatedImages: List<GeneratedImageUIModel>) {
        dataProvider.deleteGeneratedImagesAction(
            generatedImages.map { image -> image.toPublic() },
        )
    }

    override suspend fun removeAll() {
        // Ignore this, because host should solve current list of generations
    }

    override fun countFlow(): Flow<Int> = dataProvider.generatedImages.map { images -> images.size }

    companion object {
        fun getInstance(dataProvider: AiutaTryOnGenerationsHistoryFeatureDataProvider): HostGeneratedImageInteractor = HostGeneratedImageInteractor(dataProvider = dataProvider)
    }
}
