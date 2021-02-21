package com.example.koombeatest.utils

import android.content.Context
import android.util.AttributeSet

class SquareImageView(context: Context) : androidx.appcompat.widget.AppCompatImageView(
    context
) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        setMeasuredDimension(width, width)
    }
}

