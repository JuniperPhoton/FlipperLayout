package com.juniperphoton.flipperviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.juniperphoton.flipperviewlib.animation.AnimationEnd;
import com.juniperphoton.flipperviewlib.animation.MtxRotationAnimation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class FlipperView extends FrameLayout implements View.OnClickListener {
    @IntDef({FLIP_DIRECTION_BACK_TO_FRONT, FLIP_DIRECTION_FRONT_TO_BACK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

    @IntDef({AXIS_X, AXIS_Y})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Axis {
    }

    private final static int FLIP_DIRECTION_BACK_TO_FRONT = 0;
    private final static int FLIP_DIRECTION_FRONT_TO_BACK = 1;
    private final static int DEFAULT_DURATION = 200;
    private final static int AXIS_X = 0;
    private final static int AXIS_Y = 1;

    private int mFlipDirection;
    private int mDisplayIndex = 0;
    private int mDuration;
    private int mFlipAxis;
    private boolean mTapToFlip;
    private boolean mUsePerspective = true;

    private View mDisplayView;

    private boolean mPrepared;
    private boolean mAnimating;

    public FlipperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlipperView);
        mDisplayIndex = typedArray.getInt(R.styleable.FlipperView_defaultIndex, 0);
        mFlipDirection = typedArray.getInt(R.styleable.FlipperView_flipDirection, FLIP_DIRECTION_BACK_TO_FRONT);
        mUsePerspective = typedArray.getBoolean(R.styleable.FlipperView_usePerspective, true);
        mFlipAxis = typedArray.getInt(R.styleable.FlipperView_flipAxis, AXIS_X);
        mDuration = typedArray.getInt(R.styleable.FlipperView_duration, DEFAULT_DURATION);
        mTapToFlip = typedArray.getBoolean(R.styleable.FlipperView_tapToFlip, false);
        typedArray.recycle();

        setClipChildren(false);
        setOnClickListener(this);
    }

    public void setFlipDirection(@Direction int direction) {
        mFlipDirection = direction;
    }

    @Direction
    public int getFlipDirection() {
        return mFlipDirection;
    }

    public void setFlipAxis(@Axis int axis) {
        mFlipAxis = axis;
    }

    @Axis
    public int getFlipAxis() {
        return mFlipAxis;
    }

    private int getMtxRotationAxis() {
        if (mFlipAxis == 0) {
            return MtxRotationAnimation.ROTATION_X;
        }
        if (mFlipAxis == 1) {
            return MtxRotationAnimation.ROTATION_Y;
        }
        return MtxRotationAnimation.ROTATION_X;
    }

    private void prepare() {
        List<View> children = new ArrayList();
        for (int i = 0; i < getChildCount(); i++) {
            children.add(getChildAt(i));
            getChildAt(i).setVisibility(INVISIBLE);
        }
        mDisplayView = getChildAt(mDisplayIndex);
        mDisplayView.setVisibility(VISIBLE);

        mPrepared = true;
    }

    public void next() {
        if (!mPrepared) {
            prepare();
        }
        if (mAnimating) return;
        int nextIndex = mDisplayIndex + 1;
        nextIndex = checkIndex(nextIndex);
        mDisplayIndex = nextIndex;
        next(nextIndex);
    }

    public void previous() {
        if (!mPrepared) {
            prepare();
        }
        if (mAnimating) return;
        int prevIndex = mDisplayIndex - 1;
        prevIndex = checkIndex(prevIndex);
        mDisplayIndex = prevIndex;
        next(prevIndex);
    }

    public void next(int nextIndex) {

        final View nextView = getChildAt(nextIndex);
        nextView.setVisibility(VISIBLE);
        int axis = getMtxRotationAxis();
        if (axis == MtxRotationAnimation.ROTATION_X) {
            MtxRotationAnimation.setRotationX(nextView, mFlipDirection == FLIP_DIRECTION_BACK_TO_FRONT ? -90 : 90);
        } else {
            MtxRotationAnimation.setRotationY(nextView, mFlipDirection == FLIP_DIRECTION_BACK_TO_FRONT ? -90 : 90);
        }

        final MtxRotationAnimation enterAnimation = new MtxRotationAnimation(axis,
                mFlipDirection == FLIP_DIRECTION_BACK_TO_FRONT ? 90 : -90, 0, mDuration);
        enterAnimation.setUsePerspective(mUsePerspective);
        enterAnimation.setAnimationListener(new AnimationEnd() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mDisplayView = nextView;
                mAnimating = false;
            }
        });

        MtxRotationAnimation leftAnimation = new MtxRotationAnimation(axis,
                0, mFlipDirection == FLIP_DIRECTION_BACK_TO_FRONT ? -90 : 90, mDuration);
        leftAnimation.setUsePerspective(mUsePerspective);
        leftAnimation.setAnimationListener(new AnimationEnd() {
            @Override
            public void onAnimationEnd(Animation animation) {
                nextView.startAnimation(enterAnimation);
            }
        });
        mAnimating = true;
        mDisplayView.startAnimation(leftAnimation);
    }

    private int checkIndex(int nextOrPrevIndex) {
        int count = getChildCount();
        if (nextOrPrevIndex >= count) nextOrPrevIndex = 0;
        if (nextOrPrevIndex < 0) nextOrPrevIndex = getChildCount() - 1;
        return nextOrPrevIndex;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewParent parent = getParent();
        // **Must** call setClipToPadding & setClipChildren in this method or onLayout
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).setClipToPadding(false);
            ((ViewGroup) parent).setClipChildren(false);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        prepare();
    }

    @Override
    public void onClick(View v) {
        if (!mTapToFlip) return;
        if (mAnimating) return;
        next();
    }
}
