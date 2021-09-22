package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

// create the LoadingButton class and provide the constructor

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    //text attributes
    private var defaultText: CharSequence = ""
    private var loadingText: CharSequence = ""
    private var textColor = 0
    private var defaultColor = 0
    private var loadingColor = 0
    val textBounds: Rect = Rect()

    private var loadingStatus = 0.0f
    private var valueAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()
    private var arcSweepAngle = 0f
    private var arcColor = 0


    //Paint Object for the custom Button
    private val paintButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55f
        typeface = Typeface.DEFAULT
    }

    // change whenever buttonState changes
    private var buttonText = ""

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {

                buttonText = loadingText.toString()
                //start the loading animation
                valueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat()).apply {
                    duration = 3000
                    addUpdateListener { valueAnimator ->
                        loadingStatus = valueAnimator.animatedValue as Float
                        invalidate()
                    }
                }
                valueAnimator.repeatMode = ValueAnimator.REVERSE
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.start()

                // circle animation
                circleAnimator = ValueAnimator.ofFloat(0F, 360F, arcSweepAngle).apply {
                    duration = 1000
                    addUpdateListener { valueAnimator ->
                        arcSweepAngle = valueAnimator.animatedValue as Float
                        valueAnimator.repeatCount = ValueAnimator.INFINITE

                        invalidate()
                    }
                }
                circleAnimator.start()


            }
            else -> {
                buttonText = defaultText.toString()
                // stop all animations
                valueAnimator.cancel()
                circleAnimator.cancel()
                loadingStatus = 0.0f
                arcSweepAngle = 0.0f
                invalidate()

            }
        }
    }


    init {
        isClickable = true
        context.withStyledAttributes(
                set = attrs,
                attrs = R.styleable.LoadingButton
        ) {
            defaultColor =
                    getColor(R.styleable.LoadingButton_defaultColor, 0)
            loadingColor =
                    getColor(R.styleable.LoadingButton_loadingColor, 0)
            defaultText =
                    getText(R.styleable.LoadingButton_defaultText)

            loadingText =
                    getText(R.styleable.LoadingButton_loadingText)
            textColor =
                    getColor(R.styleable.LoadingButton_textColor, 0)
            arcColor = getColor(R.styleable.LoadingButton_arcColor, 0)
        }.also {
            buttonText = defaultText.toString()
            buttonState = ButtonState.Clicked


        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paintButton.color = defaultColor

        // Draw the base button
        canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintButton)

        // draw the loading filler
        paintButton.color = loadingColor
        canvas?.drawRect(0f, 0f, (width / 2 - widthSize / 2 + loadingStatus).toFloat(), measuredHeight.toFloat(), paintButton)

        // Draw the labels
        paintButton.color = Color.WHITE
        buttonText.let {
            paintButton.getTextBounds(buttonText, 0, buttonText.length, textBounds)
            val textHeight = textBounds.height()
            canvas?.drawText(buttonText, width / 2.toFloat(), (height / 2 + textHeight / 2).toFloat(), paintButton)
        }
        // Draw the circle
        paintButton.color = arcColor
        canvas?.drawArc(computeArcBounds(), 0f, arcSweepAngle, true, paintButton)


    }

    fun computeArcBounds(): RectF {
        val arcBounds =
                RectF((width * 0.85 - 20).toFloat(), (height / 2 - 20).toFloat(), (width * 0.85 + 20).toFloat(), (height / 2 + 20).toFloat())
        return arcBounds

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()

        return true
    }


}