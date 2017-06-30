package com.juniperphoton.flipperviewlib

import android.view.View
import com.juniperphoton.flipperviewlib.animation.MtxRotationAnimation

/**
 * Extension method to rotate a view perspectively.
 */
fun View.rotatePerspectively(axis: Int, deg: Int) {
    val animation = MtxRotationAnimation(axis, deg, deg, 1)
    startAnimation(animation)
}