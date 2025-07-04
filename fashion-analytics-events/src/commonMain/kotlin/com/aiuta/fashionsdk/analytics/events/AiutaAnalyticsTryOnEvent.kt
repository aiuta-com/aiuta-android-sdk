package com.aiuta.fashionsdk.analytics.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(AiutaAnalyticsEvent.EventType.TRY_ON_EVENT)
public class AiutaAnalyticsTryOnEvent(
    @SerialName("event")
    public val event: AiutaAnalyticsTryOnEventType,
    @SerialName("errorType")
    public val errorType: AiutaAnalyticsTryOnErrorType? = null,
    @SerialName("abortReason")
    public val abortReason: AiutaAnalyticsTryOnAbortedReasonType? = null,
    @SerialName("errorMessage")
    public val errorMessage: String? = null,
    @SerialName("pageId")
    public override val pageId: AiutaAnalyticsPageId?,
    @SerialName("productIds")
    override val productIds: List<String>,
    // Try on meta info
    @SerialName("uploadDuration")
    public val uploadDuration: Double? = null,
    @SerialName("tryOnDuration")
    public val tryOnDuration: Double? = null,
    @SerialName("downloadDuration")
    public val downloadDuration: Double? = null,
    @SerialName("totalDuration")
    public val totalDuration: Double? = null,
) : AiutaAnalyticsEvent

@Serializable
public enum class AiutaAnalyticsTryOnEventType {
    @SerialName("initiated")
    INITIATED,

    @SerialName("photoUploaded")
    PHOTO_UPLOADED,

    @SerialName("tryOnStarted")
    TRY_ON_STARTED,

    @SerialName("tryOnFinished")
    TRY_ON_FINISHED,

    @SerialName("tryOnAborted")
    TRY_ON_ABORTED,

    @SerialName("tryOnError")
    TRY_ON_ERROR,
}

@Serializable
public enum class AiutaAnalyticsTryOnErrorType {
    @SerialName("preparePhotoFailed")
    PREPARE_PHOTO_FAILED,

    @SerialName("uploadPhotoFailed")
    UPLOAD_PHOTO_FAILED,

    @SerialName("authorizationFailed")
    AUTHORIZATION_FAILED,

    @SerialName("requestOperationFailed")
    REQUEST_OPERATION_FAILED,

    @SerialName("startOperationFailed")
    START_OPERATION_FAILED,

    @SerialName("operationFailed")
    OPERATION_FAILED,

    @Deprecated("Use AiutaAnalyticsTryOnAbortedReasonType")
    @SerialName("operationAborted")
    OPERATION_ABORTED,

    @SerialName("operationTimeout")
    OPERATION_TIMEOUT,

    @SerialName("operationEmptyResults")
    OPERATION_EMPTY_RESULTS,

    @SerialName("downloadResultFailed")
    DOWNLOAD_RESULT_FAILED,

    @SerialName("internalSdkError")
    INTERNAL_SDK_ERROR,
}

@Serializable
public enum class AiutaAnalyticsTryOnAbortedReasonType {

    @SerialName("operationAborted")
    OPERATION_ABORTED,

    @SerialName("userCancelled")
    USER_CANCELED,
}
