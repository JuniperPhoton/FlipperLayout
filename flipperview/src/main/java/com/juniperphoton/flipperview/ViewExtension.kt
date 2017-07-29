package com.juniperphoton.flipperview

import android.view.View
import com.juniperphoton.flipperview.animation.MtxRotationAnimation

/**
 * Extension method to rotate a view perspectively.
 */
fun View.rotatePerspectively(axis: Int, deg: Int) {
    val animation = MtxRotationAnimation(axis, deg, deg, 1)
    startAnimation(animation)
}