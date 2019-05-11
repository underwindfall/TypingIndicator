package com.qifan.library

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

/**
 * Created by Qifan on 2019-05-11.
 */

class ChatDotView : View {
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    )

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )


    @ColorInt
    private var dotFirstColor: Int = Color.RED
    @ColorInt
    private var dotSecondColor: Int = Color.BLUE
    @ColorInt
    private var dotColor: Int = Color.TRANSPARENT

    private var animationTotalDuration: Long = 600

    private val paint: Paint = Paint()
    private val ovalRectF = RectF()
    private var centerX: Int = 0
    private var centerY: Int = 0
    private var radius: Float = 0f
    private val targetAlpha = 1f
    private val originAlpha = 0.3f
    private var currentAlpha = originAlpha
    private var fadeAnimator = ObjectAnimator()


    init {
        this.alpha = originAlpha
    }

    fun setColor(@ColorInt color: Int) {
        dotFirstColor = color
        dotColor = dotFirstColor
    }

    fun setSecondColor(@ColorInt color: Int) {
        dotSecondColor = color
    }

    fun setAnimationDuration(duration: Long) {
        animationTotalDuration = duration
    }


    override fun onDraw(canvas: Canvas) {
        paint.color = dotColor
        canvas.drawOval(ovalRectF, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = width / 2
        centerY = height / 2
        radius = Math.min(width, height) / 2f
        ovalRectF.left = centerX - radius
        ovalRectF.top = centerY - radius
        ovalRectF.right = centerX + radius
        ovalRectF.bottom = centerY + radius
    }


    private fun updateAlpha() {
        this.alpha = currentAlpha
        invalidate()
    }

    fun startDotAnimation() {
        stopDotAnimation()
        fadeAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, targetAlpha)
        fadeAnimator.duration = animationTotalDuration / 2
        fadeAnimator.interpolator = FastOutSlowInInterpolator()
        fadeAnimator.repeatCount = 1
        fadeAnimator.repeatMode = ValueAnimator.REVERSE
        fadeAnimator.addUpdateListener { animation ->
            currentAlpha = animation.animatedValue as Float
            updateAlpha()
        }
        fadeAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                dotColor = dotFirstColor
                currentAlpha = originAlpha
                updateAlpha()
            }
        })
        fadeAnimator.start()
    }

    fun stopDotAnimation() {
        if (isAnimating()) {
            fadeAnimator.cancel()
        }
    }

    @CheckResult
    fun isAnimating(): Boolean = fadeAnimator.isStarted

}