package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalRippleConfiguration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.buildAnnotatedStringFromHtml
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneFeature
import com.aiuta.fashionsdk.configuration.features.consent.models.AiutaConsentType
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.onboarding.AiutaConsentUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendPageEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.CenterAlignmentLine
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.createCenterAlignmentLine

@Composable
internal fun ConsentContent(
    modifier: Modifier = Modifier,
    consentsList: SnapshotStateList<AiutaConsentUiModel>,
    onUpdateConsentState: (consent: AiutaConsentUiModel, newState: Boolean) -> Unit,
) {
    val theme = LocalTheme.current

    val consentStandaloneFeature = strictProvideFeature<AiutaConsentStandaloneFeature>()

    sendPageEvent(pageId = AiutaAnalyticsPageId.CONSENT)

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 8.dp),
    ) {
        item(
            key = "CONSENT_HEADER",
            contentType = { "CONSENT_HEADER_TYPE" },
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = consentStandaloneFeature.strings.consentTitle,
                    style = theme.label.typography.titleL,
                    color = theme.color.primary,
                    textAlign = TextAlign.Start,
                )

                consentStandaloneFeature.icons.consentTitle24?.let { consentTitle24 ->
                    Spacer(Modifier.width(8.dp))

                    AiutaIcon(
                        modifier = Modifier.size(24.dp),
                        icon = consentTitle24,
                        contentDescription = null,
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = buildAnnotatedStringFromHtml(
                    consentStandaloneFeature.strings.consentDescriptionHtml,
                ),
                style = theme.label.typography.regular,
                color = theme.color.primary,
                textAlign = TextAlign.Start,
            )

            Spacer(Modifier.height(28.dp))
        }

        itemsIndexed(
            items = consentsList,
            key = { _, consentModel -> consentModel.consent.id },
            contentType = { _, _ -> "CONSENT_POINT_TYPE" },
        ) { index, consentModel ->
            val consentType = consentModel.consent.type
            val sharedModifier = Modifier.fillMaxWidth()
            val finalModifier = if (consentStandaloneFeature.styles.drawBordersAroundConsents) {
                sharedModifier
                    .border(
                        color = theme.color.border,
                        width = 1.dp,
                        shape = RoundedCornerShape(
                            topStart = 8.dp.takeIf { index == 0 } ?: 0.dp,
                            topEnd = 8.dp.takeIf { index == 0 } ?: 0.dp,
                            bottomStart = 8.dp.takeIf {
                                index == consentStandaloneFeature.data.consents.lastIndex
                            } ?: 0.dp,
                            bottomEnd = 8.dp.takeIf {
                                index == consentStandaloneFeature.data.consents.lastIndex
                            } ?: 0.dp,
                        ),
                    )
                    .padding(16.dp)
            } else {
                sharedModifier
            }

            AgreePoint(
                modifier = finalModifier,
                text = consentModel.consent.consentHtml,
                isAgreementChecked = consentModel.isObtained,
                type = consentType,
                onAgreementCheckedChange = { newState ->
                    onUpdateConsentState(consentModel, newState)
                },
            )

            if (!consentStandaloneFeature.styles.drawBordersAroundConsents) {
                // Should add padding, because it's not handled by inner padding of point
                Spacer(Modifier.height(28.dp))
            }
        }

        consentStandaloneFeature.strings.consentFooterHtml?.let { footerText ->
            item(
                key = "CONSENT_FOOTER",
                contentType = { "CONSENT_FOOTER" },
            ) {
                if (consentStandaloneFeature.styles.drawBordersAroundConsents) {
                    Spacer(Modifier.height(28.dp))
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildAnnotatedStringFromHtml(footerText),
                    style = theme.label.typography.regular,
                    color = theme.color.primary,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AgreePoint(
    modifier: Modifier = Modifier,
    text: String,
    type: AiutaConsentType,
    isAgreementChecked: Boolean,
    onAgreementCheckedChange: (Boolean) -> Unit,
) {
    val theme = LocalTheme.current

    val lineIndexToCenter = 0
    var topTextPosition by remember { mutableFloatStateOf(0f) }
    var bottomTextPosition by remember { mutableFloatStateOf(0f) }

    Row(
        modifier = modifier,
    ) {
        if (type != AiutaConsentType.IMPLICIT_WITHOUT_CHECKBOX) {
            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                Checkbox(
                    modifier = Modifier
                        .size(20.dp)
                        .alignBy(CenterAlignmentLine)
                        .createCenterAlignmentLine(),
                    checked = isAgreementChecked,
                    onCheckedChange = onAgreementCheckedChange,
                    enabled = type != AiutaConsentType.IMPLICIT_WITH_CHECKBOX,
                    colors =
                    CheckboxDefaults.colors(
                        checkedColor = theme.color.brand,
                        uncheckedColor = theme.color.neutral,
                        checkmarkColor = theme.color.onDark,
                    ),
                )
            }

            Spacer(Modifier.width(16.dp))
        }

        Text(
            modifier =
            Modifier
                .weight(1f)
                .clickableUnindicated {
                    onAgreementCheckedChange(!isAgreementChecked)
                }
                .alignBy(CenterAlignmentLine)
                .createCenterAlignmentLine(topTextPosition, bottomTextPosition),
            text = buildAnnotatedStringFromHtml(text),
            style = theme.label.typography.regular,
            color = theme.color.primary,
            textAlign = TextAlign.Start,
            onTextLayout = { textLayout: TextLayoutResult ->
                topTextPosition = textLayout.getLineTop(lineIndexToCenter)
                bottomTextPosition = textLayout.getLineBottom(lineIndexToCenter)
            },
        )
    }
}
