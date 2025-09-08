package sample.tryon.kmp.web

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.core.context.LocalAiutaPlatformContext
import com.aiuta.fashionsdk.tryon.core.tryon
import sample.tryon.kmp.web.blocks.EmptyScreen
import sample.tryon.kmp.web.blocks.LoadingScreen
import sample.tryon.kmp.web.blocks.SuccessScreen

@Composable
fun AiutaApp() {
    val aiutaPlatformContext = LocalAiutaPlatformContext.current
    val viewModel: AiutaViewModel = viewModel { AiutaViewModel() }

    val activeProductItem = viewModel.activeProductItems.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadActiveProduct(aiutaPlatformContext)
    }

    Crossfade(
        targetState = activeProductItem.value,
    ) { items ->
        when {
            items == null -> LoadingScreen()
            items.isEmpty() -> EmptyScreen()
            else -> SuccessScreen(
                products = items,
                viewModel = viewModel,
            )
        }
    }
}
