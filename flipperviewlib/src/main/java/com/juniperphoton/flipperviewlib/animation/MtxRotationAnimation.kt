package com.juniperphoton.flipperviewlib.animation

import android.graphics.Camera
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class MtxRotationAnimation(axis: Int, from: Int, to: Int, duration: Long) : Animation() {
    companion object {
        val ROTATION_X = 1
        val ROTATION_Y = 1 shl 1

        fun setRotation(rotationFun: () -> Int, view: View, deg: Int) {
            val animation = MtxRotationAnimation(rotationFun(), deg, deg, 1)
            view.startAnimation(animation)
        }
    }

    var centerX = 0
    var centerY = 0

    val camera = Camera()

    var fromDeg = 0
    var toDeg = 0
    var rotationAxis = ROTATION_X

    init {
        rotationAxis = axis
        fromDeg = from
        toDeg = to
        setDuration(duration)
    }

    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)

        centerX = width / 2
        centerY = height / 2

        fillAfter = true
    }

    override fun applyTransformation(interpolatedTime: Float, transform: Transformation?) {
        super.applyTransformation(interpolatedTime, transform)

        val targetDeg = (fromDeg + (toDeg - fromDeg) * interpolatedTime).toInt()

        val matrix = transform!!.matrix
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