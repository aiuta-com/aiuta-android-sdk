package com.aiuta.fashionsdk.compose.uikit.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import com.mohamedrejeb.ksoup.entities.KsoupEntities
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

@Composable
public fun buildAnnotatedStringFromHtml(
    input: String,
    isClickable: Boolean = true,
): AnnotatedString {
    val builder = AnnotatedString.Builder()
    val currentLink = remember { mutableStateOf<String?>(null) }

    val handler = KsoupHtmlHandler.Builder()
        .onOpenTag { name, attributes, _ ->
            when (name.lowercase()) {
                "b", "strong" -> builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                "i", "em" -> builder.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                "u" -> builder.pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                "s", "strike" -> builder.pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                "a" -> currentLink.value = attributes["href"]?.takeIf { isClickable }
                "br" -> builder.append("\n")
                "p" -> builder.append("\n\n")
            }
        }
        .onCloseTag { name, _ ->
            when (name.lowercase()) {
                "b", "strong", "i", "em", "u", "s", "strike" -> builder.pop()
            }
        }
        .onText { text ->
            currentLink.value?.let { link ->
                builder.withLink(LinkAnnotation.Url(link)) {
                    append(text)
                }
                currentLink.value = null
            } ?: builder.append(text)
        }
        .build()

    val parser = KsoupHtmlParser(handler)
    val decodedHtml = KsoupEntities.decodeHtml(input)
    parser.write(decodedHtml)
    parser.end()

    return builder.toAnnotatedString()
}
