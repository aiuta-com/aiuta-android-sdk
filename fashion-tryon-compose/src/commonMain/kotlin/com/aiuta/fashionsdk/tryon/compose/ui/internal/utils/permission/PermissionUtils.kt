package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.permission

import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.logger.w
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.permission.exception.AiutaDeniedAlwaysException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun CoroutineScope.actionWithPermission(
    pickerSource: AiutaPickerSource,
    permissionHandler: AiutaPermissionHandler,
    logger: AiutaLogger?,
    onGranted: suspend () -> Unit,
    onAlwaysDenied: suspend () -> Unit,
) {
    launch {
        when {
            !shouldAskPermission(pickerSource) -> onGranted()

            permissionHandler.isPermissionGranted(pickerSource) -> onGranted()

            else -> {
                try {
                    val permissionState = permissionHandler.getPermissionState(pickerSource)

                    logger?.d("actionWithPermission(): permissionState - $permissionState")

                    if (permissionState == AiutaPermissionState.DENIED_ALWAYS) {
                        onAlwaysDenied()
                    } else {
                        permissionHandler.providePermission(pickerSource)
                        // Permission granted, let's do main action
                        onGranted()
                    }
                } catch (e: AiutaDeniedAlwaysException) {
                    logger?.w("actionWithPermission(): DeniedAlwaysException exception - $e")
                    onAlwaysDenied()
                } catch (e: Exception) {
                    logger?.e("actionWithPermission(): general exception - $e")
                    // Just intercept
                }
            }
        }
    }
}

internal expect fun shouldAskPermission(permission: AiutaPickerSource): Boolean

internal expect fun isPickerSourceAvailable(permission: AiutaPickerSource): Boolean
