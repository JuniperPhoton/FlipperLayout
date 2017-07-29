package com.juniperphoton.flipperview.animation

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * A class that performs the transformation actually.
 */
class MtxRotationAnimation(private var rotationAxis: Int,
                           private var fromDeg: Int,
                           private var toDeg: Int,
                           duration: Long
) : Animation() {
    companion object {
        const val ROTATION_X = 1
        const val ROTATION_Y = 1 shl 1
    }

    private var centerX: Int = 0
    private var centerY: Int = 0

    private val camera: Camera = Camera()

    init {
        setDuration(duration)
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)

        centerX = width / 2
        centerY = height / 2

        fillAfter = true
    }

    override fun applyTransformation(interpolatedTime: Float, transform: Transformation) {
        super.applyTransformation(interpolatedTime, transform)

        val targetDeg = (fromDeg + (toDeg - fromDeg) * interpolatedTime).toInt()

        val matrix = transform.matrix
        camera.save()
        if (rotationAxis and ROTATION_X == ROTATION_X) {
            camera.rotateX(targetDeg.toFloat())
        }
        if (rotationAxis and ROTATION_Y == ROTATION_Y) {
            camera.rotateY(targetDeg.toFloat())
        }
        camera.getMatrix(matrix)
        matrix.preTranslate((-centerX).toFloat(), (-centerY).toFloat())
        matrix.postTranslate(centerX.toFloat(), centerY.toFloat())
        camera.restore()
    }
}