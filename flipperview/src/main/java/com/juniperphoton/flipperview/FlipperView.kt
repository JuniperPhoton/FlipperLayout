package com.juniperphoton.flipperview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import com.juniperphoton.flipperviewlib.R
import com.juniperphoton.flipperview.animation.AnimationEnd
import com.juniperphoton.flipperview.animation.MtxRotationAnimation
import kotlin.properties.Delegates

@Suppress("unused")
class FlipperView(ctx: Context, attrs: AttributeSet) : FrameLayout(ctx, attrs) {
    companion object {
        const val DEFAULT_DURATION = 200
        const val FLIP_DIRECTION_BACK_TO_FRONT = 0
        const val FLIP_DIRECTION_FRONT_TO_BACK = 1
        const val AXIS_X = 0
        const val AXIS_Y = 1
    }

    /**
     * Flip direction, either FLIP_DIRECTION_BACK_TO_FRONT or FLIP_DIRECTION_FRONT_TO_BACK
     */
    var flipDirection: Int = FLIP_DIRECTION_BACK_TO_FRONT

    /**
     * Flip axis, either AXIS_X or AXIS_Y
     */
    var flipAxis = AXIS_X

    /**
     * Display index. Use this or next() or previous() or next(nextIndex: Int) to control the display
     * PLEASE be aware of out of bound exception
     */
    var displayIndex: Int by Delegates.observable(0) { _, old, new ->
        if (!prepared) {
            return@observable
        }
        if (old != new) {
            next(new)
        }
    }

    /**
     * Animation duration, in millis
     */
    var duration = DEFAULT_DURATION

    /**
     * All tap to flip
     */
    var tapToFlip: Boolean = true

    private lateinit var displayView: View

    private var prepared: Boolean = false
    private var animating: Boolean = false

    private var mtxRotation: Int = 0
        get() {
            return when (flipAxis) {
                AXIS_X -> MtxRotationAnimation.ROTATION_X
                AXIS_Y -> MtxRotationAnimation.ROTATION_Y
                else -> MtxRotationAnimation.ROTATION_X
            }
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlipperView)
        displayIndex = typedArray.getInt(R.styleable.FlipperView_defaultIndex, 0)
        flipDirection = typedArray.getInt(R.styleable.FlipperView_flipDirection, FLIP_DIRECTION_BACK_TO_FRONT)
        flipAxis = typedArray.getInt(R.styleable.FlipperView_flipAxis, AXIS_X)
        duration = typedArray.getInt(R.styleable.FlipperView_duration, DEFAULT_DURATION)
        tapToFlip = typedArray.getBoolean(R.styleable.FlipperView_tapToFlip, false)
        typedArray.recycle()

        clipChildren = false
        setOnClickListener {
            next()
        }
    }

    private fun prepare() {
        val children = arrayListOf<View>()
        for (i in 0..childCount - 1) {
            children.add(getChildAt(i))
            getChildAt(i).visibility = View.INVISIBLE
        }
        displayView = getChildAt(displayIndex)
        displayView.visibility = View.VISIBLE

        prepared = true
    }

    fun next() {
        if (!prepared) {
            prepare()
        }

        if (animating) return

        var nextIndex = displayIndex + 1
        nextIndex = checkIndex(nextIndex)
        displayIndex = nextIndex
    }

    fun previous() {
        if (!prepared) {
            prepare()
        }

        if (animating) return

        var prevIndex = displayIndex - 1
        prevIndex = checkIndex(prevIndex)
        displayIndex = prevIndex
    }

    fun next(nextIndex: Int) {
        val nextView = getChildAt(nextIndex)
        val enterAnimation = MtxRotationAnimation(mtxRotation,
                if (flipDirection == FLIP_DIRECTION_BACK_TO_FRONT) 90 else -90,
                0, duration.toLong())
        enterAnimation.setAnimationListener(object : AnimationEnd() {
            override fun onAnimationStart(animation: Animation?) {
                nextView.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {
                displayView = nextView
                animating = false
                nextView.clearAnimation()
            }
        })

        val leftAnimation = MtxRotationAnimation(mtxRotation,
                0,
                if (flipDirection == FLIP_DIRECTION_BACK_TO_FRONT) -90 else 90,
                duration.toLong())

        leftAnimation.setAnimationListener(object : AnimationEnd() {
            override fun onAnimationEnd(animation: Animation?) {
                displayView.visibility = View.INVISIBLE
                displayView.clearAnimation()
                nextView.startAnimation(enterAnimation)
            }
        })

        animating = true
        displayView.startAnimation(leftAnimation)
    }

    private fun checkIndex(nextOrPrevIndex: Int): Int {
        var index = nextOrPrevIndex
        val count = childCount
        if (index >= count) index = 0
        if (index < 0) index = childCount - 1
        return index
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val parent = parent
        // **Must** call setClipToPadding & setClipChildren in this method or onLayout
        if (parent != null && parent is ViewGroup) {
            parent.clipToPadding = false
            parent.clipChildren = false
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        prepare()
    }
}