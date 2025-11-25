package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.permission.utils.findActivity
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import java.util.UUID
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

@Composable
internal fun rememberAiutaPermissionsController(): AiutaPermissionsControllerImpl {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    return remember(
        context,
        lifecycleOwner,
    ) {
        AiutaPermissionsControllerImpl(
            applicationContext = context,
            activity = context.findActivity(),
            lifecycleOwner = lifecycleOwner,
        )
    }
}

/**
 * This is copy past from Moko permission library to
 * support possibility init contrroler with [ActivityResultRegistryOwner],
 * therefore [bind] method is extended with resultRegistry param, but under the
 * hood it has same logic as origin.
 *
 * We need it to support custom logic of Flutter bottom sheet injection in parent activity
 *
 * **See Also:** [Link to origin](https://github.com/icerockdev/moko-permissions/blob/d6aded96b30aebe64bba6730319800400ab50e29/permissions/src/androidMain/kotlin/dev/icerock/moko/permissions/PermissionsControllerImpl.kt)
 */
@Suppress("TooManyFunctions")
internal class AiutaPermissionsControllerImpl(
    private val applicationContext: Context,
    private val activity: Activity,
    private val lifecycleOwner: LifecycleOwner,
) : PermissionsController {
    private val activityResultRegistryHolder = MutableStateFlow<ActivityResultRegistryOwner?>(null)

    private val mutex: Mutex = Mutex()

    private val launcherHolder = MutableStateFlow<ActivityResultLauncher<Array<String>>?>(null)

    private var permissionCallback: PermissionCallback? = null

    private val key = UUID.randomUUID().toString()

    override fun bind(activity: ComponentActivity) {
        bind(resultRegistry = activity)
    }

    fun bind(resultRegistry: ActivityResultRegistryOwner) {
        this.activityResultRegistryHolder.value = resultRegistry

        val launcher = resultRegistry.activityResultRegistry.register(
            key,
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            val isCancelled = permissions.isEmpty()

            val permissionCallback = permissionCallback ?: return@register

            if (isCancelled) {
                permissionCallback.callback.invoke(
                    Result.failure(RequestCanceledException(permissionCallback.permission)),
                )
                return@register
            }

            val success = permissions.values.all { it }

            if (success) {
                permissionCallback.callback.invoke(Result.success(Unit))
            } else {
                if (shouldShowRequestPermissionRationale(permissions.keys.first())) {
                    permissionCallback.callback.invoke(
                        Result.failure(DeniedException(permissionCallback.permission)),
                    )
                } else {
                    permissionCallback.callback.invoke(
                        Result.failure(DeniedAlwaysException(permissionCallback.permission)),
                    )
                }
            }
        }

        launcherHolder.value = launcher

        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    this@AiutaPermissionsControllerImpl.activityResultRegistryHolder.value = null
                    this@AiutaPermissionsControllerImpl.launcherHolder.value = null
                    source.lifecycle.removeObserver(this)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    override suspend fun providePermission(permission: Permission) {
        mutex.withLock {
            val launcher = awaitActivityResultLauncher()
            val platformPermission = permission.delegate.getPlatformPermission()
            suspendCoroutine { continuation ->
                requestPermission(
                    launcher,
                    permission,
                    platformPermission,
                ) { continuation.resumeWith(it) }
            }
        }
    }

    private fun requestPermission(
        launcher: ActivityResultLauncher<Array<String>>,
        permission: Permission,
        permissions: List<String>,
        callback: (Result<Unit>) -> Unit,
    ) {
        permissionCallback = PermissionCallback(permission, callback)
        launcher.launch(permissions.toTypedArray())
    }

    private suspend fun awaitActivityResultLauncher(): ActivityResultLauncher<Array<String>> {
        println("in awaitActivityResultLauncher")
        val activityResultLauncher = launcherHolder.value
        if (activityResultLauncher != null) return activityResultLauncher

        return withTimeoutOrNull(AWAIT_ACTIVITY_TIMEOUT_DURATION_MS) {
            launcherHolder.filterNotNull().first()
        } ?: error(
            "activityResultLauncher is null, `bind` function was never called," +
                " consider calling permissionsController.bind(activity)" +
                " or BindEffect(permissionsController) in the composable function," +
                " check the documentation for more info: " +
                "https://github.com/icerockdev/moko-permissions/blob/master/README.md",
        )
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean = getPermissionState(permission) == PermissionState.Granted

    @Suppress("ReturnCount")
    override suspend fun getPermissionState(permission: Permission): PermissionState {
        permission.delegate.getPermissionStateOverride(applicationContext)?.let { return it }
        val permissions: List<String> = permission.delegate.getPlatformPermission()
        val status: List<Int> = permissions.map {
            ContextCompat.checkSelfPermission(applicationContext, it)
        }
        val isAllGranted: Boolean = status.all { it == PackageManager.PERMISSION_GRANTED }
        if (isAllGranted) return PermissionState.Granted

        val isAllRequestRationale: Boolean = permissions.all {
            shouldShowRequestPermissionRationale(it)
        }
        return if (isAllRequestRationale) {
            PermissionState.Denied
        } else {
            PermissionState.NotGranted
        }
    }

    private fun shouldShowRequestPermissionRationale(
        permission: String,
    ): Boolean = ActivityCompat.shouldShowRequestPermissionRationale(
        activity,
        permission,
    )

    override fun openAppSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", applicationContext.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        applicationContext.startActivity(intent)
    }

    private companion object {
        private const val AWAIT_ACTIVITY_TIMEOUT_DURATION_MS = 2000L
    }
}

private class PermissionCallback(
    val permission: Permission,
    val callback: (Result<Unit>) -> Unit,
)
