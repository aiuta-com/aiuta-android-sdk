package com.aiuta.fashionsdk.compose.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.compose.tokens.icon.AiutaIcons
import com.aiuta.fashionsdk.compose.tokens.icon.AiutaResourceIcons

/**
 * Default implementation of [AiutaIcons].
 */
public fun defaultAiutaIcons(): AiutaIcons {
    return AiutaResourceIcons(
        recent100 = R.drawable.ic_recent_100,
        welcomeScreen82 = R.drawable.ic_welcome_screen_82,
        error36 = R.drawable.ic_error_36,
        like36 = R.drawable.ic_like_36,
        dislike36 = R.drawable.ic_dislike_36,
        back24 = R.drawable.ic_back_24,
        camera24 = R.drawable.ic_camera_24,
        cameraFill24 = R.drawable.ic_camera_fill_24,
        checkCorrect24 = R.drawable.ic_check_correct_24,
        checkNotCorrect24 = R.drawable.ic_check_not_correct_24,
        close24 = R.drawable.ic_close_24,
        trash24 = R.drawable.ic_trash_24,
        history24 = R.drawable.ic_history_24,
        share24 = R.drawable.ic_share_24,
        wishlist24 = R.drawable.ic_wishlist_24,
        wishlistFill24 = R.drawable.ic_wishlist_fill_24,
        check16 = R.drawable.ic_check_16,
        magic16 = R.drawable.ic_magic_16,
        lock16 = R.drawable.ic_lock_16,
        arrow16 = R.drawable.ic_arrow_16,
    )
}

@Composable
public fun rememberDefaultAiutaIcons(): AiutaIcons {
    return remember { defaultAiutaIcons() }
}
