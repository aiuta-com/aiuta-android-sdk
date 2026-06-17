package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.exception

/**
 * Thrown when a try-on generation fails at any stage of the pipeline.
 *
 * @property type The stage/category of failure, see [AiutaTryOnExceptionType].
 * @property message Human-readable error details. Defaults to a message derived from [type].
 * @property abortReason The reason the operation was aborted by the backend. Only present
 * when [type] is [AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED]; `null` otherwise.
 */
public class AiutaTryOnGenerationException(
    public val type: AiutaTryOnExceptionType,
    override val message: String? = "Failed to generate image, type of error - $type",
    public val abortReason: AiutaTryOnAbortReason? = null,
) : RuntimeException()

/**
 * Stage of the try-on generation pipeline at which a failure occurred.
 */
public enum class AiutaTryOnExceptionType {
    /** Preparing the local photo before upload failed. */
    PREPARE_PHOTO_FAILED,

    /** Uploading the photo to the backend failed. */
    UPLOAD_PHOTO_FAILED,

    /** Creating the generation operation failed. */
    START_OPERATION_FAILED,

    /** The generation operation finished with a failed status. */
    OPERATION_FAILED,

    /** The generation operation was aborted by the backend; see [AiutaTryOnGenerationException.abortReason]. */
    OPERATION_ABORTED_FAILED,

    /** The generation operation did not complete within the allowed time. */
    OPERATION_TIMEOUT_FAILED,

    /** The generation operation succeeded but returned no images. */
    OPERATION_EMPTY_RESULTS_FAILED,

    /** Downloading the generated result failed. */
    DOWNLOAD_RESULT_FAILED,
}

/**
 * Returns `true` if this exception is an [AiutaTryOnGenerationException] caused by the
 * backend aborting the operation ([AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED]).
 */
public fun Exception.isTryOnGenerationAbortedException(): Boolean = this is AiutaTryOnGenerationException && this.type == AiutaTryOnExceptionType.OPERATION_ABORTED_FAILED
