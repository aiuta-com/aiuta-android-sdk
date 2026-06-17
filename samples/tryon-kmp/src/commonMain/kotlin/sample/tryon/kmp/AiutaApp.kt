package sample.tryon.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.core.context.LocalAiutaPlatformContext
import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import com.aiuta.fashionsdk.sizefit.compose.ui.AiutaSizeFitFlow
import com.aiuta.fashionsdk.tryon.compose.ui.AiutaTryOnFlow
import com.aiuta.fashionsdk.tryon.compose.ui.HistoryFlow
import com.aiuta.fashionsdk.tryon.core.tryon
import sample.tryon.kmp.navigation.NavigationState
import sample.tryon.kmp.ui.FlowSelectorScreen
import sample.tryon.kmp.utils.buildMultiMockProductConfiguration
import sample.tryon.kmp.utils.buildSingleMockProductConfiguration

@Composable
fun AiutaApp() {
    val aiutaPlatformContext = LocalAiutaPlatformContext.current
    val viewModel: AiutaViewModel = viewModel { AiutaViewModel() }

    val navigationState = viewModel.navigationState.collectAsStateWithLifecycle()
    val activeProductItem = viewModel.activeProductItems.collectAsStateWithLifecycle()
    val aiutaConfiguration = remember {
        viewModel.buildAiutaConfiguration(aiutaPlatformContext)
    }

    when (navigationState.value) {
        NavigationState.Selector -> {
            FlowSelectorScreen(
                onTryOnClick = {
                    viewModel.navigateToFlow(NavigationState.TryOn)
                },
                onMultiTryOnClick = {
                    viewModel.navigateToFlow(NavigationState.MultiTryOn)
                },
                onShoesTryOnClick = {
                    viewModel.navigateToFlow(NavigationState.ShoesTryOn)
                },
                onHistoryClick = {
                    viewModel.navigateToFlow(NavigationState.History)
                },
                onSizeFitClick = {
                    viewModel.navigateToFlow(NavigationState.SizeFit)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }

        NavigationState.TryOn -> {
            LaunchedEffect(Unit) {
                viewModel.loadActiveProduct(aiutaConfiguration.aiuta.tryon)
            }

            if (activeProductItem.value.isNotEmpty()) {
                val mockProductConfiguration = buildSingleMockProductConfiguration(
                    generationItem = activeProductItem.value.first(),
                )

                AiutaTryOnFlow(
                    modifier = Modifier.fillMaxSize(),
                    aiutaConfiguration = aiutaConfiguration,
                    productConfiguration = mockProductConfiguration,
                    mode = AiutaMode.GENERAL,
                )
            }
        }

        NavigationState.MultiTryOn -> {
            LaunchedEffect(Unit) {
                viewModel.loadActiveProduct(aiutaConfiguration.aiuta.tryon)
            }

            if (activeProductItem.value.isNotEmpty()) {
                val multiProductConfiguration = buildMultiMockProductConfiguration(
                    generationItems = activeProductItem.value,
                )

                AiutaTryOnFlow(
                    modifier = Modifier.fillMaxSize(),
                    aiutaConfiguration = aiutaConfiguration,
                    productConfiguration = multiProductConfiguration,
                    mode = AiutaMode.GENERAL,
                )
            }
        }

        NavigationState.ShoesTryOn -> {
            LaunchedEffect(Unit) {
                viewModel.loadActiveProduct(aiutaConfiguration.aiuta.tryon)
            }

            if (activeProductItem.value.isNotEmpty()) {
                val mockProductConfiguration = buildSingleMockProductConfiguration(
                    generationItem = activeProductItem.value.first(),
                )

                AiutaTryOnFlow(
                    modifier = Modifier.fillMaxSize(),
                    aiutaConfiguration = aiutaConfiguration,
                    productConfiguration = mockProductConfiguration,
                    mode = AiutaMode.SHOES,
                )
            }
        }

        NavigationState.History -> {
            HistoryFlow(
                modifier = Modifier.fillMaxSize(),
                aiutaConfiguration = aiutaConfiguration,
            )
        }

        NavigationState.SizeFit -> {
            AiutaSizeFitFlow(
                modifier = Modifier.fillMaxSize(),
                aiutaConfiguration = aiutaConfiguration,
                productCode = BuildKonfig.SIZEFIT_PRODUCT_CODE,
            )
        }
    }
}
