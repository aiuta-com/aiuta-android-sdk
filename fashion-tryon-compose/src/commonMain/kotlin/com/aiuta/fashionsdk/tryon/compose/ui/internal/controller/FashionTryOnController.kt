package com.aiuta.fashionsdk.tryon.compose.ui.internal.controller

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import coil3.compose.LocalPlatformContext
import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneFeature
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.picker.history.AiutaImagePickerUploadsHistoryFeature
import com.aiuta.fashionsdk.configuration.features.tryon.AiutaTryOnFeature
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.configuration.ui.actions.AiutaUserInterfaceActions
import com.aiuta.fashionsdk.internal.analytics.InternalAiutaAnalytic
import com.aiuta.fashionsdk.internal.analytics.internalAiutaAnalytic
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.consent.ConsentInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.images.GeneratedImageInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.operations.GeneratedOperationInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.onboarding.OnboardingInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.session.SessionGenerationInteractor
import com.aiuta.fashionsdk.tryon.compose.domain.internal.selector.SelectedHolder
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.LastSavedImages
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.operations.GeneratedOperationUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationOperation
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.sku.ProductGenerationUIStatus
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.bottomsheet.BottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.bottomsheet.rememberBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.models.SelectorMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.ZoomImageController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.zoom.controller.rememberZoomImageController
import com.aiuta.fashionsdk.tryon.core.AiutaTryOn
import com.aiuta.fashionsdk.tryon.core.tryon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

@Composable
internal fun BoxWithConstraintsScope.rememberFashionTryOnController(
    aiutaConfiguration: AiutaConfiguration,
    productItem: ProductItem,
    startScreen: NavigationScreen,
): FashionTryOnController {
    val uiScope = rememberCoroutineScope()
    val coilContext = LocalPlatformContext.current
    val internalAiutaAnalytic = remember { aiutaConfiguration.aiuta.internalAiutaAnalytic }

    val onboardingFeature = aiutaConfiguration.features.provideFeature<AiutaOnboardingFeature>()
    val consentStandaloneFeature = aiutaConfiguration.features.provideFeature<AiutaConsentStandaloneFeature>()
    val uploadsHistoryFeature = aiutaConfiguration.features.provideFeature<AiutaImagePickerUploadsHistoryFeature>()
    val generationsHistoryFeature = aiutaConfiguration.features.provideFeature<AiutaTryOnGenerationsHistoryFeature>()
    val tryOnFeature = aiutaConfiguration.features.strictProvideFeature<AiutaTryOnFeature>()

    val activeProductItem = remember { mutableStateOf(productItem) }

    val zoomImageController = rememberZoomImageController(constraints = constraints)

    val defaultBottomSheetNavigator = rememberBottomSheetNavigator()

    return remember {
        FashionTryOnController(
            startScreen = startScreen,
            bottomSheetNavigator = defaultBottomSheetNavigator,
            zoomImageController = zoomImageController,
            activeProductItem = activeProductItem,
            aiuta = aiutaConfiguration.aiuta,
            aiutaTryOn = aiutaConfiguration.aiuta.tryon,
            aiutaUserInterfaceActions = aiutaConfiguration.userInterface.actions,
            generatedImageInteractor = GeneratedImageInteractor.getInstance(
                aiuta = aiutaConfiguration.aiuta,
                generationsHistoryFeature = generationsHistoryFeature,
            ),
            generatedOperationInteractor = GeneratedOperationInteractor.getInstance(
                aiuta = aiutaConfiguration.aiuta,
                uploadsHistoryFeature = uploadsHistoryFeature,
            ),
            onboardingInteractor = OnboardingInteractor.getInstance(
                aiuta = aiutaConfiguration.aiuta,
                scope = uiScope,
                onboardingFeature = onboardingFeature,
            ),
            consentInteractor = ConsentInteractor.getInstance(
                aiuta = aiutaConfiguration.aiuta,
                scope = uiScope,
                consentStandaloneFeature = consentStandaloneFeature,
            ),
            sessionGenerationInteractor = SessionGenerationInteractor.getInstance(
                coilContext = coilContext,
                generationsHistoryFeature = generationsHistoryFeature,
            ),
            analytic = internalAiutaAnalytic,
        )
    }.also {
        it.generationNavigationListener()
        it.generationCancellationListener(
            tryOnFeature = tryOnFeature,
        )
        it.historyAvailabilityListener(
            isGenerationsHistoryFeatureAvailable = aiutaConfiguration.features.isFeatureInitialize<AiutaTryOnGenerationsHistoryFeature>(),
        )
    }
}

@Immutable
internal class FashionTryOnController(
    // General navigation
    public val startScreen: NavigationScreen,
    public val zoomImageController: ZoomImageController,
    // Bottom sheet navigation
    public val bottomSheetNavigator: BottomSheetNavigator,
    // Data
    public val activeProductItem: MutableState<ProductItem>,
    // Domain
    public val aiuta: Aiuta,
    public val aiutaTryOn: AiutaTryOn,
    public val aiutaUserInterfaceActions: AiutaUserInterfaceActions,
    internal val generatedImageInteractor: GeneratedImageInteractor,
    internal val generatedOperationInteractor: GeneratedOperationInteractor,
    internal val onboardingInteractor: OnboardingInteractor,
    internal val consentInteractor: ConsentInteractor,
    internal val sessionGenerationInteractor: SessionGenerationInteractor,
    // Analytic
    internal val analytic: InternalAiutaAnalytic,
) {
    // Generation state
    internal val generationStatus: MutableState<ProductGenerationUIStatus> =
        mutableStateOf(ProductGenerationUIStatus.IDLE)
    internal val generationOperations: SnapshotStateList<ProductGenerationOperation> =
        mutableStateListOf()

    // General navigation
    internal val backStack: ArrayDeque<NavigationScreen> = ArrayDeque()
    public val currentScreen: MutableState<NavigationScreen> = mutableStateOf(startScreen)

    // Error state
    public val fashionTryOnErrorState: MutableState<ToastErrorState?> = mutableStateOf(null)

    // Edit changePhotoButtonStyle
    internal val selectorState: MutableState<SelectorMode> = mutableStateOf(SelectorMode.DISABLED)
    val selectorHolder: SelectedHolder<GeneratedImageUIModel> = SelectedHolder()

    // Data
    public val lastSavedImages: MutableState<LastSavedImages> =
        mutableStateOf(LastSavedImages.Empty)
    public val lastSavedOperation: MutableState<GeneratedOperationUIModel?> = mutableStateOf(null)

    // Domain
    public val isGenerationActive: MutableState<Boolean> = mutableStateOf(false)

    // Utils
    private var internalGenerationScope: CoroutineScope? = null
    internal val generationScope: CoroutineScope
        get() {
            return cancelGenerationScope()
        }

    internal val generalScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Try on auto enabling
    val isAutoTryOnEnabled: MutableState<Boolean> = mutableStateOf(false)

    /**
     * Cancel current generation scope and set new for next generation
     *
     * @return new coroutine scope
     */
    internal fun cancelGenerationScope(): CoroutineScope {
        internalGenerationScope?.cancel()
        return CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).also {
            internalGenerationScope = it
        }
    }

    internal fun clean() {
        // Cancel generation scope
        internalGenerationScope?.cancel()
    }
}
