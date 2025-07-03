package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.models.product.ProductItem

@Immutable
internal sealed interface NavigationBottomSheetScreen {
    public object IDLE : NavigationBottomSheetScreen

    public class ImagePicker(
        public val originPageId: AiutaAnalyticsPageId,
    ) : NavigationBottomSheetScreen

    public object FitDisclaimer : NavigationBottomSheetScreen

    public class Feedback(
        public val productIds: List<String>,
    ) : NavigationBottomSheetScreen

    public class ExtraFeedback(
        public val optionIndex: Int,
        public val productIds: List<String>,
    ) : NavigationBottomSheetScreen

    public class ProductInfo(
        public val primaryButtonState: PrimaryButtonState,
        public val productItem: ProductItem,
        public val originPageId: AiutaAnalyticsPageId,
    ) : NavigationBottomSheetScreen {
        public enum class PrimaryButtonState {
            ADD_TO_CART,

            TRY_ON,
        }
    }

    public object GeneratedOperations : NavigationBottomSheetScreen
}
