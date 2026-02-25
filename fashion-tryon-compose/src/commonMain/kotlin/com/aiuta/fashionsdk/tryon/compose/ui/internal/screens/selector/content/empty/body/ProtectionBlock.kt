package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.content.empty.body

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.configuration.features.picker.protection.AiutaImagePickerProtectionDisclaimerFeature

@Composable
internal fun ProtectionBlock(
    modifier: Modifier = Modifier,
    protectionFeature: AiutaImagePickerProtectionDisclaimerFeature,
) {
    val theme = LocalTheme.current

    val iconId = "ICON_ID"
    val text = buildAnnotatedString {
        appendInlineContent(iconId, "[icon]")
        append(" ")
        append(protectionFeature.strings.protectionDisclaimer)
    }

    val inlineContent = mapOf(
        Pair(
            iconId,
            InlineTextContent(
                Placeholder(
                    width = 16.sp,
                    height = 16.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                ),
            ) {
                AiutaIcon(
                    modifier = Modifier.size(16.dp),
                    icon = protectionFeature.icons.protection16,
                    contentDescription = null,
                    tint = theme.color.secondary,
                )
            },
        ),
    )

    Text(
        modifier = modifier,
        text = text,
        style = theme.label.typography.footnote,
        color = theme.color.secondary,
        textAlign = TextAlign.Center,
        inlineContent = inlineContent,
    )
}
