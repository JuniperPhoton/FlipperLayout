package com.juniperphoton.flipperviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.juniperphoton.flipperviewlib.animation.AnimationEnd;
import com.juniperphoton.flipperviewlib.animation.MtxRotationAnimation;

import java.util.ArrayList;
import java.util.List;

public class FlipperView extends FrameLayout implements View.OnClickListener {
    private final static int FLIP_DIRECTION_BACK_TO_FRONT = 0;
    private final static int FLIP_DIRECTION_FRONT_TO_BACK = 1;
    private final static int DEFAULT_DURATION = 200;

    private int mFlipDirection;
    private int mDisplayIndex = 0;
    private int mDuration;
    private View mDisplayView;
    private boolean mUsePerspective = true;

    private boolean mPrepared;
    private boolean mAnimating;

    public FlipperView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlipperView);
        mDisplayIndex = typedArray.getInt(R.styleable.FlipperView_default_index, 0);
        mFlipDirection = typedArray.getInt(R.styleable.FlipperView_flip_direction, 0);
        mUsePerspective = typedArray.getBoolean(R.styleable.FlipperView_use_perspective, true);
        mDuration = typedArray.getInt(R.styleable.FlipperView_duration, DEFAULT_DURATION);
        typedArray.recycle();

        setClipChildren(false);
        setOnClickListener(this);
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
        int nextIndex = mDisplayIndex + 1;
        nextIndex = checkIndex(nextIndex);
        mDisplayIndex = nextIndex;
        next(nextIndex);
    }

    public void next(int nextIndex) {
        final View nextView = getChildAt(nextIndex);
        nextView.setVisibility(VISIBLE);
        MtxRotationAnimation.setRotationX(nextView, mFlipDirection == FLIP_DIRECTION_BACK_TO_FRONT ? -90 : 90);

        final MtxRotationAnimation enterAnimation = new MtxRotationAnimation(
                mFlipDirection == FLIP_DIRECTION_BACK_TO_FRONT ? 90 : -90, 0, mDuration);
        enterAnimation.setAnimationListener(new AnimationEnd() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mDisplayView = nextView;
                mAnimating = false;
            }
        });
        MtxRotationAnimation leftAnimation = new MtxRotationAnimation(0,
                mFlipDirection == FLIP_DIRECTION_BACK_TO_FRONT ? -90 : 90, mDuration);
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        prepare();
    }

    @Override
    public void onClick(View v) {
        if (mAnimating) return;
        next();
    }
}
