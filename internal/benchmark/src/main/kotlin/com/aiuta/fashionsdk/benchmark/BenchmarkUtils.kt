package com.aiuta.fashionsdk.benchmark

import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

/** Waits until a node with the given testTag (resource id) appears. */
internal fun UiDevice.waitForTag(tag: String, timeoutMs: Long = UI_TIMEOUT_MS): Boolean = wait(Until.hasObject(By.res(tag)), timeoutMs)

/** Finds a node by testTag, or null if absent. */
internal fun UiDevice.findByTag(tag: String): UiObject2? = findObject(By.res(tag))

/**
 * Waits for a tagged node and clicks it. Returns false (without throwing) if it
 * never appears, so optional steps can be skipped gracefully during generation.
 */
internal fun UiDevice.tapTag(tag: String, timeoutMs: Long = UI_TIMEOUT_MS): Boolean {
    if (!waitForTag(tag, timeoutMs)) return false
    val node = findByTag(tag) ?: return false
    node.click()
    waitForIdle()
    return true
}

/**
 * Flings an element down then up. A gesture margin keeps the fling clear of the
 * system gesture area so it is not swallowed as a back/recents swipe.
 */
internal fun UiDevice.flingDownUp(element: UiObject2) {
    element.setGestureMargin(displayWidth / 5)
    element.fling(Direction.DOWN)
    waitForIdle()
    element.fling(Direction.UP)
    waitForIdle()
}
