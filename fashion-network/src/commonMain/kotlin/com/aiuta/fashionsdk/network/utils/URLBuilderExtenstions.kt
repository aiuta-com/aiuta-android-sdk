package com.aiuta.fashionsdk.network.utils

import io.ktor.http.URLBuilder

private const val DEFAULT_LIMIT_KEY = "limit"
private const val DEFAULT_OFFSET_KEY = "offset"

/**
 * Extension for [URLBuilder] that safely appends a nullable string parameter.
 * If the parameter is null, it will not be added to the URL.
 *
 * @param key The parameter key to append
 * @param parameter The nullable string parameter value
 */
public fun URLBuilder.saveAppend(
    key: String,
    parameter: String?,
) {
    parameter?.let {
        parameters.append(key, it)
    }
}

/**
 * Extension for [URLBuilder] that safely appends a nullable integer parameter.
 * If the parameter is null, it will not be added to the URL.
 *
 * @param key The parameter key to append
 * @param parameter The nullable integer parameter value
 */
public fun URLBuilder.saveAppend(
    key: String,
    parameter: Int?,
) {
    parameter?.let {
        parameters.append(key, it.toString())
    }
}

/**
 * Extension for [URLBuilder] that safely appends a limit parameter for paging requests.
 * If the limit is null, it will not be added to the URL.
 *
 * @param limit The nullable integer limit value
 */
public fun URLBuilder.saveAppendLimit(limit: Int?) {
    saveAppend(
        key = DEFAULT_LIMIT_KEY,
        parameter = limit,
    )
}

public fun URLBuilder.saveAppendOffset(offset: Int?) {
    saveAppend(
        key = DEFAULT_OFFSET_KEY,
        parameter = offset,
    )
}
