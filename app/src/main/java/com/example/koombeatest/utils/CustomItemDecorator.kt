package com.example.koombeatest.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class CustomItemDecorator (private val horizontalSpacing : Int, private val verticalSpacing : Int)
    : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        Timber.d("Width before adding horizontal space: ${view.layoutParams.width}")
        if(parent.getChildLayoutPosition(view) != parent.adapter?.itemCount?.minus(1)) {
            if(horizontalSpacing == 0){
                val space = (view.layoutParams.width * 0.08).toInt()
                outRect.right = space
                Timber.d("Set horizontal spacing to: $space")

            } else {
                outRect.right = horizontalSpacing
            }
        }
    }
}