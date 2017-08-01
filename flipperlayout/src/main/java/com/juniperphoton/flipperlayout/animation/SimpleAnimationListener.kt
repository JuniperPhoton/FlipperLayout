package com.juniperphoton.flipperlayout.animation

import android.view.animation.Animation

/**
 * Convenience class for Animation.AnimationListener.
 */
abstract class SimpleAnimationListener : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationStart(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
    }
}