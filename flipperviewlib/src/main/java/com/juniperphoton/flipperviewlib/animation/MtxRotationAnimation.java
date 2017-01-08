package com.juniperphoton.flipperviewlib.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MtxRotationAnimation extends Animation {
    public final static int ROTATION_X = 1;
    public final static int ROTATION_Y = 1 << 1;

    private int mCenterX, mCenterY;
    private Camera mCamera = new Camera();

    private int mFromDeg;
    private int mToDeg;
    private int mRotationAxis = ROTATION_X;

    private boolean mUsePerspective = true;

    public MtxRotationAnimation(int rotationAxis, int fromDeg, int toDeg, int duration) {
        mFromDeg = fromDeg;
        mToDeg = toDeg;
        mRotationAxis = rotationAxis;
        setDuration(duration);
    }

    public void setUsePerspective(boolean usePerspective) {
        mUsePerspective = usePerspective;
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

        int targetDeg = (int) (mFromDeg + (mToDeg - mFromDeg) * interpolatedTime);

        Matrix matrix = transform.getMatrix();
        mCamera.save();
        if ((mRotationAxis & ROTATION_X) == ROTATION_X) {
            mCamera.rotateX(targetDeg);
        }
        if ((mRotationAxis & ROTATION_Y) == ROTATION_Y) {
            mCamera.rotateY(targetDeg);
        }
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
        mCamera.restore();
    }

    public static void setRotationX(View view, int deg) {
        MtxRotationAnimation animation = new MtxRotationAnimation(ROTATION_X, deg, deg, 1);
        view.startAnimation(animation);
    }

    public static void setRotationY(View view, int deg) {
        MtxRotationAnimation animation = new MtxRotationAnimation(ROTATION_Y, deg, deg, 1);
        view.startAnimation(animation);
    }
}
