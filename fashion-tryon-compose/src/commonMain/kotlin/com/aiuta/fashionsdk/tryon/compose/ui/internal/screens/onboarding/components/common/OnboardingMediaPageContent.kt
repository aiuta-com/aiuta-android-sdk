package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.resources.media.AiutaMedia
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaVideoSurface
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent

/**
 * Shared layout for onboarding pages that present a media surface above a
 * centred title/subtitle block (e.g. "How it works" and "Best results").
 *
 * @param media The media (image with optional looping video) shown on top
 * @param title The page title
 * @param subtitle The page subtitle
 * @param pageId The analytics page id reported when this page is shown
 * @param modifier The modifier applied to the page container
 */
@Composable
internal fun OnboardingMediaPageContent(
    media: AiutaMedia,
    title: String,
    subtitle: String,
    pageId: AiutaAnalyticsPageId,
    modifier: Modifier = Modifier,
) {
    sendPageEvent(pageId = pageId)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AiutaVideoSurface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f),
            video = media,
        )

        CentredTextBlock(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f),
            title = title,
            subtitle = subtitle,
        )
    }
}
