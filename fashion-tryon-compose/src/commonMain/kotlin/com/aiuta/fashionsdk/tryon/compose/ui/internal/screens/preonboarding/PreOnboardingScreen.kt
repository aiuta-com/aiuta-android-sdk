package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.preonboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.internal.analytic.model.AiutaAnalyticOnboardingEventType
import com.aiuta.fashionsdk.internal.analytic.model.AiutaAnalyticPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickClose
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendOnboardingEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateTo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.NavigationScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBarIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.tryon.compose.uikit.utils.clickableUnindicated

@Composable
internal fun PreOnboardingScreen(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val welcomeScreenFeature = strictProvideFeature<com.aiuta.fashionsdk.configuration.features.welcome.AiutaWelcomeScreenFeature>()

    sendPageEvent(pageId = AiutaAnalyticPageId.WELCOME)

    Box(
        modifier = modifier.background(theme.color.background),
        contentAlignment = Alignment.Center,
    ) {
        AiutaImage(
            modifier = Modifier.fillMaxSize(),
            image = welcomeScreenFeature.images.welcomeBackground,
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        AppBar(
            modifier =
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            actions = {
                AppBarIcon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    icon = theme.pageBar.icons.close24,
                    color = Color.White,
                    onClick = controller::clickClose,
                )
            },
        )

        PreOnboardingForeground()
    }
}

@Composable
private fun PreOnboardingForeground(modifier: Modifier = Modifier) {
    val controller = LocalController.current

    val welcomeScreenFeature = strictProvideFeature<com.aiuta.fashionsdk.configuration.features.welcome.AiutaWelcomeScreenFeature>()

    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AiutaIcon(
            modifier = Modifier.size(82.dp),
            icon = welcomeScreenFeature.icons.welcome82,
            contentDescription = null,
            tint = Color.Unspecified,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = welcomeScreenFeature.strings.welcomeTitle,
            style = welcomeScreenFeature.typography.welcomeTitle,
            color = Color.White,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = welcomeScreenFeature.strings.welcomeDescription,
            style = welcomeScreenFeature.typography.welcomeDescription,
            color = Color.White,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(36.dp))

        StartButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                controller.sendOnboardingEvent(
                    eventType = AiutaAnalyticOnboardingEventType.WELCOME_START_CLICKED,
                    pageId = AiutaAnalyticPageId.WELCOME,
                    productId = controller.activeProductItem.value.id,
                    supplementaryConsents = null,
                )
                controller.navigateTo(NavigationScreen.Onboarding)
            },
        )
    }
}

@Composable
private fun StartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current
    val welcomeScreenFeature = strictProvideFeature<com.aiuta.fashionsdk.configuration.features.welcome.AiutaWelcomeScreenFeature>()

    Box(
        modifier =
        modifier
            .clip(RoundedCornerShape(4.dp))
            .background(theme.color.background)
            .clickableUnindicated { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = welcomeScreenFeature.strings.welcomeButtonStart,
            style = theme.button.typography.buttonM,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )
    }
}
