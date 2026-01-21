package com.aiuta.fashionsdk.sizefit.core.exceptions

/**
 * Exception thrown when the API returns an error status code.
 *
 * @property statusCode The HTTP status code returned by the API
 */
public class SizeFitApiException(
    public val statusCode: Int,
    message: String = "API request failed with status code: $statusCode",
) : Exception(message)

/**
 * Exception thrown when a network error occurs during the API request.
 */
public class SizeFitNetworkException(
    cause: Throwable,
) : Exception("Network error occurred during SizeFit API request", cause)
