package it.emperor.alerter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration

class SwipeDismissTouchListener(val view: View, val token: Any, val callbacks: DismissCallbacks) : View.OnTouchListener {

    // Constant values
    private var slop: Int
    private var minFlingVelocity: Int
    private var animationTime: Int

    // Properties
    private var viewWidth: Int
    private var downX: Float = 0f
    private var downY: Float = 0f
    private var swiping: Boolean = false
    private var swipingSlop: Int = 0
    private var velocityTracker: VelocityTracker? = null
    private var translationX: Float = 0f

    init {
        val viewConfiguration = ViewConfiguration.get(view.context)
        slop = viewConfiguration.scaledTouchSlop
        minFlingVelocity = viewConfiguration.scaledMinimumFlingVelocity * 16
        animationTime = view.context.resources.getInteger(android.R.integer.config_shortAnimTime)

        viewWidth = 1
    }

    override fun onTouch(view: View, motionEvent: MotionEvent?): Boolean {
        motionEvent?.offsetLocation(translationX, 0f)

        if (viewWidth < 2) {
            viewWidth = this.view.width
        }

        when (motionEvent?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = motionEvent.rawX
                downY = motionEvent.rawY
                if (callbacks.canDismiss(token)) {
                    velocityTracker = VelocityTracker.obtain()
                    velocityTracker?.addMovement(motionEvent)
                }
                callbacks.onTouch(view, true)
            }
            MotionEvent.ACTION_UP -> {
                if (velocityTracker != null) {
                    val deltaX = motionEvent.rawX - downX
                    velocityTracker?.addMovement(motionEvent)
                    velocityTracker?.computeCurrentVelocity(1000)
                    val velocityX = velocityTracker?.xVelocity ?: 0f
                    val absVelocityX = Math.abs(velocityX)
                    val absVelocityY = Math.abs(velocityTracker?.yVelocity ?: 0f)
                    var dismiss = false
                    var dismissRight = false
                    if (Math.abs(deltaX) > viewWidth / 2 && swiping) {
                        dismiss = true
                        dismissRight = deltaX > 0
                    } else if (minFlingVelocity <= absVelocityX && absVelocityY < absVelocityX && swiping) {
                        dismiss = (velocityX < 0) == (deltaX < 0)
                        dismissRight = velocityX > 0
                    }
                    if (dismiss) {
                        this.view.animate()
                                .translationX(if (dismissRight) viewWidth.toFloat() else (-viewWidth).toFloat())
                                .alpha(0f)
                                .setDuration(animationTime.toLong())
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator?) {
                                        performDismiss()
                                    }
                                })
                    } else if (swiping) {
                        this.view.animate()
                                .translationX(0f)
                                .alpha(1f)
                                .setDuration(animationTime.toLong())
                                .setListener(null)
                        callbacks.onTouch(view, false)
                    }
                    velocityTracker?.recycle()
                    velocityTracker = null
                    translationX = 0f
                    downX = 0f
                    downY = 0f
                    swiping = false
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                if (velocityTracker != null) {
                    this.view.animate()
                            .translationX(0f)
                            .alpha(1f)
                            .setDuration(animationTime.toLong())
                            .setListener(null)
                    velocityTracker?.recycle()
                    velocityTracker = null
                    translationX = 0f
                    downX = 0f
                    downY = 0f
                    swiping = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (velocityTracker != null) {
                    velocityTracker?.addMovement(motionEvent)
                    val deltaX = motionEvent.rawX - downX
                    val deltaY = motionEvent.rawY - downY
                    if (Math.abs(deltaX) > slop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                        swiping = true
                        swipingSlop = if (deltaX > 0) slop else -slop
                        this.view.parent.requestDisallowInterceptTouchEvent(true)
                        val cancelEvent = MotionEvent.obtain(motionEvent)
                        cancelEvent.action = MotionEvent.ACTION_CANCEL or (motionEvent.actionIndex shl MotionEvent.ACTION_POINTER_INDEX_SHIFT)
                        this.view.onTouchEvent(cancelEvent)
                        cancelEvent.recycle()
                    }
                    if (swiping) {
                        translationX = deltaX
                        this.view.translationX = deltaX - swipingSlop
                        this.view.alpha = Math.max(0f, Math.min(1f, 1f - 2f * Math.abs(deltaX) / viewWidth))
                        return true
                    }
                }
            }
            else -> {
                view.performClick()
                return false
            }
        }
        return false
    }

    private fun performDismiss() {
        val lp = view.layoutParams
        val originalHeight = view.height

        val animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(animationTime.toLong())

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                callbacks.onDismiss(view, token)
                view.alpha = 1f
                view.translationX = 0f
                lp.height = originalHeight
                view.layoutParams = lp
            }
        })
        animator.addUpdateListener {
            lp.height = it.animatedValue as Int
            view.layoutParams = lp
        }

        animator.start()
    }

    interface DismissCallbacks {
        fun canDismiss(token: Any): Boolean
        fun onDismiss(view: View, token: Any)
        fun onTouch(view: View, touch: Boolean)
    }
}