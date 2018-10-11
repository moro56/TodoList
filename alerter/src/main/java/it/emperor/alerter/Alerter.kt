package it.emperor.alerter

import android.app.Activity
import android.graphics.Color
import java.lang.ref.WeakReference

// https://github.com/Tapadoo/Alerter
open class Alerter private constructor() {

    private object Holder {
        val INSTANCE = Alerter()
    }

    companion object {
        val instance: Alerter by lazy { Holder.INSTANCE }
    }

    private var alertInfoList: MutableList<AlertInfo> = ArrayList()
    private var alertInfo: AlertInfo? = null
    private var alertView: AlertView? = null

    enum class ReplaceType {
        REPLACE, IGNORE
    }

    fun create(): Alerter {
        this.alertInfo = AlertInfo()
        return this
    }

    fun message(message: String): Alerter {
        alertInfo?.message = message
        return this
    }

    fun replaceType(replaceType: ReplaceType): Alerter {
        alertInfo!!.replaceType = replaceType
        return this
    }

    fun backgroundColor(color: Int): Alerter {
        alertInfo!!.color = color
        return this
    }

    fun textColor(color: Int): Alerter {
        alertInfo!!.textColor = color
        return this
    }

    fun show(activity: WeakReference<Activity>) {
        alertInfo?.let {
            when (it.replaceType) {
                Alerter.ReplaceType.REPLACE -> {
                    if (alertView != null && alertView?.parent != null) {
                        alertView?.forceHide()
                    }
                    alertInfoList.add(it)
                    innerShow(activity)
                }
                Alerter.ReplaceType.IGNORE -> if (alertView == null || alertView?.parent == null) {
                    alertInfoList.add(it)
                    innerShow(activity)
                }
            }
        }
    }

    private fun createAlert(activityRef: WeakReference<Activity>, info: AlertInfo): AlertView? {
        val activity = activityRef.get()
        if (activity != null) {
            val alertView = AlertView(activity)
            alertView.setMessage(info.message)
            alertView.setBgColor(info.color)
            alertView.setTextColor(info.textColor)
            return alertView
        } else {
            return null
        }
    }

    private fun innerShow(activity: WeakReference<Activity>) {
        if (!alertInfoList.isEmpty()) {
            val info = alertInfoList.removeAt(0)
            alertView = createAlert(activity, info)
            if (alertView != null) {
                alertView?.show(activity)
            }
        }
    }

    private inner class AlertInfo {
        var message = ""
        var replaceType = ReplaceType.REPLACE
        var color = Color.DKGRAY
        var textColor = Color.BLACK
    }
}