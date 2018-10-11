package it.emperor.alerter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import it.emperor.alerter.extension.toPx
import kotlinx.android.synthetic.main.alert_view.view.*
import java.lang.ref.WeakReference

class AlertView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener, Animation.AnimationListener, SwipeDismissTouchListener.DismissCallbacks {

    private val slideIn: Animation
    private val slideOut: Animation
    private val alphaOut: Animation

    private val hideRunnable: Runnable
    private var marginSet: Boolean

    init {
        inflate(context, R.layout.alert_view, this)
        isHapticFeedbackEnabled = true

        bg.setOnClickListener(this)
        bg.setOnTouchListener(SwipeDismissTouchListener(bg, 0, this))

        slideIn = AnimationUtils.loadAnimation(context, R.anim.alert_slide_in)
        slideOut = AnimationUtils.loadAnimation(context, R.anim.alert_slide_out)
        alphaOut = AnimationUtils.loadAnimation(context, R.anim.alert_alpha_out)

        animation = slideIn

        marginSet = false

        hideRunnable = Runnable {
            hide()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!marginSet) {
            marginSet = true

            (layoutParams as ViewGroup.MarginLayoutParams).topMargin = ((-26f).toPx()).toInt()
            requestLayout()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        slideIn.setAnimationListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        slideIn.setAnimationListener(null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        performClick()
        return super.onTouchEvent(event)
    }

    override fun onClick(p0: View?) {
        removeCallbacks(hideRunnable)
        hide()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        bg.setOnClickListener(l)
    }

    override fun onAnimationEnd(p0: Animation?) {
        startHideAnimation()
    }

    private fun startHideAnimation() {
        postDelayed(hideRunnable, 3000)
    }

    override fun onAnimationStart(p0: Animation?) {
        visibility = View.VISIBLE
    }

    override fun onAnimationRepeat(p0: Animation?) {
    }

    fun forceHide() {
        alphaOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                removeFromParent()
            }

            override fun onAnimationStart(p0: Animation?) {
                bg.setOnClickListener(null)
                bg.isClickable = false
            }
        })
        startAnimation(alphaOut)
    }

    private fun hide() {
        slideOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                removeFromParent()
            }

            override fun onAnimationStart(p0: Animation?) {
                bg.setOnClickListener(null)
                bg.isClickable = false
            }
        })
        startAnimation(slideOut)
    }

    private fun removeFromParent() {
        Handler().postDelayed({
            if (parent != null) {
                (parent as ViewGroup).removeView(this)
            }
        }, 100)
    }

    fun setMessage(message: String) {
        text.text = message
    }

    fun setBgColor(color: Int) {
        bg.setBackgroundColor(color)
    }

    fun setTextColor(color: Int) {
        text.setTextColor(color)
    }

    fun show(activity: WeakReference<Activity>) {
        val decorView: ViewGroup? = activity.get()?.window?.decorView as ViewGroup?
        if (decorView != null && parent == null) {
            decorView.addView(this)
        }
    }

    override fun canDismiss(token: Any): Boolean {
        return true
    }

    override fun onDismiss(view: View, token: Any) {
        click_shield.removeView(bg)
    }

    override fun onTouch(view: View, touch: Boolean) {
        if (touch) {
            removeCallbacks(hideRunnable)
        } else {
            startHideAnimation()
        }
    }
}