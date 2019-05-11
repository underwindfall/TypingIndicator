package com.qifan.library


import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.annotation.UiThread
import com.qifan.library.extensions.withStyleableRes

/**
 * Created by Qifan on 2019-05-11.
 */

class ChatTypingIndicatorView : LinearLayout {
    private val BACKGROUND_COLOR_DEF_VALUE = Color.LTGRAY
    private val DOT_COUNT_DEF_VALUE = 3
    private val DOT_SIZE_DEF_VALUE = 36
    private val DOT_COLOR_DEF_VALUE = Color.LTGRAY
    private val DOT_ANIMATION_DURATION_DEF_VALUE = 400
    private val DOT_HORIZONTAL_SPACING_DEF_VALUE = 20
    private val ANIMATE_FREQUENCY_DEF_VALUE = 100
    private val dotViewList: MutableList<ChatDotView> = mutableListOf()

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        parseAttributes(attrs)
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    @IntRange
    private var numOfDots: Int = DOT_COUNT_DEF_VALUE

    private var isAnimationStarted: Boolean = false

    @IntRange(from = 1)
    private var dotHorizontalSpacing: Int = DOT_HORIZONTAL_SPACING_DEF_VALUE
    @IntRange(from = 1)
    private var dotSize = DOT_SIZE_DEF_VALUE

    @ColorInt
    private var backgroudColor: Int = BACKGROUND_COLOR_DEF_VALUE

    @ColorInt
    private var dotColor: Int = DOT_COLOR_DEF_VALUE

    @ColorInt
    private var dotSecondColor: Int = DOT_COLOR_DEF_VALUE

    @IntRange(from = 1)
    private var dotAnimationDuration: Int = DOT_ANIMATION_DURATION_DEF_VALUE

    @IntRange(from = 1)
    private var animateFrequency: Int = ANIMATE_FREQUENCY_DEF_VALUE

    private val sequenceGenerator: SequenceGenerator = SequenceGenerator()

    private val animationHandler: Handler = Handler()

    private val dotAnimationRunnable: Runnable = Runnable { dotAnimation() }

    private fun parseAttributes(attrs: AttributeSet?) {
        withStyleableRes(attrs, R.styleable.TypingIndicatorView) {
            dotSize = getDimensionPixelSize(R.styleable.TypingIndicatorView_dotSize, DOT_SIZE_DEF_VALUE)
            numOfDots = getInteger(R.styleable.TypingIndicatorView_dotCount, DOT_COUNT_DEF_VALUE)
            dotHorizontalSpacing = getDimensionPixelSize(
                R.styleable.TypingIndicatorView_dotHorizontalSpacing,
                DOT_HORIZONTAL_SPACING_DEF_VALUE
            )
            dotColor = getColor(R.styleable.TypingIndicatorView_dotColor, DOT_COLOR_DEF_VALUE)
            dotSecondColor = getColor(R.styleable.TypingIndicatorView_dotSecondColor, dotColor)
            dotAnimationDuration = getInteger(
                R.styleable.TypingIndicatorView_dotAnimationDuration,
                DOT_ANIMATION_DURATION_DEF_VALUE
            )
            backgroudColor = getColor(R.styleable.TypingIndicatorView_backgroundColor, BACKGROUND_COLOR_DEF_VALUE)
            animateFrequency = getInteger(
                R.styleable.TypingIndicatorView_animateFrequency,
                Math.max(dotAnimationDuration, ANIMATE_FREQUENCY_DEF_VALUE)
            )
        }
    }

    private fun init() {
        clipToPadding = false
        clipChildren = false
        val halfHorizontalSpacing = dotHorizontalSpacing / 2
        for (i in 0 until numOfDots) {
            val dotView: ChatDotView = createDotView()
            val layoutParams: LinearLayout.LayoutParams = LayoutParams(dotSize, dotSize)
            layoutParams.gravity = Gravity.CENTER_VERTICAL
            layoutParams.setMargins(halfHorizontalSpacing, 0, halfHorizontalSpacing, 0)
            addView(dotView, layoutParams)
            dotViewList.add(dotView)
        }
    }

    private fun createDotView(): ChatDotView {
        val dotView = ChatDotView(context)
        dotView.setAnimationDuration(dotAnimationDuration.toLong())
        dotView.setColor(dotColor)
        dotView.setSecondColor(dotSecondColor)
        return dotView
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            if (visibility == View.VISIBLE) {
                startDotAnimation()
            }
        } else {
            stopDotAnimation()
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            startDotAnimation()
        } else {
            stopDotAnimation()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (visibility == View.VISIBLE) {
            startDotAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopDotAnimation()
    }

    @UiThread
    fun startDotAnimation() {
        if (isAnimationStarted) {
            return
        }

        isAnimationStarted = true
        animationHandler.post(dotAnimationRunnable)
    }

    @UiThread
    fun stopDotAnimation() {
        isAnimationStarted = false
        animationHandler.removeCallbacksAndMessages(null)
    }

    private fun dotAnimation() {
        val nextAnimateDotIndex = sequenceGenerator.nextIndex(numOfDots)
        dotViewList[nextAnimateDotIndex].startDotAnimation()
        val delayMillis = animateFrequency.toLong()
        if (isAnimationStarted) {
            animationHandler.postDelayed(dotAnimationRunnable, delayMillis)
        }
    }

}