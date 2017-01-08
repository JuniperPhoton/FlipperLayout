package com.juniperphoton.flipperviewlib.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class MtxRotationAnimation extends Animation {
    private int mCenterX, mCenterY;
    private Camera mCamera = new Camera();

    private int mFromDeg;
    private int mToDeg;

    public MtxRotationAnimation(int fromDeg, int toDeg, int duration) {
        mFromDeg = fromDeg;
        mToDeg = toDeg;
        setDuration(duration);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        mCenterX = width / 2;
        mCenterY = height / 2;

        setFillAfter(true);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transform) {
        super.applyTransformation(interpolatedTime, transform);

        int deltaDeg = mToDeg - mFromDeg;
        int deg = (int) (mFromDeg + deltaDeg * interpolatedTime);

        Matrix matrix = transform.getMatrix();
        mCamera.save();
        mCamera.rotateX(deg);
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
        mCamera.restore();
    }

    public static void setRotationX(View view, int deg) {
        MtxRotationAnimation animation = new MtxRotationAnimation(deg, deg, 1);
        view.startAnimation(animation);
    }
}
