package com.juniperphoton.flipperlayout

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import com.juniperphoton.flipperlayout.animation.MtxRotationAnimation
import com.juniperphoton.flipperlayout.animation.SimpleAnimationListener
import com.juniperphoton.flipperviewlib.R

typealias ViewAction = ((View) -> Unit)

/**
 * A view to perform perspective rotation while changing different child views.
 * You are allowed to configure its [flipDirection] and [flipAxis] and [duration] properties at runtime.
 *
 * Calling [next] or [prepare] to segue views.
 *
 */
@Suppress("unused")
class FlipperLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    companion object {
        const val DEFAULT_DURATION_MILLIS = 200L
        const val FLIP_DIRECTION_BACK_TO_FRONT = 0
        const val FLIP_DIRECTION_FRONT_TO_BACK = 1
        const val AXIS_X = 0
        const val AXIS_Y = 1
    }

    /**
     * Flip direction, either [FLIP_DIRECTION_BACK_TO_FRONT] or [FLIP_DIRECTION_FRONT_TO_BACK].
     */
    var flipDirection: Int = FLIP_DIRECTION_BACK_TO_FRONT

    /**
     * Flip axis, either [AXIS_X] or [AXIS_Y].
     */
    var flipAxis: Int = AXIS_X

    /**
     * Animation duration, in millis. Default value is [DEFAULT_DURATION_MILLIS].
     */
    var duration: Long = DEFAULT_DURATION_MILLIS

    /**
     * Allow tapping to toggle flip or not.
     */
    var tapToFlip: Boolean = true

    /**
     * Get the current display index.
     *
     * To set this value, use [next] and its overload methods.
     */
    var displayIndex: Int = 0
        private set

    private lateinit var displayView: View

    private var prepared: Boolean = false
    private var animating: Boolean = false

    private val defaultRefreshAction = object : ViewAction {
        override fun invoke(v: View) {
            v.visibility = View.INVISIBLE
        }
    }

    private var mtxRotation: Int = 0
        get() = when (flipAxis) {
            AXIS_X -> MtxRotationAnimation.ROTATION_X
            AXIS_Y -> MtxRotationAnimation.ROTATION_Y
            else -> MtxRotationAnimation.ROTATION_X
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlipperLayout)
        displayIndex = typedArray.getInt(R.styleable.FlipperLayout_defaultIndex, 0)
        flipDirection = typedArray.getInt(R.styleable.FlipperLayout_flipDirection, FLIP_DIRECTION_BACK_TO_FRONT)
        flipAxis = typedArray.getInt(R.styleable.FlipperLayout_flipAxis, AXIS_X)
        duration = typedArray.getInt(R.styleable.FlipperLayout_duration, DEFAULT_DURATION_MILLIS.toInt()).toLong()
        tapToFlip = typedArray.getBoolean(R.styleable.FlipperLayout_tapToFlip, false)
        typedArray.recycle()

        clipToPadding = false
        clipChildren = false

        if (tapToFlip) {
            setOnClickListener {
                next()
            }
        }
    }

    private fun prepare() {
        val children = arrayListOf<View>()
        for (i in 0 until childCount) {
            children.add(getChildAt(i))
            getChildAt(i).visibility = View.INVISIBLE
        }
        displayView = getChildAt(displayIndex)
        displayView.visibility = View.VISIBLE

        prepared = true
    }

    /**
     * Segue to next view.
     */
    fun next() {
        if (!prepared) {
            prepare()
        }

        if (animating) return

        val nextIndex = displayIndex + 1
        next(checkIndex(nextIndex), true, defaultRefreshAction)
    }

    /**
     * Segue to previous view.
     */
    fun previous() {
        if (!prepared) {
            prepare()
        }

        if (animating) return

        val nextIndex = displayIndex - 1
        next(checkIndex(nextIndex), true, defaultRefreshAction)
    }

    /**
     * Perform the flipping animation and update the view by [refreshBlock] on animation end.
     *
     * NOTE: don't do heavy things in [refreshBlock] to block UI.
     */
    fun refreshCurrent(refreshBlock: ViewAction) {
        next(displayIndex, true, refreshBlock)
    }

    /**
     * Segue to view at [nextIndex]. Setting [animate] to false to disable animation.
     * Use [refreshBlock] to update [displayView]'s UI on enter animation end.
     * Use [endBlock] to perform custom action on the exit animation end.
     *
     * NOTE: don't do heavy things in [refreshBlock] to block UI.
     */
    fun next(nextIndex: Int,
             animate: Boolean = true,
             refreshBlock: ViewAction = defaultRefreshAction,
             endBlock: ViewAction? = null) {
        if (animating) {
            return
        }

        displayIndex = nextIndex

        val nextView = getChildAt(nextIndex)

        if (shouldResetElevationDuringAnimation()) {
            ElevationHelper.save(nextView)
            ElevationHelper.save(displayView)
        }

        if (!animate) {
            refreshBlock.invoke(displayView)
            nextView.visibility = View.VISIBLE
            displayView = nextView
            return
        }

        val fromDeg = if (flipDirection == FLIP_DIRECTION_BACK_TO_FRONT) 90 else -90
        val enterAnimation = MtxRotationAnimation(mtxRotation, fromDeg, 0, duration).apply {
            setAnimationListener(object : SimpleAnimationListener() {
                override fun onAnimationStart(animation: Animation?) {
                    nextView.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (shouldResetElevationDuringAnimation()) {
                        ElevationHelper.restore(nextView)
                        ElevationHelper.restore(displayView)
                        ElevationHelper.clear()
                    }

                    displayView = nextView
                    animating = false
                    nextView.clearAnimation()
                    endBlock?.invoke(displayView)
                }
            })
        }

        val toDeg = if (flipDirection == FLIP_DIRECTION_BACK_TO_FRONT) -90 else 90
        val exitAnimation = MtxRotationAnimation(mtxRotation, 0, toDeg, duration).apply {
            setAnimationListener(object : SimpleAnimationListener() {
                override fun onAnimationEnd(animation: Animation?) {
                    refreshBlock.invoke(displayView)
                    displayView.clearAnimation()
                    nextView.startAnimation(enterAnimation)
                }
            })
        }

        animating = true
        displayView.startAnimation(exitAnimation)
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IndexOutOfBoundsException::class)
    fun <T> getCurrentView(): T {
        return getChildAt(displayIndex) as T
    }

    /**
     * For Android P, the shadow is drawn by hardware layer and is not manipulated
     * by matrix in Animation, thus we disable the elevation during animation.
     */
    private fun shouldResetElevationDuringAnimation(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
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
        // Must setting [clipToPadding] and [clipChildren] in this place or onLayout
        if (parent is ViewGroup) {
            parent.clipToPadding = false
            parent.clipChildren = false
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        prepare()
    }
}