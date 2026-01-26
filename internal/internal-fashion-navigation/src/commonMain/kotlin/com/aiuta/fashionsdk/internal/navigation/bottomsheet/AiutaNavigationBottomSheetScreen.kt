package com.aiuta.fashionsdk.internal.navigation.bottomsheet

import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.internal.navigation.AiutaNavKey

@Immutable
public interface AiutaNavigationBottomSheetScreen : AiutaNavKey {
    public object IDLE : AiutaNavigationBottomSheetScreen
}
