package sample.tryon.kmp.web

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.aiuta
import com.aiuta.fashionsdk.authentication.ApiKeyAuthenticationStrategy
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.logger.DebugAiutaLogger
import com.aiuta.fashionsdk.tryon.core.AiutaTryOn
import com.aiuta.fashionsdk.tryon.core.data.datasource.image.models.AiutaFileType
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationStatus
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationUrlContainer
import com.aiuta.fashionsdk.tryon.core.tryon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AiutaViewModel : ViewModel() {

    private val _activeProductItems: MutableStateFlow<List<ProductGenerationItem>?> = MutableStateFlow(null)
    val activeProductItems: StateFlow<List<ProductGenerationItem>?> = _activeProductItems.asStateFlow()

    private val _generationState: MutableStateFlow<ProductGenerationStatus?> = MutableStateFlow(null)
    val generationState: StateFlow<ProductGenerationStatus?> = _generationState.asStateFlow()

    var aiutaTryOn: AiutaTryOn? = null

    fun loadActiveProduct(context: AiutaPlatformContext) {
        viewModelScope.launch {
            // Build Auta
            val aiuta = buildAiuta(context)
            aiutaTryOn = aiuta.tryon

            // Let's get catalogs
            val catalogs = aiutaTryOn?.getProductCatalogs()?.result

            // Take first catalog and get first page of product items
            val productItems = catalogs?.firstOrNull()?.let {
                aiutaTryOn?.getProductItems(
                    catalogName = it.catalogName,
                )
            }

            // And finally put list
            _activeProductItems.value = productItems?.result ?: emptyList()
        }
    }

    fun startTryOn(
        product: ProductGenerationItem,
    ) {
        aiutaTryOn
            ?.startProductGeneration(
                container = ProductGenerationUrlContainer(
                    fileId = TRYON_IMAGE_ID,
                    fileUrl = TRYON_IMAGE_URL,
                    fileType = AiutaFileType.USER,
                    productIds = listOf(product.productId),
                ),
            )
            ?.onEach { state ->
                _generationState.value = state
            }
            ?.launchIn(viewModelScope)
    }

    private fun buildAiuta(context: AiutaPlatformContext): Aiuta = aiuta {
        authenticationStrategy = ApiKeyAuthenticationStrategy(BuildKonfig.AIUTA_API_KEY)
        platformContext = context
        logger = DebugAiutaLogger()
    }

    companion object {

        const val TRYON_IMAGE_ID = "YOUR_IMAGE_ID"
        const val TRYON_IMAGE_URL = "YOUR_IMAGE_URL"
    }
}
