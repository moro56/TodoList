package it.emperor.todolist.extension

import android.app.Activity
import androidx.core.content.ContextCompat
import it.emperor.alerter.Alerter
import it.emperor.todolist.R
import java.lang.ref.WeakReference

fun Activity.successAlert(message: String) = Alerter.instance.create().message(message)
        .backgroundColor(ContextCompat.getColor(this, R.color.alert_success))
        .textColor(ContextCompat.getColor(this, android.R.color.black))
        .show(WeakReference(this))

fun Activity.errorAlert(message: String) = Alerter.instance.create().message(message)
        .backgroundColor(ContextCompat.getColor(this, R.color.alert_error))
        .textColor(ContextCompat.getColor(this, android.R.color.black))
        .show(WeakReference(this))

fun Activity.warningAlert(message: String) = Alerter.instance.create().message(message)
        .backgroundColor(ContextCompat.getColor(this, R.color.alert_warning))
        .textColor(ContextCompat.getColor(this, android.R.color.black))
        .show(WeakReference(this))