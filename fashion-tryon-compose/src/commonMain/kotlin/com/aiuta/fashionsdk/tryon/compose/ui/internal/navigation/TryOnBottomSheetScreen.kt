package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.AiutaNavigationBottomSheetScreen

@Immutable
internal sealed interface TryOnBottomSheetScreen : AiutaNavigationBottomSheetScreen {
    public class ImagePicker(
        public val originPageId: AiutaAnalyticsPageId,
    ) : TryOnBottomSheetScreen

    public object FitDisclaimer : TryOnBottomSheetScreen

    public class Feedback(
        public val productIds: List<String>,
    ) : TryOnBottomSheetScreen

    public class ExtraFeedback(
        public val optionIndex: Int,
        public val productIds: List<String>,
    ) : TryOnBottomSheetScreen

    public class ProductInfo(
        public val primaryButtonState: PrimaryButtonState,
        public val productItem: ProductItem,
        public val originPageId: AiutaAnalyticsPageId,
    ) : TryOnBottomSheetScreen {
        public enum class PrimaryButtonState {
            ADD_TO_CART,
        }
    }

    public object GeneratedOperations : TryOnBottomSheetScreen
}
