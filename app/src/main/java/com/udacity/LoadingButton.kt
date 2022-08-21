package com.udacity

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var bgColor: Int = Color.BLACK
    private var textColor: Int = Color.BLACK
    private var valueAnimator = ValueAnimator()

    private var progress: Double = 0.0

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->

    }
    private val updateListener = ValueAnimator.AnimatorUpdateListener {

        progress = (it.animatedValue as Float).toDouble()
        invalidate()

    }


    init {
        isClickable = true

        valueAnimator = AnimatorInflater.loadAnimator(
            context, R.animator.animation
        ) as ValueAnimator

        valueAnimator.addUpdateListener(updateListener)
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                buttonState = ButtonState.Completed
            }
        })
        val attr = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0,
            0
        )
        bgColor = attr.getColor(
            R.styleable.LoadingButton_bgColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )

        textColor = attr.getColor(
            R.styleable.LoadingButton_textColor,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 70.0f
        typeface = Typeface.create("", Typeface.BOLD_ITALIC)
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (buttonState == ButtonState.Completed) {
            buttonState = ButtonState.Loading
        }
        animation()
        return true
    }

    private fun animation() {
        valueAnimator.start()
    }

    private var rect = RectF(
        740f,
        50f,
        810f,
        110f
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = bgColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)


        if (buttonState == ButtonState.Loading) {
            paint.color = Color.parseColor("#004349")
            canvas.drawRect(
                0f, 0f,
                (width * (progress / 100)).toFloat(), height.toFloat(), paint
            )
            paint.color = Color.parseColor("#F9A825")
            canvas.drawArc(rect, 0f, (360 * (progress / 100)).toFloat(), true, paint)
        }
        val buttonText =
            if (buttonState == ButtonState.Loading)
                resources.getString(R.string.button_loading)
            else resources.getString(R.string.download)

        paint.color = textColor
        canvas.drawText(
            buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(),
            paint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        setMeasuredDimension(w, h)
    }

}