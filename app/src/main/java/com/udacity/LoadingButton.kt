package com.udacity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
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
    private var loadingStatus = 0.0f
    private var valueAnimator = ValueAnimator()


    //Paint Object for the custom Button
    private val paintButton = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55f
        typeface = Typeface.DEFAULT
    }

    // And it'll change whenever [buttonState] changes
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


            }
            else -> {
                buttonText = defaultText.toString()
                // stop loading animation
                valueAnimator.cancel()
                loadingStatus = 0.0f
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
            defaultText =
                    getText(R.styleable.LoadingButton_defaultText)

            loadingText =
                    getText(R.styleable.LoadingButton_loadingText)
            textColor =
                    getColor(R.styleable.LoadingButton_textColor, 0)
        }.also {
            buttonText = defaultText.toString()
            buttonState = ButtonState.Clicked


        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Set the button color based on the loading status?
        paintButton.color = resources.getColor(R.color.colorPrimary)

        // Draw the base button
        canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintButton)

        // raw the loading rounded filler
        paintButton.color = resources.getColor(R.color.colorAccent)
        canvas?.drawRect(0f, 0f, (width / 2 - widthSize / 2 + loadingStatus).toFloat(), measuredHeight.toFloat(), paintButton)

        // Draw the loading labels
        paintButton.color = Color.WHITE
        buttonText.let {
            var bounds: Rect = Rect()
            paintButton.getTextBounds(buttonText, 0, buttonText.length, bounds)
            var textHeight = bounds.height()
            canvas?.drawText(buttonText, width / 2.toFloat(), (height / 2 + textHeight / 2).toFloat(), paintButton)
        }


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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun performClick(): Boolean {
        super.performClick()

        return true
    }


}