package com.juniperphoton.flipperlayout

import android.annotation.TargetApi
import android.os.Build
import android.view.View

/**
 * Helper class to save elevation of a [View] and restore it.
 *
 * The caller must call [clear] in some time to prevent memory leak.
 */
object ElevationHelper {
    private val elevationMap: MutableMap<View, Float> = mutableMapOf()
    private val translationZMap: MutableMap<View, Float> = mutableMapOf()

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun save(view: View) {
        if (!elevationMap.containsKey(view)) {
            elevationMap[view] = view.elevation
        }
        if (!translationZMap.containsKey(view)) {
            translationZMap[view] = view.translationZ
        }

        view.elevation = 0f
        view.translationZ = 0f
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun restore(view: View) {
        if (elevationMap.containsKey(view)) {
            view.elevation = elevationMap[view] ?: 0f
        }
        if (translationZMap.containsKey(view)) {
            view.translationZ = translationZMap[view] ?: 0f
        }
    }

    fun clear() {
        elevationMap.clear()
        translationZMap.clear()
    }
}