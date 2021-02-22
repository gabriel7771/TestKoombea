package com.example.koombeatest.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.accessibility.AccessibilityEvent
import androidx.core.graphics.times
import com.example.koombeatest.R
import com.example.koombeatest.databinding.PopupImageBinding
import com.example.koombeatest.utils.removeBlurEffect
import jp.wasabeef.blurry.Blurry
import timber.log.Timber
import kotlin.math.abs


class PopupImageDialog(context: Context, private val pictureUrl: String, private val parent: View?) : Dialog(
    context,
    R.style.PopupImageDialog
){
    lateinit var gestureDetector : GestureDetector
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PopupImageBinding.inflate(LayoutInflater.from(context))
        this.setContentView(binding.root)

        gestureDetector = GestureDetector(context, MyGestureDetector(this, parent))
        binding.root.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

        val window = this.window
        val displayRect = Rect()
        window?.decorView?.getWindowVisibleDisplayFrame(displayRect)
        window?.setLayout(
            (displayRect.width() * 0.9f).toInt(),
            (displayRect.width() * 0.9f).toInt()
        )
        window?.setGravity(Gravity.CENTER)
        window?.callback = windowCallback
        binding.picture = pictureUrl
    }

    private val windowCallback: Window.Callback = object : Window.Callback {
        override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
            if(event?.keyCode == KeyEvent.KEYCODE_BACK) {
                removeBlurEffect(parent)
                dismiss()
            }
            return false
        }

        override fun dispatchKeyShortcutEvent(event: KeyEvent?): Boolean {
            return false
        }

        override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
            if(gestureDetector != null){
                return gestureDetector.onTouchEvent(event)
            }
            return false
        }

        override fun dispatchTrackballEvent(event: MotionEvent?): Boolean {
            return false
        }

        override fun dispatchGenericMotionEvent(event: MotionEvent?): Boolean {
            return false
        }

        override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
            return false
        }

        override fun onCreatePanelView(featureId: Int): View? {
            return null
        }

        override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
            return false
        }

        override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
            return false
        }

        override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
            return false
        }

        override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
            return false
        }

        override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams?) {
        }

        override fun onContentChanged() {
        }

        override fun onWindowFocusChanged(hasFocus: Boolean) {
        }

        override fun onAttachedToWindow() {
        }

        override fun onDetachedFromWindow() {
        }

        override fun onPanelClosed(featureId: Int, menu: Menu) {
        }

        override fun onSearchRequested(): Boolean {
            return false
        }

        override fun onSearchRequested(searchEvent: SearchEvent?): Boolean {
            return false
        }

        override fun onWindowStartingActionMode(callback: android.view.ActionMode.Callback?): android.view.ActionMode? {
            return null
        }

        override fun onWindowStartingActionMode(
            callback: android.view.ActionMode.Callback?,
            type: Int
        ): android.view.ActionMode? {
            return null
        }

        override fun onActionModeStarted(mode: android.view.ActionMode?) {
        }

        override fun onActionModeFinished(mode: android.view.ActionMode?) {
        }

    }

    class MyGestureDetector(private val dialog: Dialog, private val parent: View?) : SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent, e2: MotionEvent, velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE
                && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
            ) {
                Timber.d("swipe right to left")
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE
                && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
            ) {
                Timber.d("swipe left to right")
            } else if (e1.y - e2.y > SWIPE_MIN_DISTANCE
                && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY
            ) {
                Timber.d("swipe bottom to top")

            } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE
                && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY
            ) {
                Timber.d("swipe top to bottom")
                removeBlurEffect(parent)
                dialog.dismiss()
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        companion object {
            private const val SWIPE_MIN_DISTANCE = 120
            private const val SWIPE_THRESHOLD_VELOCITY = 200
        }
    }
}
