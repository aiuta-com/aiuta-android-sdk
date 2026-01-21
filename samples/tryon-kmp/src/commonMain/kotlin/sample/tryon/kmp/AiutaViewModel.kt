package sample.tryon.kmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.aiuta
import com.aiuta.fashionsdk.authentication.ApiKeyAuthenticationStrategy
import com.aiuta.fashionsdk.configuration.aiutaConfiguration
import com.aiuta.fashionsdk.configuration.defaults.features.consent.defaultConsent
import com.aiuta.fashionsdk.configuration.defaults.features.onboarding.defaultOnboarding
import com.aiuta.fashionsdk.configuration.defaults.features.selector.defaultImagePicker
import com.aiuta.fashionsdk.configuration.defaults.features.share.defaultShare
import com.aiuta.fashionsdk.configuration.defaults.features.sizefit.defaultSizeFit
import com.aiuta.fashionsdk.configuration.defaults.features.tryon.defaultTryOn
import com.aiuta.fashionsdk.configuration.defaults.theme.defaultAiutaUserInterfaceConfiguration
import com.aiuta.fashionsdk.configuration.features.features
import com.aiuta.fashionsdk.configuration.features.tryon.cart.handler.AiutaTryOnCartFeatureHandler
import com.aiuta.fashionsdk.configuration.ui.actions.AiutaUserInterfaceActions
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.logger.DebugAiutaLogger
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFit
import com.aiuta.fashionsdk.sizefit.core.aiutaSizeFit
import com.aiuta.fashionsdk.tryon.core.AiutaTryOn
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import sample.tryon.kmp.navigation.NavigationState

class AiutaViewModel : ViewModel() {
    val activeProductItems: MutableStateFlow<List<ProductGenerationItem>> =
        MutableStateFlow(emptyList())
    val navigationState: MutableStateFlow<NavigationState> =
        MutableStateFlow(NavigationState.Selector)

    // Aiuta
    fun buildAiutaConfiguration(context: AiutaPlatformContext) = aiutaConfiguration {
        aiuta = buildAiuta(context)
        defaultAiutaUserInterfaceConfiguration(
            actions = object : AiutaUserInterfaceActions {
                override fun closeClick() {
                    navigateBack()
                }
            },
        )
        features {
            defaultOnboarding()
            defaultConsent(termsOfServiceUrl = "https://you-domain.com/you-tos")
            defaultImagePicker()
            defaultTryOn(
                cartHandler = object : AiutaTryOnCartFeatureHandler {
                    override fun addToCart(productId: String) {
                        println("CLICK ADD TO CART")
                    }
                },
            )
            defaultShare()
            defaultSizeFit(privacyPolicyUrl = "https://you-domain.com/you-pp")
        }
    }

    fun buildAiutaSizeFit(aiuta: Aiuta): AiutaSizeFit = aiutaSizeFit {
        this.aiuta = aiuta
        this.apiKey = BuildKonfig.SIZEFIT_API_KEY
        this.partitionHeader = BuildKonfig.SIZEFIT_PARTITION
    }

    // Navigation
    fun navigateToFlow(flow: NavigationState) {
        navigationState.value = flow
    }

    fun navigateBack() {
        navigationState.value = NavigationState.Selector
    }

    // Preload
    fun loadActiveProduct(aiutaTryOn: AiutaTryOn) {
        viewModelScope.launch {
            // Let's get catalogs
            val catalogs = aiutaTryOn.getProductCatalogs().result

            // Take first catalog and get first page of product items
            val productItems = catalogs.firstOrNull()?.let {
                aiutaTryOn.getProductItems(
                    catalogName = it.catalogName,
                )
            }

            // And finally take first product item
            activeProductItems.value = productItems?.result ?: emptyList()
        }
    }

    private fun buildAiuta(context: AiutaPlatformContext): Aiuta = aiuta {
        authenticationStrategy = ApiKeyAuthenticationStrategy(BuildKonfig.AIUTA_API_KEY)
        platformContext = context
        logger = DebugAiutaLogger()
    }
}
