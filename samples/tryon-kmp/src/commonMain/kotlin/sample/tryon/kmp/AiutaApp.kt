package sample.tryon.kmp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.core.context.LocalAiutaPlatformContext
import com.aiuta.fashionsdk.tryon.compose.ui.AiutaTryOnFlow
import com.aiuta.fashionsdk.tryon.core.tryon
import sample.tryon.kmp.utils.buildMockProductItem

@Composable
fun AiutaApp() {
    val aiutaPlatformContext = LocalAiutaPlatformContext.current
    val viewModel: AiutaViewModel = viewModel { AiutaViewModel() }

    val activeProductItem = viewModel.activeProductItem.collectAsState()
    val aiutaConfiguration = remember {
        viewModel.buildAiutaConfiguration(aiutaPlatformContext)
    }

    LaunchedEffect(Unit) {
        viewModel.loadActiveProduct(aiutaConfiguration.aiuta.tryon)
    }

    activeProductItem.value?.let { generationItem ->
        val mockProductItem = buildMockProductItem(generationItem)

        AiutaTryOnFlow(
            modifier = Modifier.fillMaxSize(),
            aiutaConfiguration = aiutaConfiguration,
            productForGeneration = mockProductItem,
        )
    }
}
